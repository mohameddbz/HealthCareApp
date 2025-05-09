package com.example.projecttdm.ui.patient

sealed class PatientRoutes(val route: String) {
    object BookAppointment : PatientRoutes("bookAppointment")
    object PatientDetails : PatientRoutes("patientDetails")
    object PinVerification : PatientRoutes("pinVerification")
    object PatientSummary : PatientRoutes("patientSummary")
    object Success : PatientRoutes("success")
    object SuccessCancel : PatientRoutes("successCancel")
    object Failure : PatientRoutes("failure")
    object NotificationScreen : PatientRoutes("notifications")
    object HomeScreen : PatientRoutes("home")
    object topDoctors : PatientRoutes("doctor_top_doctors")
    object searchDoctor : PatientRoutes("search_doctor")
    object Appointment : PatientRoutes("Appointment")
    object AppointmentQR : PatientRoutes("QRcode")
    object AppQR : PatientRoutes("appointmentQR") {
        const val routeWithArgs = "appointmentQR/{appointmentId}"
        fun createRoute(appointmentId: String) = "appointmentQR/$appointmentId"
    }

    object  doctorProfile : PatientRoutes("doctor_profil")
    object RescheduleReason : PatientRoutes("rescheduleAppointmentReason")
    object CancelReason : PatientRoutes("cancelReason")
    object RescheduleAppointment : PatientRoutes("rescheduleAppointment")
    object CancelDialog : PatientRoutes("cancelDialog")
    object Prescription : PatientRoutes("prescription"){
        const val routeWithArgs = "prescription-id/{prescriptionId}"
        fun createRoute(prescriptionId: String) = "prescription-id/$prescriptionId"
    }
    object FavoriteDoctors : PatientRoutes("favorite_doctors")
    object PrescriptionCreate : PatientRoutes("prescriptionCreate")
}