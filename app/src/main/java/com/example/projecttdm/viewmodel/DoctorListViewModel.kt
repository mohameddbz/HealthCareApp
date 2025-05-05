//package com.example.projecttdm.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.projecttdm.data.model.Doctor
//import com.example.projecttdm.data.repository.DoctorRepository
//import com.example.projecttdm.state.UiState
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.launch
//import com.example.projecttdm.data.model.Review
//import com.example.projecttdm.data.repository.ReviewRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.async
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.supervisorScope
//import kotlinx.coroutines.withContext
//
//class DoctorListViewModel(
//    private val doctorSearchViewModel: DoctorSearchViewModel,
//    private val doctorRepository: DoctorRepository,
//    private val reviewRepository: ReviewRepository
//) : ViewModel() {
//
//    // State for all doctors
//    private val _doctorsState = MutableStateFlow<UiState<List<Doctor>>>(UiState.Loading)
//    val doctorsState: StateFlow<UiState<List<Doctor>>> = _doctorsState.asStateFlow()
//
//    // State for a single doctor
//    private val _doctorState = MutableStateFlow<UiState<Doctor>>(UiState.Loading)
//    val doctorState: StateFlow<UiState<Doctor>> = _doctorState.asStateFlow()
//
//    private val _filteredDoctors = MutableStateFlow<List<Doctor>>(emptyList())
//    val filteredDoctors: StateFlow<List<Doctor>> = _filteredDoctors.asStateFlow()
//
//    private val _reviewsState = MutableStateFlow<UiState<List<Review>>>(UiState.Loading)
//    val reviewsState: StateFlow<UiState<List<Review>>> = _reviewsState.asStateFlow()
//
//    // Initialize the loading of doctors list
//    init {
//        loadDoctors()
//    }
//
//    // Function to load all doctors
//    private fun loadDoctors() {
//        viewModelScope.launch {
//            try {
//                doctorRepository.getTopDoctors()
//                    .catch { e ->
//                        _doctorsState.value = UiState.Error(e.message ?: "An unexpected error occurred")
//                    }
//                    .collect { state ->
//                        _doctorsState.value = state
//                        if (state is UiState.Success) {
//                            observeFiltering(state.data)
//                        }
//                    }
//            } catch (e: Exception) {
//                _doctorsState.value = UiState.Error(e.message ?: "An unexpected error occurred")
//            }
//        }
//    }
//
//    private fun observeFiltering(doctorsList: List<Doctor>) {
//        viewModelScope.launch {
//            combine(
//                doctorSearchViewModel.selectedSpecialty,
//                doctorSearchViewModel.searchQuery,
//                doctorSearchViewModel.selectedRating
//            ) { specialty, query, rating ->
//                doctorsList.filter { doctor ->
//                    val matchesSpecialty = specialty.id == "all" || doctor.specialty.id == specialty.id
//                    val matchesQuery = doctor.name.contains(query, ignoreCase = true)
//                    val matchesRating = rating == "All" || doctor.rating.toInt() >= rating.toInt()
//                    matchesSpecialty && matchesQuery && matchesRating
//                }
//            }.collect { filteredList ->
//                _filteredDoctors.value = filteredList
//            }
//        }
//    }
//
//    // Function to get details of a single doctor by ID
//    // In your DoctorListViewModel
//    fun getDoctorById(doctorId: String) {
//        viewModelScope.launch {
//            // Reset states to Loading
//            _doctorState.value = UiState.Loading
//            _reviewsState.value = UiState.Loading
//
//            // Use coroutineScope to run both requests truly concurrently
//            try {
//                coroutineScope {
//                    // Launch both requests simultaneously with async
//                    val doctorDeferred = async(Dispatchers.IO) {
//                        doctorRepository.getDetailDoctorById(doctorId)
//                    }
//
//                    val reviewsDeferred = async(Dispatchers.IO) {
//                        reviewRepository.getReviewOfDoctor(doctorId)
//                    }
//
//                    // Collect both flows in parallel
//                    launch {
//                        doctorDeferred.await().collect { state ->
//                            _doctorState.value = state
//                        }
//                    }
//
//                    launch {
//                        reviewsDeferred.await().collect { state ->
//                            _reviewsState.value = state
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                // Handle any exceptions from the coroutineScope
//                _doctorState.value = UiState.Error("Failed to load data")
//                _reviewsState.value = UiState.Error("Failed to load data")
//            }
//        }
//    }
//
//    // Function to refresh doctor list
//    fun refreshDoctors() {
//        loadDoctors()
//    }
//}

