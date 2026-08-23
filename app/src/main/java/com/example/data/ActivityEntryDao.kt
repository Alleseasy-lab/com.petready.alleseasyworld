package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityEntryDao {
    @Query("SELECT * FROM activity_entries ORDER BY timestamp DESC")
    fun getAllActivities(): Flow<List<ActivityEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(entry: ActivityEntry)

    @Delete
    suspend fun deleteActivity(entry: ActivityEntry)
}
