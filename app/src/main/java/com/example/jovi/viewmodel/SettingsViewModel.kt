package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.UserSettingsEntity
import com.example.jovi.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _settings = MutableStateFlow<UserSettingsEntity?>(null)
    val settings: StateFlow<UserSettingsEntity?> = _settings.asStateFlow()

    fun loadSettings(userId: Long) {
        viewModelScope.launch {
            userRepository.getSettings(userId).collect { _settings.value = it }
        }
    }

    fun toggleDarkMode() = updateSettings { copy(darkMode = !darkMode) }
    fun toggleNotifications() = updateSettings { copy(notificationsEnabled = !notificationsEnabled) }
    fun toggleMatchNotifications() = updateSettings { copy(matchNotifications = !matchNotifications) }
    fun toggleMessageNotifications() = updateSettings { copy(messageNotifications = !messageNotifications) }
    fun toggleBiometric() = updateSettings { copy(biometricEnabled = !biometricEnabled) }

    private fun updateSettings(transform: UserSettingsEntity.() -> UserSettingsEntity) {
        val current = _settings.value ?: return
        viewModelScope.launch {
            userRepository.upsertSettings(current.transform())
        }
    }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository) as T
        }
    }
}
