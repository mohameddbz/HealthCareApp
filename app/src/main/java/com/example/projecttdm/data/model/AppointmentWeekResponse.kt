package com.example.projecttdm.data.model



data class AppointmentWeekResponse(
    val appointment_id: String,
    val slot_id: String,
    val patient_id: String,
    val status: String,
    val reason: String?,
//    val qr_data: String?,
    val slot_info: SlotInfo?,
    val patient_info: PatientInfo?,
    val doctor_info: DoctorInfo?
)


data class SlotInfo(
    val start_time: String,
    val end_time: String,
    val working_date: String,
    val is_booked: Boolean
)


data class PatientInfo(
    val name: String,
    val email: String?,
    val phone: String?
)


data class DoctorInfo(
    val doctor_id: String,
    val name: String
)