package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.ReviewRepository
import com.example.projecttdm.data.repository.SpecialtyRepository
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.example.projecttdm.data.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class DoctorListViewModel(
    private val doctorRepository: DoctorRepository,
    private val reviewRepository: ReviewRepository,
    private val specialtyRepository: SpecialtyRepository
) : ViewModel() {

    // State for all doctors
    private val _doctorsState = MutableStateFlow<UiState<List<Doctor>>>(UiState.Loading)
    val doctorsState: StateFlow<UiState<List<Doctor>>> = _doctorsState.asStateFlow()

    // State for a single doctor
    private val _doctorState = MutableStateFlow<UiState<Doctor>>(UiState.Loading)
    val doctorState: StateFlow<UiState<Doctor>> = _doctorState.asStateFlow()

    // State for reviews
    private val _reviewsState = MutableStateFlow<UiState<List<Review>>>(UiState.Loading)
    val reviewsState: StateFlow<UiState<List<Review>>> = _reviewsState.asStateFlow()

    // Search and filter states
    private val _allSpecialties = MutableStateFlow<List<Specialty>>(emptyList())
    val allSpecialties: StateFlow<List<Specialty>> = _allSpecialties.asStateFlow()

    private val _selectedSpecialty = MutableStateFlow(Specialty(id = "all", name = "All"))
    val selectedSpecialty: StateFlow<Specialty> = _selectedSpecialty.asStateFlow()

    private val _selectedRating = MutableStateFlow("All")
    val selectedRating: StateFlow<String> = _selectedRating.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredDoctors = MutableStateFlow<List<Doctor>>(emptyList())
    val filteredDoctors: StateFlow<List<Doctor>> = _filteredDoctors.asStateFlow()

    // Initialize the loading of doctors list and specialties
    init {
        loadDoctors()
        loadSpecialties()
    }

    // Function to load all doctors
    private fun loadDoctors() {
        viewModelScope.launch {
            try {
                doctorRepository.getTopDoctors()
                    .catch { e ->
                        _doctorsState.value = UiState.Error(e.message ?: "An unexpected error occurred")
                    }
                    .collect { state ->
                        _doctorsState.value = state
                        if (state is UiState.Success) {
                            setupFiltering(state.data)
                        }
                    }
            } catch (e: Exception) {
                _doctorsState.value = UiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    // Function to load all specialties
    private fun loadSpecialties() {
        viewModelScope.launch {
            try {
                val specialties = specialtyRepository.getAllSpecialtiesWithAll()
                _allSpecialties.value = specialties
            } catch (e: Exception) {
                // Handle error loading specialties
            }
        }
    }

    // Setup filtering for doctors based on specialty, query and rating
    private fun setupFiltering(doctorsList: List<Doctor>) {
        viewModelScope.launch {
            combine(
                _selectedSpecialty,
                _searchQuery,
                _selectedRating
            ) { specialty, query, rating ->
                filterDoctors(doctorsList, specialty.id, query, rating)
            }.collect { filteredList ->
                _filteredDoctors.value = filteredList
            }
        }
    }

    // Filter doctors based on criteria
    private fun filterDoctors(
        doctors: List<Doctor>,
        specialtyId: String,
        query: String,
        rating: String
    ): List<Doctor> {
        return doctors.filter { doctor ->
            val matchesSpecialty = specialtyId == "all" || doctor.specialty.id == specialtyId
            val matchesQuery = doctor.name.contains(query, ignoreCase = true)
            val matchesRating = rating == "All" || doctor.rating.toInt() >= rating.toInt()
            matchesSpecialty && matchesQuery && matchesRating
        }
    }

    // Function to get details of a single doctor by ID
    fun getDoctorById(doctorId: String) {
        viewModelScope.launch {
            // Reset states to Loading
            _doctorState.value = UiState.Loading
            _reviewsState.value = UiState.Loading

            // Use coroutineScope to run both requests truly concurrently
            try {
                coroutineScope {
                    // Launch both requests simultaneously with async
                    val doctorDeferred = async(Dispatchers.IO) {
                        doctorRepository.getDetailDoctorById(doctorId)
                    }

                    val reviewsDeferred = async(Dispatchers.IO) {
                        reviewRepository.getReviewOfDoctor(doctorId)
                    }

                    // Collect both flows in parallel
                    launch {
                        doctorDeferred.await().collect { state ->
                            _doctorState.value = state
                        }
                    }

                    launch {
                        reviewsDeferred.await().collect { state ->
                            _reviewsState.value = state
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle any exceptions from the coroutineScope
                _doctorState.value = UiState.Error("Failed to load data")
                _reviewsState.value = UiState.Error("Failed to load data")
            }
        }
    }

    // Search and filter functions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSpecialty(specialty: Specialty) {
        _selectedSpecialty.value = specialty
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
}