package com.example.projecttdm.data.model.auth

data class AuthResponse(
    val token: String,
    val userId: Int,
    val role: String
)