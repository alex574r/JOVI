package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageType { TEXT, FILE, INTERVIEW_CARD, SYSTEM }
enum class MessageStatus { SENT, DELIVERED, READ }

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: Long,
    val senderId: Long,
    val senderName: String,
    val content: String,
    val type: MessageType = MessageType.TEXT,
    val status: MessageStatus = MessageStatus.SENT,
    val isRead: Boolean = false,
    val fileName: String? = null,
    val fileSize: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
)
