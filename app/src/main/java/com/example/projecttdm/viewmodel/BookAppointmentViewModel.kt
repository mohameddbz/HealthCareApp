//package com.example.projecttdm.viewmodel
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.projecttdm.data.model.Appointment
//import com.example.projecttdm.data.model.AppointmentSlot
//import com.example.projecttdm.data.repository.RepositoryHolder
//import com.example.projecttdm.state.UiState
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//import java.time.LocalTime
//import java.time.YearMonth
//
//@RequiresApi(Build.VERSION_CODES.O)
//class BookAppointmentViewModel : ViewModel() {
//    private val repository = RepositoryHolder.appointmentRepository
//    private val bookAppointmentRepository = RepositoryHolder.bookAppointmentRepository
//
//    // Selected date for appointment
//    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
//    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()
//
//    // Selected time for appointment
//    private val _selectedTime = MutableStateFlow<LocalTime?>(null)
//    val selectedTime: StateFlow<LocalTime?> = _selectedTime.asStateFlow()
//
//    // Current month for date picker
//    private val _currentMonth = MutableStateFlow(YearMonth.now())
//    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()
//
//    // Selected doctor ID
//    private val _selectedDoctorId = MutableStateFlow<String?>(null)
//    val selectedDoctorId: StateFlow<String?> = _selectedDoctorId.asStateFlow()
//
//    // Patient ID (assuming the logged-in user is the patient)
//    private val _patientId = MutableStateFlow<String?>(null)
//    val patientId: StateFlow<String?> = _patientId.asStateFlow()
//
//    // Appointment reason
//    private val _reason = MutableStateFlow("")
//    val reason: StateFlow<String> = _reason.asStateFlow()
//
//    // Available time slots
//    private val _availableTimeSlots = MutableStateFlow<List<LocalTime>>(emptyList())
//    val availableTimeSlots: StateFlow<List<LocalTime>> = _availableTimeSlots.asStateFlow()
//
//    // New state for slots from API
//    private val _slotsUiState = MutableStateFlow<UiState<List<AppointmentSlot>>>(UiState.Loading)
//    val slotsUiState: StateFlow<UiState<List<AppointmentSlot>>> = _slotsUiState.asStateFlow()
//
//    // Function to set the patient ID (could be called when user logs in)
//    fun setPatientId(id: String) {
//        _patientId.value = id
//    }
//
//    // Function to set doctor ID
//    fun setDoctorId(id: String) {
//        _selectedDoctorId.value = id
//    }
//
//    // Function to set the selected date
//    fun setSelectedDate(date: LocalDate?) {
//        _selectedDate.value = date
//        _selectedTime.value = null
//
//        // If a date is selected, update available time slots
//        if (date != null) {
//            _availableTimeSlots.value = repository.getAvailableTimeSlots(date)
//        } else {
//            _availableTimeSlots.value = emptyList()
//        }
//    }
//
//    // Function to set the selected time
//    fun setSelectedTime(time: LocalTime?) {
//        _selectedTime.value = time
//    }
//
//    // Function to update the reason for the appointment
//    fun setReason(reason: String) {
//        _reason.value = reason
//    }
//
//    // Function to change the current month in the date picker
//    fun changeMonth(increment: Boolean) {
//        _currentMonth.value = if (increment) {
//            _currentMonth.value.plusMonths(1)
//        } else {
//            _currentMonth.value.minusMonths(1)
//        }
//    }
//
//    // Function to check if a time slot is available
//    fun isTimeSlotAvailable(time: LocalTime): Boolean {
//        return repository.isTimeSlotAvailable(time)
//    }
//
//    // Function to book an appointment
//    fun bookAppointment(): Boolean {
//        val patientId = _patientId.value
//        val doctorId = _selectedDoctorId.value
//        val date = _selectedDate.value
//        val time = _selectedTime.value
//        val reason = _reason.value
//
//        if (patientId == null || doctorId == null || date == null || time == null) {
//            return false
//        }
//
//        repository.bookAppointment(
//            patientId = patientId,
//            doctorId = doctorId,
//            date = date,
//            time = time,
//            reason = reason
//        )
//
//        // Reset fields after booking
//        _selectedDate.value = null
//        _selectedTime.value = null
//        _reason.value = ""
//
//        return true
//    }
//
//    // Function to get patient's appointments
//    fun getPatientAppointments(): List<Appointment> {
//        return _patientId.value?.let { patientId ->
//            repository.getPatientAppointments(patientId)
//        } ?: emptyList()
//    }
//
//    // ⬇️ Add this new function to fetch slots
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun fetchSlotsByDoctorIdAndDate(doctorId: String) {
//        val date = selectedDate.value
//        if (date == null) {
//            _slotsUiState.value = UiState.Error("Selected date is not set.")
//            return
//        }
//
//        viewModelScope.launch {
//            bookAppointmentRepository.getSlotsByDoctorIdAndDate(doctorId, date).collect { result ->
//                _slotsUiState.value = when (result) {
//                    is UiState.Loading -> UiState.Loading
//                    is UiState.Success -> UiState.Success(result.data)
//                    is UiState.Error -> UiState.Error(result.message)
//                    UiState.Init -> UiState.Init
//                }
//            }
//        }
//    }
//
//}

package com.example.projecttdm.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentSlot
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class BookAppointmentViewModel : ViewModel() {
    private val repository = RepositoryHolder.appointmentRepository
    private val bookAppointmentRepository = RepositoryHolder.bookAppointmentRepository

    // Selected date for appointment
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    // Selected time for appointment
    private val _selectedTime = MutableStateFlow<LocalTime?>(null)
    val selectedTime: StateFlow<LocalTime?> = _selectedTime.asStateFlow()

    // Current month for date picker
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    // Selected doctor ID
    private val _selectedDoctorId = MutableStateFlow<String?>(null)
    val selectedDoctorId: StateFlow<String?> = _selectedDoctorId.asStateFlow()

    // Patient ID (assuming the logged-in user is the patient)
    private val _patientId = MutableStateFlow<String?>(null)
    val patientId: StateFlow<String?> = _patientId.asStateFlow()

    // Appointment reason
    private val _reason = MutableStateFlow("")
    val reason: StateFlow<String> = _reason.asStateFlow()

    // Available time slots
    private val _availableTimeSlots = MutableStateFlow<List<LocalTime>>(emptyList())
    val availableTimeSlots: StateFlow<List<LocalTime>> = _availableTimeSlots.asStateFlow()

    // State for slots from API
    private val _slotsUiState = MutableStateFlow<UiState<List<AppointmentSlot>>>(UiState.Init)
    val slotsUiState: StateFlow<UiState<List<AppointmentSlot>>> = _slotsUiState.asStateFlow()

    // Simplified access to available slots (for UI components that don't need to handle loading/error states)
    private val _availableSlots = MutableStateFlow<List<AppointmentSlot>>(emptyList())
    val availableSlots: StateFlow<List<AppointmentSlot>> = _availableSlots.asStateFlow()

    // Loading state for UI components
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Function to set the patient ID
    fun setPatientId(id: String) {
        _patientId.value = id
    }

    // Function to set doctor ID
    fun setDoctorId(id: String) {
        _selectedDoctorId.value = id
    }

    // Function to set the selected date
    fun setSelectedDate(date: LocalDate?) {
        _selectedDate.value = date
        _selectedTime.value = null

        // If a date is selected, update available time slots
        if (date != null && _selectedDoctorId.value != null) {
            fetchSlotsByDoctorIdAndDate(_selectedDoctorId.value!!)
        } else {
            _availableSlots.value = emptyList()
        }
        Log.d("BookAppointmentVM", "Selected date updated to: $date")
    }

    // Function to set the selected time
    fun setSelectedTime(time: LocalTime?) {
        _selectedTime.value = time
    }

    // Function to update the reason for the appointment
    fun setReason(reason: String) {
        _reason.value = reason
    }

    // Function to change the current month in the date picker
    fun changeMonth(increment: Boolean) {
        _currentMonth.value = if (increment) {
            _currentMonth.value.plusMonths(1)
        } else {
            _currentMonth.value.minusMonths(1)
        }
    }

    // Function to check if a time slot is available
    fun isTimeSlotAvailable(time: LocalTime): Boolean {
        return repository.isTimeSlotAvailable(time)
    }

    // Function to book an appointment
    fun bookAppointment(): Boolean {
        val patientId = _patientId.value
        val doctorId = _selectedDoctorId.value
        val date = _selectedDate.value
        val time = _selectedTime.value
        val reason = _reason.value

        if (patientId == null || doctorId == null || date == null || time == null) {
            return false
        }

        repository.bookAppointment(
            patientId = patientId,
            doctorId = doctorId,
            date = date,
            time = time,
            reason = reason
        )

        // Reset fields after booking
        _selectedDate.value = null
        _selectedTime.value = null
        _reason.value = ""

        return true
    }

    // Function to get patient's appointments
    fun getPatientAppointments(): List<Appointment> {
        return _patientId.value?.let { patientId ->
            repository.getPatientAppointments(patientId)
        } ?: emptyList()
    }

    // Function to fetch slots from API
    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchSlotsByDoctorIdAndDate(doctorId: String) {
        val date = selectedDate.value
        if (date == null) {
            _slotsUiState.value = UiState.Error("Selected date is not set.")
            _isLoading.value = false
            return
        }

        _isLoading.value = true
        _slotsUiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                bookAppointmentRepository.getSlotsByDoctorIdAndDate(doctorId, date).collect { result ->
                    _slotsUiState.value = result

                    when (result) {
                        is UiState.Success -> {
                            _availableSlots.value = result.data
                            Log.d("BookAppointmentViewModel", "Slots loaded: ${result.data.size}")
                        }
                        is UiState.Error -> {
                            _availableSlots.value = emptyList()
                            Log.e("BookAppointmentViewModel", "Error loading slots: ${result.message}")
                        }
                        else -> {
                            // Do nothing for Loading and Init states
                        }
                    }
                    // Update loading state based on the current UiState
                    _isLoading.value = result is UiState.Loading
                }
            } catch (e: Exception) {
                Log.e("BookAppointmentViewModel", "Exception fetching slots", e)
                _slotsUiState.value = UiState.Error("Failed to load slots: ${e.message}")
                _availableSlots.value = emptyList()
                _isLoading.value = false
            }
        }
    }
}