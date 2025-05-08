package com.example.projecttdm.data.model

data class Review(
    val id: String,
    val doctor_id: String,
    val reviewerName: String,
    val reviewerImage: ImageBlob,
    val rating: Float,
    val comment: String
)