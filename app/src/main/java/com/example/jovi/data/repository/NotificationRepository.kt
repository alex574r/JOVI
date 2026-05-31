package com.example.jovi.data.repository

import com.example.jovi.data.db.dao.NotificationDao
import com.example.jovi.data.db.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDao: NotificationDao) {
    fun getAllNotifications(): Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    fun getUnreadCount(): Flow<Int> = notificationDao.getUnreadCount()
    fun getNotificationsForUser(userId: Long): Flow<List<NotificationEntity>> = notificationDao.getNotificationsForUser(userId)
    suspend fun insert(notification: NotificationEntity) = notificationDao.insert(notification)
    suspend fun markRead(id: Long) = notificationDao.markRead(id)
    suspend fun markAllRead() = notificationDao.markAllRead()
}
