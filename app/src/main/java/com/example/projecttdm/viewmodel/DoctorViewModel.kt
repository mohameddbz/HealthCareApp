package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DoctorViewModel : ViewModel() {
    private val repository = DoctorRepository()

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val categories = listOf("All", "General", "Dentist", "Nutritionist")

    init {
        fetchDoctors()
    }

    private fun fetchDoctors() {
        _doctors.value = repository.getTopDoctors()
    }

    fun setCategory(category: String) {
        if (category in categories) {
            _selectedCategory.value = category
            // In a real app, you might filter the doctors based on the category
        }
    }

    fun toggleFavorite(doctorId: String) {
        // Implementation for toggling favorite status
    }
}