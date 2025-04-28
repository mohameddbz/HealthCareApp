package com.example.projecttdm.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.repository.DoctorRepository

class FavoriteDoctorsViewModel : ViewModel() {
    private val repository = DoctorRepository()

    // Default patient ID
    private val patientId = "patient1"

    private val _favoriteDoctors = MutableLiveData<List<Doctor>>()
    val favoriteDoctors: LiveData<List<Doctor>> = _favoriteDoctors

    init {

        refreshFavorites()
    }


    fun refreshFavorites() {

        _favoriteDoctors.value = repository.getFavoriteDoctors(patientId)
    }


    fun refreshSessionFavorites() {
        _favoriteDoctors.value = repository.getSessionFavoriteDoctors()
    }


    fun toggleFavorite(doctorId: String) {
        repository.toggleSessionFavorite(doctorId)
        refreshSessionFavorites()
    }


    fun isFavorite(doctorId: String): Boolean {
        return repository.isSessionFavorite(doctorId)
    }
}