package com.example.projecttdm.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Review
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
@RequiresApi(Build.VERSION_CODES.O)
class DoctorListViewModel() : ViewModel() {

    private val doctorRepository = RepositoryHolder.doctorRepository
    private val specialtyRepository = RepositoryHolder.specialtyRepository

    // State for all doctors
    private val _doctorsState = MutableStateFlow<UiState<List<Doctor>>>(UiState.Loading)
    val doctorsState: StateFlow<UiState<List<Doctor>>> = _doctorsState.asStateFlow()

    // State for a single doctor
    private val _doctorState = MutableStateFlow<UiState<Doctor>>(UiState.Loading)
    val doctorState: StateFlow<UiState<Doctor>> = _doctorState.asStateFlow()

    // Search and filter states
    private val _allSpecialties = MutableStateFlow<List<Specialty>>(emptyList())
    val allSpecialties: StateFlow<List<Specialty>> = _allSpecialties.asStateFlow()

    // Initialize with "All" specialty and ensure it's not null
    private val _selectedSpecialty = MutableStateFlow(Specialty(id = "all", name = "All"))
    val selectedSpecialty: StateFlow<Specialty> = _selectedSpecialty.asStateFlow()

    private val _selectedRating = MutableStateFlow("All")
    val selectedRating: StateFlow<String> = _selectedRating.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredDoctors = MutableStateFlow<List<Doctor>>(emptyList())
    val filteredDoctors: StateFlow<List<Doctor>> = _filteredDoctors.asStateFlow()

    // Keep track of favorites
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()


//    init {
//        loadDoctors()
//        loadSpecialties()
//        setupFiltering()
//    }

    // Function to load all specialties
    fun loadSpecialties() {
        viewModelScope.launch {
            try {
                val specialties = specialtyRepository.getAllSpecialtiesWithAll()
                _allSpecialties.value = specialties

                // Ensure we have the "All" specialty in our list
                if (specialties.isNotEmpty() && !specialties.any { it.id == "all" }) {
                    val updatedList = listOf(Specialty(id = "all", name = "All")) + specialties
                    _allSpecialties.value = updatedList
                }
            } catch (e: Exception) {
                // Handle error loading specialties
                _allSpecialties.value = listOf(Specialty(id = "all", name = "All"))
            }
        }
    }

    fun loadDoctors() {
        viewModelScope.launch {
            try {
                _doctorsState.value = UiState.Loading
                doctorRepository.getTopDoctors()
                    .catch { e ->
                        _doctorsState.value = UiState.Error(e.message ?: "An unexpected error occurred")
                    }
                    .collect { state ->
                        _doctorsState.value = state
                    }
            } catch (e: Exception) {
                _doctorsState.value = UiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // Setup filtering for doctors based on specialty, query and rating
    fun setupFiltering() {
        viewModelScope.launch {
            combine(
                _selectedSpecialty,
                _searchQuery,
                _selectedRating,
                _doctorsState
            ) { specialty, query, rating, doctorsState ->
                if (doctorsState is UiState.Success) {
                    filterDoctors(doctorsState.data, specialty.name, query, rating)
                } else {
                    emptyList()
                }
            }.collect { filteredList ->
                _filteredDoctors.value = filteredList
                // Log the filter results for debugging
                println("Filtered doctors: ${filteredList.size} doctors after applying filters")
                println("Current specialty filter: ${_selectedSpecialty.value.name} (${_selectedSpecialty.value.id})")
            }
        }
    }

    // Filter doctors based on criteria
    private fun filterDoctors(
        doctors: List<Doctor>,
        specialtyname: String,
        query: String,
        rating: String
    ): List<Doctor> {
        return doctors.filter { doctor ->
            val matchesSpecialty = specialtyname == "All" || doctor.specialty.name == specialtyname
            val matchesQuery = doctor.name.contains(query, ignoreCase = true)
            val matchesRating = rating == "All" || doctor.rating.toInt() >= rating.toInt()
            matchesSpecialty && matchesQuery && matchesRating
        }
    }

    // Search and filter functions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSpecialty(specialty: Specialty) {
        _selectedSpecialty.value = specialty
        println("Setting specialty: ${specialty.name} with ID: ${specialty.id}")
        applyFiltering()
    }

    // Function to explicitly apply filtering based on current state
    private fun applyFiltering() {
        val doctorsState = _doctorsState.value
        if (doctorsState is UiState.Success) {
            val filteredList = filterDoctors(
                doctorsState.data,
                _selectedSpecialty.value.name,
                _searchQuery.value,
                _selectedRating.value
            )
            _filteredDoctors.value = filteredList
            println("Explicitly filtered doctors after specialty change: ${filteredList.size} doctors")
        }
    }

    fun setRating(rating: String) {
        _selectedRating.value = rating
    }

    // Function to get specialty by ID
    fun getSpecialtyById(specialtyId: String): Specialty? {
        return _allSpecialties.value.find { it.id == specialtyId }
    }

    // Function to refresh doctor list
    fun refreshDoctors() {
        loadDoctors()
    }

    fun toggleFavorite(doctorId: String) {
        val currentFavorites = _favorites.value.toMutableSet()
        if (currentFavorites.contains(doctorId)) {
            currentFavorites.remove(doctorId)
        } else {
            currentFavorites.add(doctorId)
        }
        _favorites.value = currentFavorites
    }

    fun isFavorite(doctorId: String): Boolean {
        return _favorites.value.contains(doctorId)
    }


}