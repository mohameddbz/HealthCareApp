package com.example.projecttdm.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.local.BookAppointmentData
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
        return BookAppointmentData.isTimeSlotAvailable(time)
    }


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

        return appointment
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getAvailableTimeSlots(date: LocalDate): List<LocalTime> {
        return BookAppointmentData.availableTimeSlots.filter { time ->
            BookAppointmentData.isTimeSlotAvailable(time)
        }
    }


    fun cancelAppointment(appointmentId: String) {
        _appointments.value = _appointments.value.map { appointment ->
            if (appointment.id == appointmentId) {
                appointment.copy(status = AppointmentStatus.CANCELLED)
            } else {
                appointment
            }
        }
    }


    fun getPatientAppointments(patientId: String): List<Appointment> {
        return _appointments.value.filter { it.patientId == patientId }
    }


    fun getDoctorAppointments(doctorId: String): List<Appointment> {
        return _appointments.value.filter { it.doctorId == doctorId }
    }
}