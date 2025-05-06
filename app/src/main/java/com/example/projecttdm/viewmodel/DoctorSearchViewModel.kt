package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.data.repository.SpecialtyRepository
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class DoctorSearchViewModel : ViewModel() {

    private val doctorRepository = RepositoryHolder.doctorRepository
    private val specialtyRepository = RepositoryHolder.specialtyRepository

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

    private val _searchResults = MutableStateFlow<UiState<List<Doctor>>>(UiState.Init)
    val searchResults: StateFlow<UiState<List<Doctor>>> = _searchResults.asStateFlow()
    private var searchJob: Job? = null

    init {
        fetchDoctors()
        fetchSpecialties()
        observeFiltering()
    }

    private fun fetchDoctors() {
        // Simulate fetching data, replace with actual repository call
        viewModelScope.launch {
            _allDoctors.value = doctorRepository.getDoctors()  // fetch doctors
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


    /**
     * Search for doctors by query with debounce
     */
    fun searchDoctors() {
        // Get the current search query
        val query = _searchQuery.value

        // Cancel previous search job if it's still running
        searchJob?.cancel()

        if (query.trim().isEmpty()) {
            _searchResults.value = UiState.Success(emptyList())
            return
        }

        // Start a new search with debounce
        searchJob = viewModelScope.launch {
            // Set loading state immediately
            _searchResults.value = UiState.Loading

            // Add a small delay to debounce inputs
            delay(300)

            // Collect search results
            try {
                doctorRepository.searchDoctors(query).collect { state ->
                    _searchResults.value = state
                }
            } catch (e: Exception) {
                _searchResults.value = UiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    /**
     * Overloaded search function to directly provide a query
     * @param query The search query
     */
    fun searchDoctors(query: String) {
        setSearchQuery(query)
        searchDoctors()
    }

    /**
     * Reset search results
     */
    fun resetSearch() {
        searchJob?.cancel()
        _searchResults.value = UiState.Init
        _searchQuery.value = ""
    }
}