package com.example.plantin.classification.history.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DetectionHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: DetectionHistory)

    @Query("SELECT * FROM detection_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<DetectionHistory>>

    @Query("DELETE FROM detection_history")
    suspend fun clear()
}
