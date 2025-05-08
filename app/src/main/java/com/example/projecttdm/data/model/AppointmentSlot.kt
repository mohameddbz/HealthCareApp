package com.example.projecttdm.data.model

import java.time.LocalDate
import java.time.LocalTime

data class AppointmentSlot(
    val slot_id: Int,
    val schedule_id: Int,
    val working_date: LocalDate,
    val start_time: LocalTime,
    val end_time: LocalTime,
    val is_book: Boolean,
    val DOCTOR_SCHEDULE: DoctorSchedule
)