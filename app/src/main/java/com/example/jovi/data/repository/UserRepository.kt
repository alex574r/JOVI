package com.example.jovi.data.repository

import com.example.jovi.data.db.dao.UserDao
import com.example.jovi.data.db.dao.SettingsDao
import com.example.jovi.data.db.entity.UserEntity
import com.example.jovi.data.db.entity.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao, private val settingsDao: SettingsDao) {
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    fun getStudents(): Flow<List<UserEntity>> = userDao.getStudents()
    fun getRecruiters(): Flow<List<UserEntity>> = userDao.getRecruiters()
    fun getUserById(id: Long): Flow<UserEntity?> = userDao.getUserById(id)
    fun searchUsers(query: String): Flow<List<UserEntity>> = userDao.searchUsers(query)
    fun getSettings(userId: Long): Flow<UserSettingsEntity?> = settingsDao.getSettings(userId)
    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
    suspend fun insert(user: UserEntity): Long = userDao.insert(user)
    suspend fun update(user: UserEntity) = userDao.update(user)
    suspend fun upsertSettings(settings: UserSettingsEntity) = settingsDao.upsert(settings)
}
