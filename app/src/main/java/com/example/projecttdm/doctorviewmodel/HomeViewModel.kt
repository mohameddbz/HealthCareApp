package com.example.projecttdm.doctorviewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.NextAppointementResponse
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.data.model.User
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.patient.PatientRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime




class DoctorHomeViewModel : ViewModel() {
    private val userRepository = RepositoryHolder.UserRepository
    private val appointementRepo  = RepositoryHolder.appointmentRepository


    private val _currentUser = MutableStateFlow<UiState<User>>(UiState.Init)
    val currentUser: StateFlow<UiState<User>> = _currentUser.asStateFlow()

    private val _nextAppointemnt = MutableStateFlow<UiState<NextAppointementResponse>>(UiState.Init)
    val nextAppointment: StateFlow<UiState<NextAppointementResponse>> = _nextAppointemnt.asStateFlow()

    fun getNextAppoitmentForDoctor () {
        viewModelScope.launch {
            _nextAppointemnt.value = UiState.Loading
            try {
                val response = appointementRepo.getNextAppointmentForDoctor()
                _nextAppointemnt.value = UiState.Success(response)
            }catch (
               e: Exception
            ){
                _nextAppointemnt.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

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

    init {
        getCurrentUser()
        getNextAppoitmentForDoctor()
    }

}