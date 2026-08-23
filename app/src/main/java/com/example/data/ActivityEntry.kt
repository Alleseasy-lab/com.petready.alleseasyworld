package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_entries")
data class ActivityEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
