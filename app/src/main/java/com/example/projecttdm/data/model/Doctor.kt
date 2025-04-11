package com.example.projecttdm.data.model

import androidx.annotation.DrawableRes

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val hospital: String,
    val rating: Float,
    val reviewCount: Int,
    val imageResId: Int? = null, // For local drawables
    val imageUrl: String? = null // For network images
)
