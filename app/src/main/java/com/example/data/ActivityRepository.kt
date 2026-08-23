package com.example.data

import kotlinx.coroutines.flow.Flow

class ActivityRepository(private val activityEntryDao: ActivityEntryDao) {
    val allActivities: Flow<List<ActivityEntry>> = activityEntryDao.getAllActivities()

    suspend fun insert(entry: ActivityEntry) {
        activityEntryDao.insertActivity(entry)
    }

    suspend fun delete(entry: ActivityEntry) {
        activityEntryDao.deleteActivity(entry)
    }
}
