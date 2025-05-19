package com.example.projecttdm.data.model

data class FavoriteDoctorsApiResponse(
    val message: String,
    val data: List<FavoriteDoctorResponse>,
    val count: Int
)
