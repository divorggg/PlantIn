package com.example.plantin.notes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.plantin.classification.history.dao.DetectionHistory
import com.example.plantin.classification.history.dao.DetectionHistoryDao
import com.example.plantin.notes.Notes


@Database(entities = [Notes::class, DetectionHistory::class], version = 2)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val NAME = "Notes_DB"
    }

    abstract fun detectionHistoryDao(): DetectionHistoryDao
    abstract fun getNotesdao(): NotesDao
}
