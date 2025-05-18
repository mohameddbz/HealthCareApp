package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.FavoriteDoctorsApiResponse
import retrofit2.http.*

interface FavoriteEndPoint {
    @GET("favorites")
    suspend fun getFavoriteDoctors(): FavoriteDoctorsApiResponse

    @POST("favorites/{doctorId}")
    suspend fun addFavoriteDoctor(@Path("doctorId") doctorId: Int)

    @DELETE("favorites/{doctorId}")
    suspend fun removeFavoriteDoctor(@Path("doctorId") doctorId: Int)

    @GET("favorites/check/{doctorId}")
    suspend fun checkFavoriteDoctor(@Path("doctorId") doctorId: Int): Boolean
}
