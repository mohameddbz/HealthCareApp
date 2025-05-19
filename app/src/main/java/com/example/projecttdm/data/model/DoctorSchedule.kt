package com.example.projecttdm.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
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

data class Docschedule(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("doctor_id")
    val doctorId: Int,

    @SerializedName("working_date")
    val workingDate: String,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("appointment_duration")
    val appointmentDuration: Int
) : Serializable

data class CreateSchedulesByDayRequest(
    @SerializedName("doctor_id")
    val doctorId: Int,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("appointment_duration")
    val appointmentDuration: Int
)