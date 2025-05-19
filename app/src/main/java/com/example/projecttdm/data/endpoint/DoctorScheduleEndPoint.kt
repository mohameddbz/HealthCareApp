package com.yourapp.data.endpoint


import com.example.projecttdm.data.model.CreateSchedulesByDayRequest
import com.example.projecttdm.data.model.Docschedule
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DoctorScheduleApi {
    @POST("doctor-schedules/bulk-by-day")
    suspend fun createSchedulesByDay(
        @Header("journee") dayName: String,
        @Body request: CreateSchedulesByDayRequest
    ): Response<List<Docschedule>>
}