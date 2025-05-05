package com.example.projecttdm.data.repository


import com.example.projecttdm.data.endpoint.ApiClient
import com.example.projecttdm.data.endpoint.DoctorEndPoint
import com.example.projecttdm.data.local.DoctorData
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DoctorRepository() {

    private val endpoint = ApiClient.create(DoctorEndPoint::class.java)


     fun getTopDoctors(): Flow<UiState<List<Doctor>>> = flow {
        emit(UiState.Loading) // Emit loading first
        try {
            val doctorsList = endpoint.getDoctors() // Fetch data
            emit(UiState.Success(doctorsList))      // Emit success
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Unknown error")) // Emit error
        }
    }

    fun getDetailDoctorById(doctorId: String): Flow<UiState<Doctor>> = flow {
        emit(UiState.Loading)
        try {
            // Make network call directly on IO dispatcher
            val doctor = endpoint.getDoctorById(doctorId)
            emit(UiState.Success(doctor))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    fun getDoctors(): List<Doctor> = DoctorData.listDcctors

    fun getDoctorById(id: String): Doctor? {
        return DoctorData.listDcctors.find { it.id == id }
    }
}