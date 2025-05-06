package com.example.projecttdm.data.endpoint


import com.example.projecttdm.data.model.Review
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewEndPoint {

    @GET("reviews/doctor/{doctorId}")
    suspend fun getReviewsByDoctor(@Path("doctorId") doctorId: String): List<Review>

}