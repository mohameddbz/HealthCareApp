package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.NotificationApiResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationEndPoint {

    @GET("notifications")
    suspend fun getNotifications(): NotificationApiResponse


    @POST("notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") notificationId: Int): Unit

}
