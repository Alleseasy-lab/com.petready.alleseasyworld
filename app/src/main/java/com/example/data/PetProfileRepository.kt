package com.example.data

import kotlinx.coroutines.flow.Flow

class PetProfileRepository(private val petProfileDao: PetProfileDao) {
    val allProfiles: Flow<List<PetProfile>> = petProfileDao.getAllProfiles()

    suspend fun insert(profile: PetProfile) {
        petProfileDao.insertProfile(profile)
    }

    suspend fun delete(profile: PetProfile) {
        petProfileDao.deleteProfile(profile)
    }
}
