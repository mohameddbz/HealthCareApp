package com.example.projecttdm.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.repository.AppointmentRepository
import com.example.projecttdm.data.repository.DoctorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class AppointmentViewModel(
) : ViewModel() {

    private val repository = AppointmentRepository()
    private val doctorRepository = DoctorRepository()

    private val _selectedTab = MutableStateFlow(AppointmentStatus.PENDING)
    val selectedTab: StateFlow<AppointmentStatus> = _selectedTab.asStateFlow()

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // For appointment details
    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment: StateFlow<Appointment?> = _selectedAppointment.asStateFlow()

    init {
        loadAppointments(AppointmentStatus.PENDING)
        refreshAppointments()
    }

    fun selectTab(status: AppointmentStatus) {
        _selectedTab.value = status
        loadAppointments(status)
    }

    // This function loads a list of appointments based on a given status
    private fun loadAppointments(status: AppointmentStatus) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAppointmentsByStatus(status).collect { appointmentList ->
                    _appointments.value = appointmentList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error loading appointments: ${e.message}")
                _isLoading.value = false
            }
        }
    }


    // This function performs a search operation on the list of appointments
    fun searchAppointments(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query
            _isLoading.value = true
            try {
                val doctors = doctorRepository.getDoctors()
                repository.searchAppointments(query, doctors).collect { results ->
                    _appointments.value = results
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error searching appointments: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    // Useful when a user clears a search input
    fun clearSearch() {
        _searchQuery.value = ""
        loadAppointments(_selectedTab.value)
    }


    fun cancelAppointment(appointmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.cancelAppointment(appointmentId)
                if (result.isSuccess) {
                    loadAppointments(_selectedTab.value)
                } else {
                    _errorMessage.emit("Failed to cancel appointment: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error cancelling appointment: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun rescheduleAppointment(appointmentId: String, newDate: LocalDate, newTime: LocalTime) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.rescheduleAppointment(appointmentId, newDate, newTime)
                if (result.isSuccess) {
                    // Refresh the appointment list
                    loadAppointments(_selectedTab.value)
                } else {
                    _errorMessage.emit("Failed to reschedule appointment: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error rescheduling appointment: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getAppointmentDetails(appointmentId: String) {
        viewModelScope.launch {
            repository.getAppointmentById(appointmentId).collect { appointment ->
                _selectedAppointment.value = appointment
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAppointments() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.refreshAppointments()
                if (result.isSuccess) {
                    loadAppointments(_selectedTab.value)
                } else {
                    _errorMessage.emit("Failed to refresh appointments: ${result.exceptionOrNull()?.message}")
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error refreshing appointments: ${e.message}")
                _isLoading.value = false
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTime(time: LocalTime): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        return time.format(formatter)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(date: LocalDate): String {
        val today = LocalDate.now()
        return when {
            date.isEqual(today) -> "Today"
            date.isEqual(today.plusDays(1)) -> "Tomorrow"
            date.isEqual(today.minusDays(1)) -> "Yesterday"
            else -> date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
        }
    }


    fun updateAppointmentNotes(appointmentId: String, notes: String) {
        viewModelScope.launch {
            try {
                val result = repository.updateAppointmentNotes(appointmentId, notes)
                if (result.isSuccess) {
                    getAppointmentDetails(appointmentId)
                } else {
                    _errorMessage.emit("Failed to update notes: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error updating appointment notes: ${e.message}")
            }
        }
    }
}