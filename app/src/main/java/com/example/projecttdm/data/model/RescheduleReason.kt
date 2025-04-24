package com.example.projecttdm.data.model

data class RescheduleReason(
    val id: String,
    val text: String
) {
    // Added toString method for easier serialization
    override fun toString(): String {
        return "$id::$text"
    }
}
