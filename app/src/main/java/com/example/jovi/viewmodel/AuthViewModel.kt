package com.example.jovi.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.AccountType
import com.example.jovi.data.db.entity.UserEntity
import com.example.jovi.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserEntity) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val userRepository: UserRepository,
    private val prefs: SharedPreferences,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    init {
        restoreSession()
    }

    private fun restoreSession() {
        val savedEmail = prefs.getString("current_user_email", null) ?: return
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(savedEmail)
            if (user != null) {
                _currentUser.value = user
                _authState.value = AuthState.Success(user)
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank()) { _authState.value = AuthState.Error("Ingresa tu correo"); return }
        if (password.isBlank()) { _authState.value = AuthState.Error("Ingresa tu contraseña"); return }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Retry once to handle the case where DB seed is still running on first launch
            var user = userRepository.loginWithPassword(email.trim(), password)
            if (user == null && userRepository.getUserByEmail(email.trim()) == null) {
                delay(1500)
                user = userRepository.loginWithPassword(email.trim(), password)
            }
            if (user != null) {
                saveSession(user)
                _currentUser.value = user
                _authState.value = AuthState.Success(user)
            } else {
                val exists = userRepository.getUserByEmail(email.trim())
                _authState.value = AuthState.Error(
                    if (exists != null) "Contraseña incorrecta" else "Email no encontrado"
                )
            }
        }
    }

    fun loginAsDemo(accountType: AccountType = AccountType.STUDENT) {
        val email = if (accountType == AccountType.RECRUITER) "hr@innovatech.com" else "carlos@example.com"
        val pass = if (accountType == AccountType.RECRUITER) "innovatech123" else "carlos123"
        login(email, pass)
    }

    fun logout() {
        prefs.edit().remove("current_user_id").remove("current_user_email").apply()
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    fun clearError() { _authState.value = AuthState.Idle }

    fun updatePassword(newPassword: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updated = user.copy(password = newPassword)
            userRepository.update(updated)
            _currentUser.value = updated
        }
    }

    fun updateProfile(updated: UserEntity) {
        viewModelScope.launch {
            userRepository.update(updated)
            _currentUser.value = updated
        }
    }

    private fun saveSession(user: UserEntity) {
        prefs.edit()
            .putLong("current_user_id", user.id)
            .putString("current_user_email", user.email)
            .apply()
    }

    class Factory(
        private val repository: UserRepository,
        private val prefs: SharedPreferences,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, prefs) as T
        }
    }
}
