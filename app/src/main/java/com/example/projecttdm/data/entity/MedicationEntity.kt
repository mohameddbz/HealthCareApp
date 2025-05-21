package com.example.projecttdm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// 1. Entités Room pour le stockage local

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val prescriptionId: String,
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String
)