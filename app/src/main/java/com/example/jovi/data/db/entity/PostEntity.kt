package com.example.jovi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PostType { THREAD, REEL }

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorId: Long,
    val authorName: String,
    val authorInitials: String,
    val authorUsername: String,
    val type: PostType = PostType.THREAD,
    val content: String = "",
    val mediaUrl: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val tags: String = "",
)
