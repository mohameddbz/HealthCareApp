package com.example.projecttdm.data.model


/**
 * Data class to represent QR code information for an appointment
 */

data class QRCodeData(
    val id: String,
    val content: String,
    val timestamp: Long,
    val image: String // base64 image
)
