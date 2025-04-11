package com.example.projecttdm.data.repository

import com.example.projecttdm.data.local.DoctorData
import com.example.projecttdm.data.model.Specialty

class SpecialtyRepository {
    private val allOption = Specialty(id = "all", name = "All")

    // Get all specialties including the "All" option
    fun getAllSpecialtiesWithAll(): List<Specialty> = listOf(allOption) + DoctorData.specialties

    // Get only actual specialties without the "All" option
    fun getAllSpecialties(): List<Specialty> = DoctorData.specialties

    // Get specialty by ID
    fun getSpecialtyById(id: String): Specialty? {
        if (id == "all") return allOption
        return DoctorData.specialties.find { it.id == id }
    }

    // Get specialty by name
    fun getSpecialtyByName(name: String): Specialty? {
        if (name == "All") return allOption
        return DoctorData.specialties.find { it.name == name }
    }

    // Get the "All" specialty
    fun getAllSpecialty(): Specialty = allOption
}