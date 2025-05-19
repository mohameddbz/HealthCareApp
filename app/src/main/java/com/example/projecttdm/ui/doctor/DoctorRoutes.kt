package com.example.projecttdm.ui.doctor

sealed class DoctorRoutes(val route: String) {
    object HomeScreen : DoctorRoutes("home")
<<<<<<< Updated upstream
=======
    object AppointmentValidationScreen : DoctorRoutes("appointment_validation")
    object SuccessConfirm : DoctorRoutes("succes_confirm")
    object SuccesRefuse : DoctorRoutes("succes_refuse")
    object QrScanner : DoctorRoutes("qr_scanner")
    object AppointmentOfWeek : DoctorRoutes("AppointmentOfWeek")
    object DOCTOR_SCHEDULE : DoctorRoutes("DOCTOR_SCHEDULE")
>>>>>>> Stashed changes
}