package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DoctorListViewModel(
    private val doctorSearchViewModel: DoctorSearchViewModel
) : ViewModel() {
    private val doctorRepository = RepositoryHolder.doctorRepository
    private val allDoctors: List<Doctor> = doctorRepository.getDoctors()

    private val _doctors = MutableStateFlow<List<Doctor>>(allDoctors)
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    init {
        observeFiltering()
    }

    private fun observeFiltering() {
        viewModelScope.launch {
            combine(
                doctorSearchViewModel.selectedSpecialty,
                doctorSearchViewModel.searchQuery
            ) { specialty, query ->
                filterDoctorsList(specialty.id, query)
            }.collect { filteredList ->
                _doctors.value = filteredList
            }
        }
    }

    private fun filterDoctorsList(specialtyId: String, query: String): List<Doctor> {
        return allDoctors.filter { doctor ->
            val matchesSpecialty = specialtyId == "all" || doctor.specialty.id == specialtyId
            val matchesQuery = doctor.name.contains(query, ignoreCase = true)
            matchesSpecialty && matchesQuery
        }
    }

    fun toggleFavorite(doctorId: String) {
        // implement favorite logic if needed
    }
}
