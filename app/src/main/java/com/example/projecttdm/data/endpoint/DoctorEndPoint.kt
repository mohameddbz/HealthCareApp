package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentWeekApiResponse
import com.example.projecttdm.data.model.AppointmentWeekResponse
import com.example.projecttdm.data.model.Doctor
import retrofit2.http.GET
import retrofit2.http.Path

interface DoctorEndPoint {
    @GET("doctors/")
    suspend fun getDoctors(): List<Doctor>

    @GET("doctors/{id}")
    suspend fun getDoctorById(@Path("id") doctorId: String): Doctor

    @GET("doctors/{id}/appointments/{date}")
    suspend fun getAppointmentsByDate(
        @Path("id") doctorId: String,
        @Path("date") date: String
    ): AppointmentWeekApiResponse
}