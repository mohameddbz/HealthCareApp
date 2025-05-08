package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.NextAppointementResponse
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.data.model.auth.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface AppointmentEndPoint {

    @GET("appointments/doctor/my-next-appointments")
    suspend fun getNextAppointmentForDoctor(): NextAppointementResponse



}