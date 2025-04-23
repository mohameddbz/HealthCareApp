package com.example.projecttdm.data.endpoint

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//------------------------- don't touch this file ------------------------//
object ApiClient {

    private const val BASE_URL = "https://doctor-app-backend-b63m.onrender.com/api/" // Remplace par ta vraie URL
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}