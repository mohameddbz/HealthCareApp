package com.example.projecttdm.data.endpoint


import com.example.projecttdm.data.entity.SyncPrescriptionsRequest
import com.example.projecttdm.data.entity.SyncPrescriptionsResponse
import com.example.projecttdm.data.model.FullPrescription
import com.example.projecttdm.data.model.PrescriptionRequest
import com.example.projecttdm.data.model.PrescriptionRes
import com.example.projecttdm.data.model.PrescriptionResponse
import com.example.projecttdm.data.model.Prescriptions
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PrescriptionEndPoint {
    @GET("prescriptions")
    suspend fun getAllPrescriptions(): List<Prescriptions>

    @GET("prescriptions/{id}")
    suspend fun getPrescriptionById(@Path("id") id: String): PrescriptionResponse

    @GET("prescriptions/doctor/{doctorId}")
    suspend fun getPrescriptionsByDoctor(@Path("doctorId") doctorId: String): List<Prescriptions>

    @GET("prescriptions/patient/{patientId}")
    suspend fun getPrescriptionsByPatient(@Path("patientId") patientId: String): List<Prescriptions>

    @POST("prescriptions")
    suspend fun createPrescription(@Body request: PrescriptionRequest): PrescriptionResponse

    @PUT("prescriptions/{id}")
    suspend fun updatePrescription(
        @Path("id") id: String,
        @Body request: PrescriptionRequest
    ): PrescriptionResponse

    @DELETE("prescriptions/{id}")
    suspend fun deletePrescription(@Path("id") id: String): PrescriptionResponse

    @GET("prescriptions/appointments/{id}")
    suspend fun getPrescriptions(@Path("id") id: String): PrescriptionRes

    @POST("prescriptions/sync-prescriptions")
    suspend fun syncPrescriptions(
        @Body body: SyncPrescriptionsRequest
    ): Response<SyncPrescriptionsResponse>
}
