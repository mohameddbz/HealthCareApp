package com.example.projecttdm.ui.patient

sealed class PatientRoutes(val route: String) {
    object BookAppointment : PatientRoutes("bookAppointment")
    object PatientDetails : PatientRoutes("patientDetails")
    object PinVerification : PatientRoutes("pinVerification")
    object Success : PatientRoutes("success")
    object Failure : PatientRoutes("failure")
    object NotificationScreen : PatientRoutes("notifications")
    object HomeScreen : PatientRoutes("home")
    object topDoctors : PatientRoutes("doctor_top_doctors")
    object searchDoctor : PatientRoutes("search_doctor")
}