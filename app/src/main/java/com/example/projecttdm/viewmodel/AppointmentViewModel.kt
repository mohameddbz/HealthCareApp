package com.example.projecttdm.viewmodel


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.AppointementResponse
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentReviewData
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

    private val _qrCodeData = MutableLiveData<Result<QRCodeData>>()
    val qrCodeData: LiveData<Result<QRCodeData>> = _qrCodeData

    // For appointment details
    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment: StateFlow<Appointment?> = _selectedAppointment.asStateFlow()

    // For Dialog state
    private val _showQRCodeDialog = MutableStateFlow(false)
    val showQRCodeDialog: StateFlow<Boolean> = _showQRCodeDialog.asStateFlow()

    val _appointmentState = MutableStateFlow<UiState<AppointmentReviewData>>(UiState.Loading)
    val appointmentState: StateFlow<UiState<AppointmentReviewData>> = _appointmentState.asStateFlow()





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


    fun fetchQRCode(appointmentId: String) {
        viewModelScope.launch {
            val result = repository.getQRCodeForAppointment(appointmentId)
            _qrCodeData.postValue(result)
            println("================= ${result}")
        }
    }



    // New method to load latest appointment for QR display
//    fun loadLatestAppointment() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                // Get the first pending appointment
//                repository.getAppointmentsByStatus(AppointmentStatus.PENDING).collect { appointments ->
//                    if (appointments.isNotEmpty()) {
//                        val latestAppointment = appointments.first()
//                        _selectedAppointment.value = latestAppointment
//
//                        // Get QR code for this appointment
//                        val result = repository.getAppointmentQRCode(latestAppointment.id)
//                        if (result.isSuccess) {
//                            _qrCodeData.value = result.getOrNull()
//                            _showQRCodeDialog.value = true
//                        } else {
//                            _errorMessage.emit("Failed to generate QR code: ${result.exceptionOrNull()?.message}")
//                        }
//                    } else {
//                        _errorMessage.emit("No pending appointments found")
//                    }
//                    _isLoading.value = false
//                }
//            } catch (e: Exception) {
//                _errorMessage.emit("Error loading appointment: ${e.message}")
//                _isLoading.value = false
//            }
//        }
//    }

    fun closeQRCodeDialog() {
        _showQRCodeDialog.value = false
       // _qrCodeData.value = null
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

    fun fetchAppointmentDetails(appointmentId: String) {
        viewModelScope.launch {
            repository.getAppointmentDetailsById(appointmentId)
                .collect { uiState ->
                    _appointmentState.value = uiState
                }
        }
    }


}