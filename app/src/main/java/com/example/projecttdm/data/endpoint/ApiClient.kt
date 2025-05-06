package com.example.projecttdm.data.endpoint

import com.example.projecttdm.utils.TokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//------------------------- don't touch this file ------------------------//

object ApiClient {

//    private const val BASE_URL = "https://doctor-app-backend-b63m.onrender.com/api/"
private const val BASE_URL = "http://192.168.100.50:5000/api/"
    private var tokenProvider: () -> String? = { null }

    fun setTokenProvider(provider: () -> String?) {
        tokenProvider = provider
    }

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(tokenProvider))
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}