package com.example.projecttdm.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

data class Appointment(
    @SerializedName("appointment_id")
    val id: String,

    @SerializedName("patient_id")
    val patientId: String,

    @SerializedName("APPOINTMENT_SLOT.DOCTOR_SCHEDULE.doctor_id")
    val doctorId: String,

    @SerializedName("APPOINTMENT_SLOT.working_date")
    val date: LocalDate,

    @SerializedName("APPOINTMENT_SLOT.start_time")
    val time: LocalTime,

    val status: AppointmentStatus = AppointmentStatus.PENDING,

    val reason: String = ""
)
