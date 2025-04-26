package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.data.model.auth.LoginRequest
import com.example.projecttdm.data.model.auth.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthEndPoint {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part image: MultipartBody.Part?,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone") phone: RequestBody,
      //  @Part("sexe") gender: RequestBody,
       // @Part("date_birthday") birthDate: RequestBody,
        @Part("role") role: RequestBody
      ):AuthResponse

}