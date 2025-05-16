package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.AppointementResponse
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.NextAppointementResponse
import com.example.projecttdm.data.model.NextAppointementsResponse
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.data.model.auth.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import com.example.projecttdm.data.model.AppointmentReviewData
import com.example.projecttdm.data.model.DateRequest
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Date

interface AppointmentEndPoint {

    @GET("appointments/doctor/my-next-appointments")
    suspend fun getNextAppointmentForDoctor(): NextAppointementResponse


    @GET("appointments/doctor/todays-appointments")
    suspend fun getTodaysAppointmentsForDoctor(): NextAppointementsResponse

    @GET("appointments/{id}")
    suspend fun getAppointmentById(@Path("id") appointmentId: String): Appointment

    //@GET("appointments/")
    //suspend fun getAllAppointments(): List<Appointment>

    @GET("appointments/patient/")
    suspend fun getAppointmentsByPatientId(): List<Appointment>


    @GET("appointments/first-upcoming")
    suspend fun getfirstUpcomingAppointment(): Appointment

    @GET("appointments/appointment/{id}")
    suspend fun getAppointmentDetails(@Path("id") appointmentId: String): AppointmentReviewData

    @POST("appointments/doctor/day")
    suspend fun  getAppointmentOfDoctorOfDay(@Body date : DateRequest) : NextAppointementsResponse

    @PATCH("appointments/cancel/{id}")
    suspend fun cancelAppointment(@Path("id") appointmentId: String): AppointementResponse

    @PATCH("appointments/confirm/{id}")
    suspend fun confirmAppointement(@Path("id") appointmentId: String): AppointementResponse


}