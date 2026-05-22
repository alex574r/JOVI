package com.example.jovi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jovi.data.db.entity.ConversationEntity
import com.example.jovi.data.db.entity.MessageEntity
import com.example.jovi.data.db.entity.MessageType
import com.example.jovi.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    private val _conversations = MutableStateFlow<List<ConversationEntity>>(emptyList())
    val conversations: StateFlow<List<ConversationEntity>> = _conversations.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            messageRepository.getAllConversations().collect { _conversations.value = it }
        }
    }

    fun loadMessages(conversationId: Long) {
        viewModelScope.launch {
            messageRepository.getMessages(conversationId).collect { _messages.value = it }
        }
    }

    fun sendMessage(conversationId: Long, senderId: Long, senderName: String, text: String) {
        viewModelScope.launch {
            messageRepository.sendMessage(
                MessageEntity(
                    conversationId = conversationId,
                    senderId = senderId,
                    senderName = senderName,
                    content = text,
                    type = MessageType.TEXT,
                )
            )
        }
    }

    class Factory(private val repository: MessageRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(repository) as T
        }
    }
}
