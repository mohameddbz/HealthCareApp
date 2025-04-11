package com.example.projecttdm.ui.patient

sealed class PatientRoutes(val route: String) {
    object BookAppointment : PatientRoutes("bookAppointment")
    object PatientDetails : PatientRoutes("patientDetails")
    object PinVerification : PatientRoutes("pinVerification")
    object Success : PatientRoutes("success")
    object Failure : PatientRoutes("failure")
    object NotificationScreen : PatientRoutes("notifications")
}