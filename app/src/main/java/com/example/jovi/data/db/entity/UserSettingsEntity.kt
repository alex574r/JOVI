package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val userId: Long,
    val notificationsEnabled: Boolean = true,
    val matchNotifications: Boolean = true,
    val messageNotifications: Boolean = true,
    val darkMode: Boolean = false,
    val biometricEnabled: Boolean = false,
    val language: String = "es",
    val profileVisibility: String = "public",
)
