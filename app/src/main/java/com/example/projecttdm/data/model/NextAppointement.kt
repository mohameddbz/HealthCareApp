package com.example.projecttdm.data.model

import androidx.media3.common.util.HandlerWrapper.Message
import com.example.projecttdm.state.UiState
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

data class NextAppointment(

    val appointement_id: String,
    val patient_id: String,
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val fullname : String ,
    val reason: String = "",
    val start_time : String,
    val numberOfVisit : Int = 0
)



data class NextAppointementResponse (
    val success: Boolean ,
    val message: String ="",
    val nextAppointment : NextAppointment
)