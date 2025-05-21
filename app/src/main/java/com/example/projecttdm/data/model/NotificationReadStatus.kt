package com.example.projecttdm.data.model

data class NotificationReadStatus(
    val notification_id: Int,
    val is_read: Boolean
)
data class NotificationReadResponse(
    val message: String,
    val data: NotificationReadStatus
)

