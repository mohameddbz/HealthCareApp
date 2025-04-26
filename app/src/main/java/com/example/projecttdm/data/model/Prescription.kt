package com.example.projecttdm.data.model

data class Prescription(
    val doctor: Doctor,
    val patient: Patient,
    val medications: List<Medication>,
    val date: String
)