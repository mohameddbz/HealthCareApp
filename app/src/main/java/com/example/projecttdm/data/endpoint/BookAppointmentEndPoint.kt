package com.example.projecttdm.data.endpoint


import com.example.projecttdm.data.model.AppointmentSlot
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate


interface BookAppointmentEndPoint {

    @GET("appointment-slots/doctor/{id}/by-date")
    suspend fun getSlotsByDoctorIdAndDate(
        @Path("id") doctorId: String,
        @Query("workingDate") workingDate: LocalDate
    ): List<AppointmentSlot>


}