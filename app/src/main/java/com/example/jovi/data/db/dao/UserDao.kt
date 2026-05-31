package com.example.jovi.data.db.dao

import androidx.room.*
import com.example.jovi.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE accountType = 'STUDENT'")
    fun getStudents(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE accountType = 'RECRUITER'")
    fun getRecruiters(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE displayName LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%'")
    fun searchUsers(query: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<UserEntity>)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int
}
