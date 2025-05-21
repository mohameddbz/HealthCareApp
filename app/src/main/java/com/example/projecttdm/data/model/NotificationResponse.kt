package com.example.projecttdm.data.model



data class NotificationResponse(
    val notification_id: Int,
    val title: String,
    val message: String,
    val is_read: Boolean,
    val created_at: String
)

