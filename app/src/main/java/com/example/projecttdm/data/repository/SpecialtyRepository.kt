package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.ApiClient
import com.example.projecttdm.data.endpoint.DoctorEndPoint
import com.example.projecttdm.data.endpoint.SpecialtyEndPoint
import com.example.projecttdm.data.local.DoctorData
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SpecialtyRepository {

    private val endpoint = ApiClient.create(SpecialtyEndPoint::class.java)
    private val allOption = Specialty(id = "all", name = "All")

    // Get all specialties including the "All" option
    fun getAllSpecialtiesWithAll(): List<Specialty> = listOf(allOption) + DoctorData.specialties

    fun getAllSpecialties(): Flow<UiState<List<Specialty>>> = flow {
        emit(UiState.Loading)
        try {
            val specialtes = endpoint.getSpecialties()
            println("ddsasds${specialtes}")
            emit(UiState.Success(specialtes))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }

    //fun getAllSpecialties(): List<Specialty> = DoctorData.specialties

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