package com.example.projecttdm.data.model

import com.google.gson.annotations.SerializedName


/**
 * Data class to represent QR code information for an appointment
 */

data class QRCodeData(
//    @SerializedName("appointmentId")
    val id: String,
    val content: String,
    val timestamp: Long,
    val image: String? = null
)
