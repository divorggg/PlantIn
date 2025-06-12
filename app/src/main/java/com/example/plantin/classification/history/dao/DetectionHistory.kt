package com.example.plantin.classification.history.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detection_history")
data class DetectionHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val confidence: Float,
    val imageUri: String,
    val timestamp: Long = System.currentTimeMillis()
)
