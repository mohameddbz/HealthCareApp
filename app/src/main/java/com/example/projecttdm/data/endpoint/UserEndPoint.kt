package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.User
import com.example.projecttdm.data.model.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part


interface UserEndPoint {

    @GET("users/current")
    suspend fun getCurrentUser(): User

    @GET("users/currentPatient")
    suspend fun getProfile(): Response<UserProfileResponse>

    @Multipart
    @PUT("users/editProfile")
    suspend fun updateProfile(
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("date_birthday") dateBirthday: RequestBody?,
        @Part("sexe") sexe: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<UserProfileResponse>

}