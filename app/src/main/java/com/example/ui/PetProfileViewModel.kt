package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PetProfile
import com.example.data.PetProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetProfileViewModel(private val repository: PetProfileRepository) : ViewModel() {

    val uiState: StateFlow<List<PetProfile>> = repository.allProfiles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addProfile(name: String, species: String, breed: String, age: String, photoUri: String? = null) {
        viewModelScope.launch {
            repository.insert(PetProfile(name = name, species = species, breed = breed, age = age, photoUri = photoUri))
        }
    }

    fun deleteProfile(profile: PetProfile) {
        viewModelScope.launch {
            repository.delete(profile)
        }
    }
}

class PetProfileViewModelFactory(private val repository: PetProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
