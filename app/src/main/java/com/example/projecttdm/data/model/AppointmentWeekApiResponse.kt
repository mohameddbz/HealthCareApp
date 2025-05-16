package com.example.projecttdm.data.model

data class AppointmentWeekApiResponse(
    val message: String,
    val data: List<AppointmentWeekResponse>,
    val count: Int
)

