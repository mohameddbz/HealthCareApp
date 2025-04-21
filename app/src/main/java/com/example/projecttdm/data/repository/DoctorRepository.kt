package com.example.projecttdm.data.repository

import com.example.projecttdm.R
import com.example.projecttdm.data.local.DoctorData
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty

class DoctorRepository {
    fun getTopDoctors(): List<Doctor> = DoctorData.listDcctors

    fun  getDetailDoctorById(doctorId :String): Doctor = Doctor(
        id = "dr-jenny-watson",
        name = "Dr. Jenny Watson",
        specialty = Specialty(id = "12", name = "Ophtalmologue"),
        hospital = "Christ Hospital",
        hospitalLocation = "London, UK",
        patients = 5000,
        yearsExperience = 10,
        rating = 4.8f,
        reviewCount = 4942,
        about = "Dr. Jenny Watson is the top most Immunologists specialist in Christ Hospital at London. She achieved several awards for her wonderful contribution in medical field. She is available for private consultation.",
        workingHours = "Monday - Friday, 08.00 AM - 20.00 PM",
        imageResId = R.drawable.doctor_image2,
    )

    fun getDoctors(): List<Doctor> = DoctorData.listDcctors

    fun getDoctorById(id: String): Doctor? {
        return DoctorData.listDcctors.find { it.id == id }
    }
}