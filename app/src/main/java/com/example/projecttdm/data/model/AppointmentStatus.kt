package com.example.projecttdm.data.model

enum class AppointmentStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED,
    RESCHEDULED;

    // Companion object to safely convert from string
    companion object {
        fun fromString(value: String?): AppointmentStatus {
            return when (value?.uppercase()) {
                "PENDING" -> PENDING
                "CONFIRMED" -> CONFIRMED
                "CANCELLED" -> CANCELLED
                "COMPLETED" -> COMPLETED
                "RESCHEDULED" -> RESCHEDULED
                else -> PENDING // Default value if conversion fails
            }
        }
    }
}