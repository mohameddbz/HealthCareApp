package com.example.projecttdm.data.model


data class Doctor(
    val id: String,
    val name: String,
    val specialty: Specialty, // Changed from String to Specialty data class
    val hospital: String,
    val rating: Float,
    val reviewCount: Int,
    val imageResId: Int? = null, // For local drawables
    val imageUrl: String? = null // For network images
)