package com.example.projecttdm.ui.doctor

sealed class DoctorRoutes(val route: String) {
    object HomeScreen : DoctorRoutes("home")
    object AppointmentOfWeek : DoctorRoutes("AppointmentOfWeek")
}