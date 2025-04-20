package com.example.projecttdm.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.local.AppointmentData
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class AppointmentRepository {
    // In-memory storage for appointments
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeSlotAvailable(time: LocalTime): Boolean {
        return AppointmentData.isTimeSlotAvailable(time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bookAppointment(
        patientId: String,
        doctorId: String,
        date: LocalDate,
        time: LocalTime,
        reason: String = ""
    ): Appointment {
        val appointment = Appointment(
            id = UUID.randomUUID().toString(),
            patientId = patientId,
            doctorId = doctorId,
            date = date,
            time = time,
            status = AppointmentStatus.PENDING,
            reason = reason
        )

        _appointments.value = _appointments.value + appointment
        AppointmentData.bookTimeSlot(time)

        return appointment
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun rescheduleAppointment(
        appointmentId: String,
        newDate: LocalDate,
        newTime: LocalTime
    ): Boolean {
        val appointment = _appointments.value.find { it.id == appointmentId } ?: return false

        // Release the old time slot
        AppointmentData.releaseTimeSlot(appointment.time)

        // Book the new time slot
        AppointmentData.bookTimeSlot(newTime)

        // Update the appointment
        _appointments.value = _appointments.value.map {
            if (it.id == appointmentId) {
                it.copy(
                    date = newDate,
                    time = newTime,
                    status = AppointmentStatus.RESCHEDULED
                )
            } else {
                it
            }
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAvailableTimeSlots(date: LocalDate): List<LocalTime> {
        return AppointmentData.availableTimeSlots.filter { time ->
            AppointmentData.isTimeSlotAvailable(time)
        }
    }

    fun cancelAppointment(appointmentId: String) {
        val appointment = _appointments.value.find { it.id == appointmentId }

        if (appointment != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppointmentData.releaseTimeSlot(appointment.time)
            }

            _appointments.value = _appointments.value.map {
                if (it.id == appointmentId) {
                    it.copy(status = AppointmentStatus.CANCELLED)
                } else {
                    it
                }
            }
        }
    }

    fun getPatientAppointments(patientId: String): List<Appointment> {
        return _appointments.value.filter { it.patientId == patientId }
    }

    fun getDoctorAppointments(doctorId: String): List<Appointment> {
        return _appointments.value.filter { it.doctorId == doctorId }
    }

    fun getAppointmentById(appointmentId: String): Appointment? {
        return _appointments.value.find { it.id == appointmentId }
    }
}