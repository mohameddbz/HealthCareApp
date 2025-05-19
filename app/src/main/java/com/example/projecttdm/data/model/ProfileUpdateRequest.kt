package com.example.projecttdm.data.model

data class ProfileUpdateRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val image: String?,
    val date_birthday: String?,
    val sexe: String
)