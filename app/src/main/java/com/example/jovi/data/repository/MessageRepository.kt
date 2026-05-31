package com.example.jovi.data.repository

import com.example.jovi.data.db.dao.MessageDao
import com.example.jovi.data.db.entity.ConversationEntity
import com.example.jovi.data.db.entity.MessageEntity
import com.example.jovi.data.db.entity.MessageType
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {
    fun getAllConversations(): Flow<List<ConversationEntity>> = messageDao.getAllConversations()
    fun getConversationsForUser(userId: Long): Flow<List<ConversationEntity>> = messageDao.getConversationsForUser(userId)
    fun getMessages(conversationId: Long): Flow<List<MessageEntity>> = messageDao.getMessages(conversationId)

    suspend fun sendMessage(message: MessageEntity): Long {
        val id = messageDao.insertMessage(message)
        val preview = if (message.type == MessageType.FILE) "📎 ${message.fileName ?: message.content}" else message.content
        messageDao.updateLastMessage(message.conversationId, preview, message.timestamp)
        return id
    }

    suspend fun sendDocument(conversationId: Long, senderId: Long, senderName: String, fileName: String, fileSize: String): Long {
        val msg = MessageEntity(
            conversationId = conversationId,
            senderId = senderId,
            senderName = senderName,
            content = fileName,
            type = MessageType.FILE,
            fileName = fileName,
            fileSize = fileSize,
        )
        val id = messageDao.insertMessage(msg)
        messageDao.updateLastMessage(conversationId, "📎 $fileName", msg.timestamp)
        return id
    }

    suspend fun markAllRead(conversationId: Long) {
        messageDao.markAllRead(conversationId)
        messageDao.resetUnreadCount(conversationId)
    }
}
