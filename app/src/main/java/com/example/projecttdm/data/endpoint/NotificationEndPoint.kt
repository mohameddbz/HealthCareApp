package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.NotificationApiResponse
import retrofit2.http.GET

interface NotificationEndPoint {
    @GET("notifications")
    suspend fun getNotifications(): NotificationApiResponse
}
