package com.example.projecttdm.ui.doctor

import com.example.projecttdm.ui.patient.PatientRoutes

sealed class DoctorRoutes(val route: String) {
    object HomeScreen : DoctorRoutes("home")
    object AppointmentValidationScreen : DoctorRoutes("appointment_validation")
    object SuccessConfirm : DoctorRoutes("succes_confirm")
    object SuccesRefuse : DoctorRoutes("succes_refuse")
    object QrScanner : DoctorRoutes("qr_scanner")
    object AppointmentOfWeek : DoctorRoutes("AppointmentOfWeek")
    object DOCTOR_SCHEDULE : DoctorRoutes("DOCTOR_SCHEDULE")
}