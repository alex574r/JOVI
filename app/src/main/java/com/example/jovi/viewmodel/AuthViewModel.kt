package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.AccountType
import com.example.jovi.data.db.entity.UserEntity
import com.example.jovi.data.repository.UserRepository
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

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = userRepository.getUserByEmail(email)
            if (user != null) {
                _currentUser.value = user
                _authState.value = AuthState.Success(user)
            } else {
                _authState.value = AuthState.Error("Usuario no encontrado")
            }
        }
    }

    fun loginAsDemo(accountType: AccountType = AccountType.STUDENT) {
        viewModelScope.launch {
            val demoEmail = if (accountType == AccountType.RECRUITER) "hr@innovatech.com" else "carlos@example.com"
            val user = userRepository.getUserByEmail(demoEmail)
            if (user != null) {
                _currentUser.value = user
                _authState.value = AuthState.Success(user)
            } else {
                _authState.value = AuthState.Idle
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    fun clearError() { _authState.value = AuthState.Idle }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
    }
}
