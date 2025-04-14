package com.example.projecttdm.data.model

import java.time.LocalDate
import java.time.LocalTime

data class Appointment(
    val id: String,
    val patientId: String,
    val doctorId: String,
    val date: LocalDate,
    val time: LocalTime,
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val reason: String = "",

)
