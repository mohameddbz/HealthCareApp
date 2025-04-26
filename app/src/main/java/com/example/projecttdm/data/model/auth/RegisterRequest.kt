package com.example.projecttdm.data.model.auth

import android.net.Uri

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String,
    val sexe: String,
    val date_birthday: String,
    val role: String = "user", // or whatever your default is
    val image: Uri? // Will be handled as Multipart later
)