package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.FavoriteEndPoint
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.FavoriteDoctorResponse
import com.example.projecttdm.data.model.FavoriteDoctorsApiResponse
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoriteRepository(private val endpoint: FavoriteEndPoint) {

    /**
     * Get all favorite doctors of the authenticated patient from API
     */
    fun getFavoriteDoctors(): Flow<UiState<List<FavoriteDoctorResponse>>> = flow {
        emit(UiState.Loading)
        try {
            val response = endpoint.getFavoriteDoctors()
            emit(UiState.Success(response.data))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Error fetching favorite doctors"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Add a doctor to favorites
     */
    fun addFavoriteDoctor(doctorId: Int): Flow<UiState<Boolean>> = flow {
        emit(UiState.Loading)
        try {
            endpoint.addFavoriteDoctor(doctorId)
            emit(UiState.Success(true))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Error adding doctor to favorites"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Remove a doctor from favorites
     */
    fun removeFavoriteDoctor(doctorId: Int): Flow<UiState<Boolean>> = flow {
        emit(UiState.Loading)
        try {
            endpoint.removeFavoriteDoctor(doctorId)
            emit(UiState.Success(true))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Error removing doctor from favorites"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Check if a doctor is in favorites
     */
    fun checkFavoriteDoctor(doctorId: Int): Flow<UiState<Boolean>> = flow {
        emit(UiState.Loading)
        try {
            val isFavorite = endpoint.checkFavoriteDoctor(doctorId)
            emit(UiState.Success(isFavorite))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Error checking favorite status"))
        }
    }.flowOn(Dispatchers.IO)
}