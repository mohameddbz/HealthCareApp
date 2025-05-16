package com.example.projecttdm.data.endpoint

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.utils.AppointmentStatusTypeAdapter
import com.example.projecttdm.utils.LocalDateAdapter
import com.example.projecttdm.utils.LocalTimeAdapter
import com.example.projecttdm.utils.TokenInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime


//------------------------- don't touch this file ------------------------//

@RequiresApi(Build.VERSION_CODES.O)
object ApiClient {

//    private const val BASE_URL = "https://doctor-app-backend-b63m.onrender.com/api/"
 // private const val BASE_URL = "http://10.0.2.2:5000/api/"
    private const val BASE_URL = "http://172.20.10.3:5000/api/"
    private var tokenProvider: () -> String? = { null }

    fun setTokenProvider(provider: () -> String?) {
        tokenProvider = provider
    }

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(tokenProvider))
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(AppointmentStatus::class.java, AppointmentStatusTypeAdapter())
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}