package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetProfileDao {
    @Query("SELECT * FROM pet_profiles ORDER BY id DESC")
    fun getAllProfiles(): Flow<List<PetProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: PetProfile)

    @Delete
    suspend fun deleteProfile(profile: PetProfile)
}
