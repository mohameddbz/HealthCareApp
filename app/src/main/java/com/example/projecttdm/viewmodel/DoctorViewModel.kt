package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.SpecialtyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DoctorViewModel : ViewModel() {
    private val doctorRepository = DoctorRepository()
    private val specialtyRepository = SpecialtyRepository()

    // All doctors
    private val allDoctors = doctorRepository.getTopDoctors()

    // All specialties including "All" option
    private val allSpecialtiesWithAll = specialtyRepository.getAllSpecialtiesWithAll()

    // MutableStateFlow for doctors that will be displayed
    private val _doctors = MutableStateFlow(allDoctors)
    val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

    // Get the "All" specialty
    private val allSpecialty = specialtyRepository.getAllSpecialty()

    // Initialize with "All" specialty selected
    private val _selectedSpecialty = MutableStateFlow<Specialty>(allSpecialty)
    val selectedSpecialty: StateFlow<Specialty> = _selectedSpecialty.asStateFlow()

    // MutableStateFlow for all specialties to expose to UI
    private val _specialties = MutableStateFlow(allSpecialtiesWithAll)
    val specialties: StateFlow<List<Specialty>> = _specialties.asStateFlow()

    init {
        // Initialize with all doctors shown
        filterDoctors()
    }

    // Set selected specialty and filter doctors
    fun setSpecialty(specialty: Specialty) {
        _selectedSpecialty.value = specialty
        filterDoctors()
    }

    // Filter doctors based on selected specialty
    private fun filterDoctors() {
        val selectedSpec = _selectedSpecialty.value
        if (selectedSpec.id == "all") {
            _doctors.value = allDoctors
        } else {
            _doctors.value = allDoctors.filter { it.specialty.id == selectedSpec.id }
        }
    }

    // Toggle favorite status for a doctor
    fun toggleFavorite(doctorId: String) {
        // Implement favorite toggling functionality
        // This is a placeholder and would require proper implementation
        // with a repository that can update favorite status
    }

    // Get specialty by ID
    fun getSpecialtyById(id: String): Specialty? {
        return specialtyRepository.getSpecialtyById(id)
    }
}