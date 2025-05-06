package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.data.model.User
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {

    private val userRepository = RepositoryHolder.UserRepository
    private val specialtyRepository = RepositoryHolder.specialtyRepository
    private val doctorRepository = RepositoryHolder.doctorRepository

    private val _currentUser = MutableStateFlow<UiState<User>>(UiState.Init)
    val currentUser: StateFlow<UiState<User>> = _currentUser.asStateFlow()

    private val _specialtyUiState = MutableStateFlow<UiState<List<Specialty>>>(UiState.Init)
    val specialtyUiState: StateFlow<UiState<List<Specialty>>> = _specialtyUiState.asStateFlow()

    private val _doctorsState = MutableStateFlow<UiState<List<Doctor>>>(UiState.Loading)
    val doctorsState: StateFlow<UiState<List<Doctor>>> = _doctorsState.asStateFlow()

    init {
        getCurrentUser()
        loadSpecialties()
        loadDoctors()
    }

    /**
     * Récupère les informations de l'utilisateur courant
     */
    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = UiState.Loading
            try {
                val response = userRepository.getCurrentUser()
                _currentUser.value = UiState.Success(response)
            } catch (e: Exception) {
                _currentUser.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    /**
     * Charge toutes les spécialités disponibles depuis le repository
     */
    private fun loadSpecialties() {
        viewModelScope.launch {
            _specialtyUiState.value = UiState.Loading
            try {
                specialtyRepository.getAllSpecialties().collectLatest { state ->
                    _specialtyUiState.value = state
                    if (state is UiState.Success) {
                    } else if (state is UiState.Error) {
                    }
                }
            } catch (e: Exception) {
                _specialtyUiState.value = UiState.Error(e.message ?: "Erreur inconnue")
                println("Exception while loading specialties: ${e.message}")
            }
        }
    }


    private fun loadDoctors() {
        viewModelScope.launch {
            doctorRepository.getTopDoctors()
                .catch { e ->
                    _doctorsState.value = UiState.Error(e.message ?: "An unexpected error occurred")
                }
                .collect { state ->
                    _doctorsState.value = state
                }
        }
    }





    /**
     * Retourne l'icône associée à une spécialité donnée
     */
    fun getIconForSpecialty(specialtyName: String): Int {
        return when (specialtyName.lowercase()) {
            "general practitioner" -> com.example.projecttdm.R.drawable.dentistry
            "dentist", "dentistry" -> com.example.projecttdm.R.drawable.dentistry
            "ophthalmology" -> com.example.projecttdm.R.drawable.ophthalmology
            "nutrition" -> com.example.projecttdm.R.drawable.nutrition
            "neurologists" -> com.example.projecttdm.R.drawable.neurology
            "pediatric", "pediatrics" -> com.example.projecttdm.R.drawable.pediatrics
            "radiology" -> com.example.projecttdm.R.drawable.radiology
            "immunologists" -> com.example.projecttdm.R.drawable.neurology
            "cardiologists" -> com.example.projecttdm.R.drawable.pediatrics
            "allergists" -> com.example.projecttdm.R.drawable.ophthalmology
            else -> com.example.projecttdm.R.drawable.more
        }
    }





}