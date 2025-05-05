package com.example.projecttdm.data.repository

import com.example.projecttdm.R
import com.example.projecttdm.data.local.DoctorData
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty

class DoctorRepository {

    private val patientFavorites = mapOf(
        "patient1" to listOf("1", "3", "5"),
        "patient2" to listOf("2", "4"),
        "patient3" to listOf("1", "2", "3", "4", "5")
    )


    private val defaultPatientId = "patient1"


    fun getDoctors(): List<Doctor> = DoctorData.listDcctors


    fun getTopDoctors(): List<Doctor> = DoctorData.listDcctors


    fun getDoctorById(id: String): Doctor? {
        return DoctorData.listDcctors.find { it.id == id }
    }


    fun getDetailDoctorById(doctorId: String): Doctor {
        val existingDoctor = getDoctorById(doctorId)
        if (existingDoctor != null) {
            return existingDoctor
        }


        return Doctor(
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
    }


    fun getFavoriteDoctors(patientId: String = defaultPatientId): List<Doctor> {
        val favoriteIds = patientFavorites[patientId] ?: emptyList()
        return DoctorData.listDcctors.filter { it.id in favoriteIds }
    }


    fun isFavorite(doctorId: String, patientId: String = defaultPatientId): Boolean {
        val favoriteIds = patientFavorites[patientId] ?: emptyList()
        return doctorId in favoriteIds
    }


    private val currentSessionFavorites = mutableSetOf<String>()


    init {
        patientFavorites[defaultPatientId]?.let {
            currentSessionFavorites.addAll(it)
        }
    }


    fun toggleSessionFavorite(doctorId: String): Boolean {
        return if (doctorId in currentSessionFavorites) {
            currentSessionFavorites.remove(doctorId)
            false
        } else {
            currentSessionFavorites.add(doctorId)
            true
        }
    }

    fun getSessionFavoriteDoctors(): List<Doctor> {
        return DoctorData.listDcctors.filter { it.id in currentSessionFavorites }
    }

    fun isSessionFavorite(doctorId: String): Boolean {
        return doctorId in currentSessionFavorites
    }
}