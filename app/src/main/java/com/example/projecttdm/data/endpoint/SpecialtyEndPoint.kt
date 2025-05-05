package com.example.projecttdm.data.endpoint


import com.example.projecttdm.data.model.Specialty
import retrofit2.http.GET

interface SpecialtyEndPoint {

    @GET("specialties/")
    suspend fun getSpecialties(): List<Specialty>


}