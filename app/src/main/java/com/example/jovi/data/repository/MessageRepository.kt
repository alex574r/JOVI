package com.example.jovi.data.repository

import com.example.jovi.data.db.dao.MessageDao
import com.example.jovi.data.db.entity.ConversationEntity
import com.example.jovi.data.db.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {
    fun getAllConversations(): Flow<List<ConversationEntity>> = messageDao.getAllConversations()
    fun getMessages(conversationId: Long): Flow<List<MessageEntity>> = messageDao.getMessages(conversationId)
    suspend fun sendMessage(message: MessageEntity): Long = messageDao.insertMessage(message)
    suspend fun markAllRead(conversationId: Long) = messageDao.markAllRead(conversationId)
}
