package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId1: Long,
    val userId2: Long,
    val user1Name: String,
    val user2Name: String,
    val user1Initials: String,
    val user2Initials: String,
    val lastMessage: String = "",
    val lastMessageAt: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
)
