package com.example.projecttdm.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.repository.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class RescheduleAppointmentViewModel : ViewModel() {
    private val repository = AppointmentRepository()

    // Selected date for rescheduled appointment
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    // Selected time for rescheduled appointment
    private val _selectedTime = MutableStateFlow<LocalTime?>(null)
    val selectedTime: StateFlow<LocalTime?> = _selectedTime.asStateFlow()

    // Current month for date picker
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    // Original appointment to be rescheduled
    private val _appointmentToReschedule = MutableStateFlow<Appointment?>(null)
    val appointmentToReschedule: StateFlow<Appointment?> = _appointmentToReschedule.asStateFlow()

    // Available time slots
    private val _availableTimeSlots = MutableStateFlow<List<LocalTime>>(emptyList())
    val availableTimeSlots: StateFlow<List<LocalTime>> = _availableTimeSlots.asStateFlow()

    // Success state
    private val _rescheduleSuccessful = MutableStateFlow(false)
    val rescheduleSuccessful: StateFlow<Boolean> = _rescheduleSuccessful.asStateFlow()

    // Function to set the appointment to reschedule
    suspend fun setAppointmentToReschedule(appointmentId: String) {
            repository.getAppointmentById(appointmentId).collect { appointment ->
                _appointmentToReschedule.value = appointment
            }
    }


    // Function to set the selected date
    fun setSelectedDate(date: LocalDate?) {
        _selectedDate.value = date
        _selectedTime.value = null

        // If a date is selected, update available time slots
        if (date != null) {
            _availableTimeSlots.value = repository.getAvailableTimeSlots(date)
        } else {
            _availableTimeSlots.value = emptyList()
        }
    }

    // Function to set the selected time
    fun setSelectedTime(time: LocalTime?) {
        _selectedTime.value = time
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

    fun rescheduleAppointment(): Boolean {
        val appointment = _appointmentToReschedule.value
        val date = _selectedDate.value
        val time = _selectedTime.value

        if (appointment == null || date == null || time == null) {
            return false
        }

        val result = repository.rescheduleAppointment(
            appointmentId = appointment.id,
            newDate = date,
            newTime = time
        )

        if (result) {
            _rescheduleSuccessful.value = true
            // Reset fields after rescheduling
            _selectedDate.value = null
            _selectedTime.value = null
            _appointmentToReschedule.value = null
        }

        return result
    }

    // Reset the success state
    fun resetSuccess() {
        _rescheduleSuccessful.value = false
    }
    fun debugReschedule(): Boolean {
        // Force the success state to be true for debugging
        _rescheduleSuccessful.value = true
        return true
    }

    // Function to get patient's appointments
    fun getPatientAppointments(patientId: String): List<Appointment> {
        return repository.getPatientAppointments(patientId)
    }
}