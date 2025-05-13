package com.example.projecttdm.data.model

data class AppointmentReviewData(
    val doctor: doctor2,
    val appointment: appointment2,
    val patient: patient2
)

data class doctor2(
    val id: String,
    val name: String,
    val specialty: specialty2,
    val hospital: String,
    val rating: Float,
    val reviewCount: Int,
    val imageResId: ImageBlob? = null
)

data class specialty2(
    val id: String,
    val name: String
)

data class appointment2(
    val id: String,
    val patientId: String,
    val doctorId: String,
    val date: String?, // Can be LocalDate if you need to convert it to Date object
    val time: String?, // Can be LocalTime if you need to convert it
    val status: String,
    val reason: String
)

data class patient2(
    val id: String,
    val fullName: String,
    val gender: String,
    val age: Int,
    val problemDescription: String
)