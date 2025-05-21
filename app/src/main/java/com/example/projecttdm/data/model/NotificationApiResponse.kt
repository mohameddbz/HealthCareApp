package com.example.projecttdm.data.model

data class NotificationApiResponse(
    val message: String,
    val data: List<NotificationResponse>,
    val count: Int
)
