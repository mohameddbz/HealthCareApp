package com.example.projecttdm.data.entity

data class SyncPrescriptionsRequest(
    val prescriptions: List<PrescriptionSyncDto>
)

data class PrescriptionSyncDto(
    val appointmentId:String ,
    val patientId: String,
    val doctorId: String,
    val instructions: String,
    val expiryDate: String,
    val medications: List<MedicationDto>
)

data class MedicationDto(
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String
)

data class SyncPrescriptionsResponse(
    val success: Boolean,
    val results: List<SyncResult>
)

data class SyncResult(
    val id: Int? = null,
    val success: Boolean,
    val error: String? = null
)