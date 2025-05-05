package com.example.projecttdm.data.endpoint


import com.example.projecttdm.data.model.Specialty
import retrofit2.Response // Or your preferred Response wrapper
import retrofit2.http.GET

interface SpecialtyEndPoint {

    // Example endpoint - adjust path as needed
    @GET("specialties/")
    suspend fun getSpecialties(): List<Specialty>


}