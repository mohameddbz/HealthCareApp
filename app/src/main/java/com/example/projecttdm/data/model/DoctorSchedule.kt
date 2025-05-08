package com.example.projecttdm.data.model

import java.time.LocalDate
import java.time.LocalTime

data class DoctorSchedule(
    val schedule_id: Int,
    val doctor_id: Int,
    val working_date: LocalDate,
    val start_time: LocalTime,
    val end_time: LocalTime,
    val appointment_duration: Int
)