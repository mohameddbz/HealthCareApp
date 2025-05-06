package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.Appointment
import retrofit2.http.GET
import retrofit2.http.Path

interface AppointmentEndPoint {


    @GET("appointments/{id}")
    suspend fun getAppointmentById(@Path("id") appointmentId: String): Appointment

    @GET("appointments/")
    suspend fun getAllAppointments(): List<Appointment>

    @GET("appointments/patient/")
    suspend fun getAppointmentsByPatientId(): List<Appointment>


    @GET("appointments/first-upcoming")
    suspend fun getfirstUpcomingAppointment(): Appointment
}