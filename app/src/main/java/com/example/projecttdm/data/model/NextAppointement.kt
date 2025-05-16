package com.example.projecttdm.data.model

import androidx.media3.common.util.HandlerWrapper.Message
import com.example.projecttdm.state.UiState
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date


data class DateRequest(
    val date: Date  // ou LocalDate si tu veux, mais String c'est plus simple ici
)



data class NextAppointment(

    val appointement_id: String,
    val patient_id: String,
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val fullname : String ,
    val reason: String = "",
    val start_time : String,
    val numberOfVisit : Int = 0,
    val imageUrl: ImageBlob? = null, // For network images
)



data class NextAppointementResponse (
    val success: Boolean ,
    val message: String ="",
    val nextAppointment : NextAppointment
)

data class NextAppointementsResponse(
    val success: Boolean,
    val message: String ="",
    val nextAppointments : List<NextAppointment>
)