package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Review
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DoctorProfileViewModel : ViewModel() {

    private val repository = RepositoryHolder.doctorRepository
    private val repositoryReview = RepositoryHolder.reviewRepository

    private val _doctorState = MutableStateFlow<UiState<Doctor>>(UiState.Init)
    val doctorState: StateFlow<UiState<Doctor>> = _doctorState

    private val _reviewsState = MutableStateFlow<UiState<List<Review>>>(UiState.Loading)
    val reviewsState: StateFlow<UiState<List<Review>>> = _reviewsState.asStateFlow()


    fun loadDoctorDetails(doctorId: String) {
        viewModelScope.launch {
            _doctorState.value = UiState.Loading
            try {
                repository.getDetailDoctorById(doctorId).collect { state ->
                    _doctorState.value = state
                }
            } catch (e: Exception) {
                _doctorState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadDoctorReviews(doctorId: String) {
        viewModelScope.launch {
            _reviewsState.value = UiState.Loading
            try {
                repositoryReview.getReviewOfDoctor(doctorId).collect { state ->
                    _reviewsState.value = state
                }
            } catch (e: Exception) {
                _reviewsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadDoctorProfile(doctorId: String) {
        loadDoctorDetails(doctorId)
        loadDoctorReviews(doctorId)
    }
}
