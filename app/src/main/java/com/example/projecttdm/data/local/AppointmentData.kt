package com.example.projecttdm.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import java.time.LocalDate
import java.time.LocalTime

object AppointmentsData {
    // Simple list of available time slots
    @RequiresApi(Build.VERSION_CODES.O)
    val Appointments = listOf(
            Appointment(
                id = "1",
                doctorId = "1",
                patientId = "p1",
                date = LocalDate.now(),
                time = LocalTime.of(16, 0),
                status = AppointmentStatus.PENDING,
                reason = "Annual check-up",
            ),
            Appointment(
                id = "2",
                doctorId = "2",
                patientId = "p1",
                date = LocalDate.now(),
                time = LocalTime.of(14, 0),
                status = AppointmentStatus.PENDING,
                reason = "Follow-up consultation"
            ),
            Appointment(
                id = "3",
                doctorId = "3",
                patientId = "p1",
                date = LocalDate.now(),
                time = LocalTime.of(5, 0),
                status = AppointmentStatus.PENDING,
                reason = "Medication review"
            ),
            Appointment(
                id = "4",
                doctorId = "4",
                patientId = "p1",
                date = LocalDate.now(),
                time = LocalTime.of(11, 30),
                status = AppointmentStatus.COMPLETED,
                reason = "Regular check-up",
            ),
            Appointment(
                id = "5",
                doctorId = "5",
                patientId = "p1",
                date = LocalDate.now(),
                time = LocalTime.of(9, 15),
                status = AppointmentStatus.CANCELLED,
                reason = "Dental cleaning"
            ),
            Appointment(
                id = "6",
                doctorId = "2",
                patientId = "p1",
                date = LocalDate.now(),
                time = LocalTime.of(9, 0),
                status = AppointmentStatus.CONFIRMED,
                reason = "Skin condition assessment"
            )
    )


}