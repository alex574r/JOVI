package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AccountType { STUDENT, RECRUITER }

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val displayName: String,
    val email: String,
    val password: String = "",
    val avatarInitials: String,
    val bio: String = "",
    val accountType: AccountType = AccountType.STUDENT,
    val isVerified: Boolean = false,
    val streakDays: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val university: String = "",
    val company: String = "",
    val location: String = "",
    val headline: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
