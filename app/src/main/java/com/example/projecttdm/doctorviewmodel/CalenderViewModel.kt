package com.example.projecttdm.doctorviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.AppointementResponse
import com.example.projecttdm.data.model.NextAppointementsResponse
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class CalendarViewModel : ViewModel() {
    private val appointementRepo  = RepositoryHolder.appointmentRepository

    // StateFlow pour exposer les rendez-vous
    private val _appointments = MutableStateFlow<NextAppointementsResponse?>(null)
    val appointments: StateFlow<NextAppointementsResponse?> = _appointments


    private val _appointmentState = MutableStateFlow<UiState<AppointementResponse>>(UiState.Loading)
    val appointmentState: StateFlow<UiState<AppointementResponse>> = _appointmentState

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

     fun loadAppointmentsForDate(date: Date){
        _isLoading.value = true
        viewModelScope.launch {

            try {
                _appointments.value =  appointementRepo.getAppointmentOfDoctorOfDay(date)
                Log.e("AppointmentsPendingList", _appointments.value.toString())
                _isLoading.value = false ;

            }catch (e :Exception){
                println("--------------------------------------------------------------")
                println("--------------------------------------------------------------")
                  println("the message " + e.message)
                println("--------------------------------------------------------------")
                println("--------------------------------------------------------------")
              _isLoading.value = false ;
            }
        }


    }


    fun confirmAppointment(appointmentId: String) {
        _appointmentState.value = UiState.Loading

        viewModelScope.launch {
            val state = appointementRepo.confirmAppointment(appointmentId)
            _appointmentState.value = state
        }
    }

    fun cancelAppointment(appointmentId: String) {
        _appointmentState.value = UiState.Loading

        viewModelScope.launch {
            val state = appointementRepo.cancelAppointment(appointmentId)
            _appointmentState.value = state
        }
    }

   /* fun setAppointmentsForDate(date: Date, newAppointments: List<Appointment>) {
        val dateStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date)
        _appointments.value = _appointments.value.toMutableMap().apply {
            this[dateStr] = newAppointments
        }
    }
    */
}
