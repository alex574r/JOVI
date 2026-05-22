package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NotificationType { MATCH, MESSAGE, APPLICATION_UPDATE, LIKE, FOLLOW, SYSTEM }

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val type: NotificationType,
    val title: String,
    val body: String,
    val senderInitials: String = "",
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
)
