package com.example.projecttdm.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.FavoriteDoctorResponse
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.FavoriteRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class FavoriteDoctorsViewModel : ViewModel() {

    // Initialize repository directly from RepositoryHolder
    @RequiresApi(Build.VERSION_CODES.O)
    private val favoriteRepository: FavoriteRepository = RepositoryHolder.favoriteRepository

    // LiveData for favorite doctors list
    private val _favoriteDoctors = MutableLiveData<List<FavoriteDoctorResponse>>(emptyList())
    val favoriteDoctors: LiveData<List<FavoriteDoctorResponse>> = _favoriteDoctors

    // Loading state
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Error state
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        // Load favorites from API when ViewModel is created
        refreshFavorites()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshFavorites() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteDoctors().collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        _isLoading.value = true
                        _error.value = null
                    }
                    is UiState.Success -> {
                        _isLoading.value = false
                        _favoriteDoctors.value = state.data
                    }
                    is UiState.Error -> {
                        _isLoading.value = false
                        _error.value = state.message
                        _favoriteDoctors.value = emptyList()
                    }
                    UiState.Init -> {}
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleFavorite(doctorId: Int) {
        // Check if the doctor is already in favorites
        val currentFavorites = _favoriteDoctors.value ?: emptyList()
        val isCurrentlyFavorite = currentFavorites.any { it.doctor_id == doctorId }

        viewModelScope.launch {
            if (isCurrentlyFavorite) {
                // Remove from favorites
                favoriteRepository.removeFavoriteDoctor(doctorId).collect { state ->
                    handleToggleState(state, doctorId)
                }
            } else {
                // Add to favorites
                favoriteRepository.addFavoriteDoctor(doctorId).collect { state ->
                    handleToggleState(state, doctorId)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleToggleState(state: UiState<Boolean>, doctorId: Int) {
        when (state) {
            is UiState.Loading -> {
                _isLoading.value = true
            }
            is UiState.Success -> {
                _isLoading.value = false
                // Refresh the favorites list after toggling
                refreshFavorites()
            }
            is UiState.Error -> {
                _isLoading.value = false
                _error.value = state.message
            }
            UiState.Init -> {}
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkFavoriteStatus(doctorId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            favoriteRepository.checkFavoriteDoctor(doctorId).collect { state ->
                when (state) {
                    is UiState.Success -> {
                        callback(state.data)
                    }
                    is UiState.Error -> {
                        _error.value = state.message
                        callback(false)
                    }
                    is UiState.Loading -> {
                        // Do nothing while loading
                    }
                    UiState.Init -> {}
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}