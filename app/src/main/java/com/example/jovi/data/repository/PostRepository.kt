package com.example.jovi.data.repository

import com.example.jovi.data.db.dao.PostDao
import com.example.jovi.data.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

class PostRepository(private val postDao: PostDao) {
    fun getAllPosts(): Flow<List<PostEntity>> = postDao.getAllPosts()
    fun getThreads(): Flow<List<PostEntity>> = postDao.getThreads()
    fun getReels(): Flow<List<PostEntity>> = postDao.getReels()
    fun getPostsByUser(userId: Long): Flow<List<PostEntity>> = postDao.getPostsByUser(userId)
    fun getPostById(id: Long): Flow<PostEntity?> = postDao.getPostById(id)
    fun searchPosts(query: String): Flow<List<PostEntity>> = postDao.searchPosts(query)
    suspend fun insert(post: PostEntity): Long = postDao.insert(post)
    suspend fun likePost(postId: Long) = postDao.likePost(postId)
    suspend fun unlikePost(postId: Long) = postDao.unlikePost(postId)
}
