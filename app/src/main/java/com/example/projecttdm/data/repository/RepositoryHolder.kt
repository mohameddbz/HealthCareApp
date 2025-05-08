package com.example.projecttdm.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.endpoint.ApiClient
import com.example.projecttdm.data.endpoint.AppointmentEndPoint
import com.example.projecttdm.data.endpoint.AuthEndPoint
import com.example.projecttdm.data.endpoint.BookAppointmentEndPoint
import com.example.projecttdm.data.endpoint.DoctorEndPoint
import com.example.projecttdm.data.endpoint.PrescriptionEndPoint
import com.example.projecttdm.data.endpoint.ReviewEndPoint
import com.example.projecttdm.data.endpoint.SpecialtyEndPoint
import com.example.projecttdm.data.endpoint.UserEndPoint


@RequiresApi(Build.VERSION_CODES.O)
object RepositoryHolder {

    val authRepository by lazy {
        AuthRepository(ApiClient.create(AuthEndPoint::class.java))
    }
    val UserRepository by lazy {
        UserRepository(ApiClient.create(UserEndPoint::class.java))
    }
    val prescriptionRepository by lazy {
        PrescriptionRepository(ApiClient.create(PrescriptionEndPoint::class.java))
    }
    val specialtyRepository by lazy {
        SpecialtyRepository(ApiClient.create(SpecialtyEndPoint::class.java))
    }
    val doctorRepository by lazy {
        DoctorRepository(ApiClient.create(DoctorEndPoint::class.java))
    }
    val reviewRepository by lazy {
        ReviewRepository(ApiClient.create(ReviewEndPoint::class.java))
    }
    val appointmentRepository by lazy {
        AppointmentRepository(ApiClient.create(AppointmentEndPoint::class.java))
    }
    val bookAppointmentRepository by lazy {
        BookAppointmentRepository(ApiClient.create(BookAppointmentEndPoint::class.java))
    }
}