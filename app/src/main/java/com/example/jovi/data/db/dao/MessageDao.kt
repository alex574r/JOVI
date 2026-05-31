package com.example.jovi.data.db.dao

import androidx.room.*
import com.example.jovi.data.db.entity.ConversationEntity
import com.example.jovi.data.db.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessages(conversationId: Long): Flow<List<MessageEntity>>

    @Query("SELECT * FROM conversations ORDER BY lastMessageAt DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE userId1 = :userId OR userId2 = :userId ORDER BY lastMessageAt DESC")
    fun getConversationsForUser(userId: Long): Flow<List<ConversationEntity>>

    @Query("UPDATE conversations SET lastMessage = :msg, lastMessageAt = :ts WHERE id = :convId")
    suspend fun updateLastMessage(convId: Long, msg: String, ts: Long)

    @Query("UPDATE conversations SET unreadCount = 0 WHERE id = :convId")
    suspend fun resetUnreadCount(convId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllConversations(conversations: List<ConversationEntity>)

    @Update
    suspend fun updateConversation(conversation: ConversationEntity)

    @Query("UPDATE messages SET isRead = 1 WHERE conversationId = :conversationId")
    suspend fun markAllRead(conversationId: Long)

    @Query("SELECT COUNT(*) FROM conversations")
    suspend fun countConversations(): Int
}
