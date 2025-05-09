package com.example.projecttdm.data.endpoint


import com.example.projecttdm.data.model.AppointementResponse
import com.example.projecttdm.data.model.AppointmentRequest
import com.example.projecttdm.data.model.AppointmentSlot
import com.example.projecttdm.ui.patient.PatientRoutes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate


interface BookAppointmentEndPoint {

    @GET("appointment-slots/doctor/{id}/by-date")
    suspend fun getSlotsByDoctorIdAndDate(
        @Path("id") doctorId: String,
        @Query("workingDate") workingDate: LocalDate
    ): List<AppointmentSlot>

    @POST("appointments/")
    suspend fun bookAppointment(@Body request:AppointmentRequest ): AppointementResponse


}