package com.example.projecttdm.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.projecttdm.data.db.AppDatabase
import com.example.projecttdm.data.endpoint.ApiClient
import com.example.projecttdm.data.endpoint.AppointmentEndPoint
import com.example.projecttdm.data.endpoint.AuthEndPoint
import com.example.projecttdm.data.endpoint.BookAppointmentEndPoint
import com.example.projecttdm.data.endpoint.DoctorEndPoint
import com.example.projecttdm.data.endpoint.FavoriteEndPoint
import com.example.projecttdm.data.endpoint.NotificationEndPoint
import com.example.projecttdm.data.endpoint.PrescriptionEndPoint
import com.example.projecttdm.data.endpoint.ReviewEndPoint
import com.example.projecttdm.data.endpoint.SpecialtyEndPoint
import com.example.projecttdm.data.endpoint.UserEndPoint
import com.yourapp.data.endpoint.DoctorScheduleApi


@RequiresApi(Build.VERSION_CODES.O)
object RepositoryHolder {


    private lateinit var database: AppDatabase

    fun init(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "tdm_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    val authRepository by lazy {
        AuthRepository(ApiClient.create(AuthEndPoint::class.java))
    }
    val UserRepository by lazy {
        UserRepository(ApiClient.create(UserEndPoint::class.java))
    }
    val profileRepository by lazy {
        ProfileRepository(ApiClient.create(UserEndPoint::class.java))
    }
    val prescriptionRepository by lazy {
        PrescriptionRepository(ApiClient.create(PrescriptionEndPoint::class.java))
    }
    val specialtyRepository by lazy {
        SpecialtyRepository(ApiClient.create(SpecialtyEndPoint::class.java))
    }
    val doctorRepository by lazy {
        DoctorRepository(ApiClient.create(DoctorEndPoint::class.java), database)
    }
    val reviewRepository by lazy {
        ReviewRepository(ApiClient.create(ReviewEndPoint::class.java))
    }
    val appointmentRepository by lazy {
        AppointmentRepository(ApiClient.create(AppointmentEndPoint::class.java), database)
    }
    val bookAppointmentRepository by lazy {
        BookAppointmentRepository(ApiClient.create(BookAppointmentEndPoint::class.java))
    }
    val doctorScheduleRepository by lazy {
        DoctorScheduleRepository(ApiClient.create(DoctorScheduleApi::class.java))
    }
    val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepository(ApiClient.create(FavoriteEndPoint::class.java))
    }
    val notificationRepository: NotificationRepository by lazy {
        NotificationRepository(ApiClient.create(NotificationEndPoint::class.java))
    }



}