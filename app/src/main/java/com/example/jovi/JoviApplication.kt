package com.example.jovi

import android.app.Application
import com.example.jovi.data.db.JoviDatabase
import com.example.jovi.data.repository.MessageRepository
import com.example.jovi.data.repository.NotificationRepository
import com.example.jovi.data.repository.PostRepository
import com.example.jovi.data.repository.UserRepository

class JoviApplication : Application() {

    val database by lazy { JoviDatabase.getInstance(this) }

    val userRepository by lazy {
        UserRepository(database.userDao(), database.settingsDao())
    }
    val postRepository by lazy {
        PostRepository(database.postDao())
    }
    val messageRepository by lazy {
        MessageRepository(database.messageDao())
    }
    val notificationRepository by lazy {
        NotificationRepository(database.notificationDao())
    }
}
