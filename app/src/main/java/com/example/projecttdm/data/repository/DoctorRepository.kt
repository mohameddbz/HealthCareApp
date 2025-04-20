package com.example.projecttdm.data.repository

import com.example.projecttdm.data.local.DoctorData
import com.example.projecttdm.data.model.Doctor

class DoctorRepository {
    fun getTopDoctors(): List<Doctor> = DoctorData.listDcctors

    fun getDoctors(): List<Doctor> = DoctorData.listDcctors

    fun getDoctorById(id: String): Doctor? {
        return DoctorData.listDcctors.find { it.id == id }
    }
}