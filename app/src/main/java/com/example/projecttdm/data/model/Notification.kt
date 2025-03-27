package com.example.projecttdm.data.model

data class Notification(
    val notification_id : Int ,
    val user_id : Int ,
    val title: String,
    val message: String,
    val date: String,
    val heure : String,
    val is_read: Boolean = false
)