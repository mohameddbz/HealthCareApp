package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Review
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.data.repository.ReviewRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel
class DoctorProfileViewModel : ViewModel() {
    val doctorRepository = RepositoryHolder.doctorRepository
    val reviewRepository = ReviewRepository()
    private val _doctorData = MutableStateFlow<Doctor?>(null)
    val doctorData: StateFlow<Doctor?> = _doctorData

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadDoctorProfile(doctorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Simulation d'un appel API
                delay(500)
              //  _doctorData.value = doctorRepository.getDetailDoctorById("dr-jenny-watson")

                _reviews.value = reviewRepository.getReviewOfDoctor(_doctorData.value!!.id)
               _isLoading.value = false;
                _isFavorite.value = false
            } catch (e: Exception) {
                // Gérer l'erreur
                _isLoading.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
    }

    fun bookAppointment(doctorId: String) {
        // Implémenter la logique pour prendre rendez-vous
    }
}
