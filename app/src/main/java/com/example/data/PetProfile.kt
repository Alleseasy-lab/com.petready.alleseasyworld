package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_profiles")
data class PetProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val species: String,
    val breed: String,
    val age: String,
    val photoUri: String? = null
)
