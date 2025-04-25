package com.example.projecttdm.data.endpoint

import com.example.projecttdm.data.model.User

import retrofit2.http.GET


interface UserEndPoint {

    @GET("users/current")
    suspend fun getCurrentUser(): User

}