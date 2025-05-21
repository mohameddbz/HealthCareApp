package com.example.projecttdm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prescriptions")
data class PrescriptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientId: String,
    val appointmentId: String,
    val instructions: String,
    val expiryDate: String,
    val medicationsJson: String , // On va sérialiser la liste des médicaments
    val isSynced : Int = 0
)