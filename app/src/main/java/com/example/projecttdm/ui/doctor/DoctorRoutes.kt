package com.example.projecttdm.ui.doctor

sealed class DoctorRoutes(val route: String) {
    object HomeScreen : DoctorRoutes("home")
    object AppointmentValidationScreen : DoctorRoutes("appointment_validation")
    object SuccessConfirm : DoctorRoutes("succes_confirm")
    object SuccesRefuse : DoctorRoutes("succes_refuse")
}