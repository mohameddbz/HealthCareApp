package com.example.projecttdm.data.model

data class Review(
    val id: String,
    val reviewerName: String,
    val reviewerImage: String,
    val rating: Int,
    val comment: String
)