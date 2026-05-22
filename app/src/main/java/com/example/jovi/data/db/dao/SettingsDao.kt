package com.example.jovi.data.db.dao

import androidx.room.*
import com.example.jovi.data.db.entity.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM user_settings WHERE userId = :userId")
    fun getSettings(userId: Long): Flow<UserSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(settings: UserSettingsEntity)
}
