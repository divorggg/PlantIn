package com.example.plantin.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    val description: String?,
    var createdAt: Date
)