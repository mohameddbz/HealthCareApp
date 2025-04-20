package com.example.projecttdm.data.model


/**
 * Data class to represent QR code information for an appointment
 */
data class QRCodeData(
    val id: String,           // Appointment ID
    val content: String,      // Content to be encoded in the QR code
    val timestamp: Long       // When the QR code was generated
)