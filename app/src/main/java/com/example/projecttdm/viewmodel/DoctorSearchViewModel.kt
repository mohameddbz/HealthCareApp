package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.SpecialtyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class DoctorSearchViewModel : ViewModel() {

    private val doctorRepository = DoctorRepository()
    private val specialtyRepository = SpecialtyRepository()

    // Flow to fetch doctors and specialties
    private val _allDoctors = MutableStateFlow<List<Doctor>>(emptyList())
    val allDoctors: StateFlow<List<Doctor>> = _allDoctors.asStateFlow()

    private val _allSpecialties = MutableStateFlow<List<Specialty>>(emptyList())
    val allSpecialties: StateFlow<List<Specialty>> = _allSpecialties.asStateFlow()

    private val _selectedSpecialty = MutableStateFlow(Specialty(id = "all", name = "All"))
    val selectedSpecialty = _selectedSpecialty.asStateFlow()

    private val _selectedRating = MutableStateFlow("All")
    val selectedRating = _selectedRating.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filteredDoctors = MutableStateFlow<List<Doctor>>(emptyList())
    val filteredDoctors = _filteredDoctors.asStateFlow()

    init {
        fetchDoctors()
        fetchSpecialties()
        observeFiltering()
    }

    private fun fetchDoctors() {
        // Simulate fetching data, replace with actual repository call
        viewModelScope.launch {
            _allDoctors.value = doctorRepository.getTopDoctors()  // fetch doctors
        }
    }

    private fun fetchSpecialties() {
        // Simulate fetching data, replace with actual repository call
        viewModelScope.launch {
            _allSpecialties.value = specialtyRepository.getAllSpecialtiesWithAll() // fetch specialties
        }
    }

    fun setSpecialty(specialty: Specialty) {
        _selectedSpecialty.value = specialty
    }

    fun setRating(rating: String) {
        _selectedRating.value = rating
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun observeFiltering() {
        viewModelScope.launch {
            combine(_selectedSpecialty, _searchQuery, _selectedRating, _allDoctors) { specialty, query, rating, doctors ->
                filterDoctors(specialty.id, query, rating, doctors)
            }.collect { filtered ->
                _filteredDoctors.value = filtered
            }
        }
    }


    private fun filterDoctors(specialtyId: String, query: String, rating: String, doctors: List<Doctor>): List<Doctor> {
        return doctors.filter { doctor ->
            val matchesSpecialty = specialtyId == "all" || doctor.specialty.id == specialtyId
            val matchesQuery = doctor.name.contains(query, ignoreCase = true)
            val matchesRating = rating == "All" || doctor.rating.toInt() >= rating.toInt()
            matchesSpecialty && matchesQuery && matchesRating
        }
    }

}
