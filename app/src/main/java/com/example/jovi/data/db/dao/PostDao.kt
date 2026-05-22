package com.example.jovi.data.db.dao

import androidx.room.*
import com.example.jovi.data.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE type = 'THREAD' ORDER BY timestamp DESC")
    fun getThreads(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE type = 'REEL' ORDER BY timestamp DESC")
    fun getReels(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE authorId = :userId ORDER BY timestamp DESC")
    fun getPostsByUser(userId: Long): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getPostById(id: Long): Flow<PostEntity?>

    @Query("SELECT * FROM posts WHERE content LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchPosts(query: String): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Update
    suspend fun update(post: PostEntity)

    @Query("UPDATE posts SET likeCount = likeCount + 1, isLiked = 1 WHERE id = :postId")
    suspend fun likePost(postId: Long)

    @Query("UPDATE posts SET likeCount = likeCount - 1, isLiked = 0 WHERE id = :postId")
    suspend fun unlikePost(postId: Long)

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun count(): Int
}
