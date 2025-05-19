package com.example.projecttdm.data.model

data class Prescription(
    val doctor: Doctor,
    val patient: Patient,
    val medications: List<Medication>,
    val date: String
)



// 1. Modèle de données pour la prescription

data class Prescriptions(
    val id: String? = null,
    val patientId: String,
    val doctorId: String,
    val medications: List<Medications>,
    val instructions: String,
    val appointmentId: String,  // Ajout du champ appointmentId optionnel
    val createdAt: String? = null,
    val expiryDate: String
)

data class Medications(
    val id: String? = null,
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String
)

data class PrescriptionRequest(
    val patientId: String,
//    val doctorId: String,
    val medications: List<Medications>,
    val appointmentId: String ,  // Ajout du champ appointmentId optionnel
    val instructions: String,
    val expiryDate: String
)

data class PrescriptionResponse(
    val success: Boolean,
    val message: String,
    val prescription: FullPrescription? = null
)

data class FullPrescription(
    val prescription_id: Int,
    val patient_id: Int,
    val doctor_id: Int,
    val instructions: String,
    val appointment_id: Int ,  // Ajout du champ appointment_id optionnel
    val created_at: String,
    val expiry_date: String,
    val MEDICATIONs: List<Medication>,
    val Doctor: Doctor,
    val Patient: PatientX,
    val Appointment: Appointment? = null  // Ajout du champ Appointment optionnel
)

