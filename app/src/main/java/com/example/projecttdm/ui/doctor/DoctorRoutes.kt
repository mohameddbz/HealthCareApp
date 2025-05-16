package com.example.projecttdm.ui.doctor

import com.example.projecttdm.ui.patient.PatientRoutes

sealed class DoctorRoutes(val route: String) {
    object HomeScreen : DoctorRoutes("home")
    object AppointmentValidationScreen : DoctorRoutes("appointment_validation")
    object SuccessConfirm : DoctorRoutes("succes_confirm")
    object SuccesRefuse : DoctorRoutes("succes_refuse")
    object AppointmentOfWeek : DoctorRoutes("AppointmentOfWeek") {
        const val routeWithArgs = "AppointmentOfWeek/{doctorId}"
        fun createRoute(doctorId: String) = "AppointmentOfWeek/$doctorId"
    }
}