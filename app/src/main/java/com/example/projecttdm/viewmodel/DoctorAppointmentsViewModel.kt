package com.example.projecttdm.viewmodel


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentWeekResponse
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DoctorAppointmentsViewModel : ViewModel() {

    private val _appointments = MutableStateFlow<UiState<List<AppointmentWeekResponse>>>(UiState.Loading)
    val appointments: StateFlow<UiState<List<AppointmentWeekResponse>>> = _appointments

    // Accéder au repository via RepositoryHolder
    @RequiresApi(Build.VERSION_CODES.O)
    private val doctorRepository = RepositoryHolder.doctorRepository

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAppointmentsByDate(date: String) {
        viewModelScope.launch {
            doctorRepository.getAppointmentsByDate(date).collect {
                _appointments.value = it
            }
        }
    }
}
