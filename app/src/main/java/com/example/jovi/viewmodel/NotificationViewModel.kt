package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.NotificationEntity
import com.example.jovi.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val repo: NotificationRepository) : ViewModel() {
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    init {
        viewModelScope.launch { repo.getAllNotifications().collect { _notifications.value = it } }
        viewModelScope.launch { repo.getUnreadCount().collect { _unreadCount.value = it } }
    }

    fun markRead(id: Long) { viewModelScope.launch { repo.markRead(id) } }
    fun markAllRead() { viewModelScope.launch { repo.markAllRead() } }

    class Factory(private val repository: NotificationRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return NotificationViewModel(repository) as T
        }
    }
}
