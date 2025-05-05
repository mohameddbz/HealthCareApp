package com.example.projecttdm.data.model

data class Patient(
    val id: String = "",
    val fullName: String = "",
    val gender: String = "",
    val age: Int = 0,
    val problemDescription: String = ""
)

data class PatientX(
    val patient_id: Int,
    val fullName: String = "",
    val date_birthday: String,
    val sexe: String,
    val user_id: Int,
)
