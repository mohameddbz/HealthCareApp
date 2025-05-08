package com.example.projecttdm.viewmodel


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.QRCodeData
import com.example.projecttdm.data.repository.AppointmentRepository
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class AppointmentViewModel(
) : ViewModel() {

    private val repository = RepositoryHolder.appointmentRepository
    private val doctorRepository = RepositoryHolder.doctorRepository

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

    // For QR code data
    private val _qrCodeData = MutableStateFlow<QRCodeData?>(null)
    val qrCodeData: StateFlow<QRCodeData?> = _qrCodeData.asStateFlow()

    // For Dialog state
    private val _showQRCodeDialog = MutableStateFlow(false)
    val showQRCodeDialog: StateFlow<Boolean> = _showQRCodeDialog.asStateFlow()



//    init {
//        refreshAppointments()
//    }

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
                val message = "Error loading appointments: ${e.message}"
                println(message)
                e.printStackTrace()
                _errorMessage.emit(message)
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
                    // If we're showing QR for this appointment, close the dialog
                    if (_selectedAppointment.value?.id == appointmentId) {
                        closeQRCodeDialog()
                    }
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


    fun getAppointmentDetails(appointmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // Reset current appointment to avoid showing stale data
            _selectedAppointment.value = null

            repository.getAppointmentById(appointmentId).collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // Loading already set to true above
                    }
                    is UiState.Success -> {
                        _selectedAppointment.value = state.data
                        _isLoading.value = false
                    }
                    is UiState.Error -> {
                        _errorMessage.emit("Error fetching appointment: ${state.message}")
                        _isLoading.value = false
                    }
                    else -> {
                        // Handle other states if needed
                    }
                }
            }
        }
    }

    fun showAppointmentQRCode(appointmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // First get the appointment details
                repository.getAppointmentById(appointmentId).collect { appointment ->
                    //_selectedAppointment.value = appointment

                    // Then fetch the QR code data
                    if (appointment != null) {
                        val result = repository.getAppointmentQRCode(appointmentId)
                        if (result.isSuccess) {
                            _qrCodeData.value = result.getOrNull()
                            _showQRCodeDialog.value = true
                            _isLoading.value = false
                        } else {
                            _errorMessage.emit("Failed to generate QR code: ${result.exceptionOrNull()?.message}")
                        }
                    } else {
                        _errorMessage.emit("Appointment not found")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error showing QR code: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // New method to load latest appointment for QR display
    fun loadLatestAppointment() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Get the first pending appointment
                repository.getAppointmentsByStatus(AppointmentStatus.PENDING).collect { appointments ->
                    if (appointments.isNotEmpty()) {
                        val latestAppointment = appointments.first()
                        _selectedAppointment.value = latestAppointment

                        // Get QR code for this appointment
                        val result = repository.getAppointmentQRCode(latestAppointment.id)
                        if (result.isSuccess) {
                            _qrCodeData.value = result.getOrNull()
                            _showQRCodeDialog.value = true
                        } else {
                            _errorMessage.emit("Failed to generate QR code: ${result.exceptionOrNull()?.message}")
                        }
                    } else {
                        _errorMessage.emit("No pending appointments found")
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error loading appointment: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    fun closeQRCodeDialog() {
        _showQRCodeDialog.value = false
        _qrCodeData.value = null
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
                    val errorMsg = result.exceptionOrNull()?.message
                    _errorMessage.emit("Failed to refresh appointments: $errorMsg")
                }
            } catch (e: Exception) {
                _errorMessage.emit("Error refreshing appointments: ${e.message}")
            } finally {
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

    fun getDoctorName(doctorId: String): String {
        return try {
            val doctor = doctorRepository.getDoctorById(doctorId)
            doctor?.name ?: "Unknown Doctor"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

}