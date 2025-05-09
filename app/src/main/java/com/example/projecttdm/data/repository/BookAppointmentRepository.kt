package com.example.projecttdm.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.endpoint.BookAppointmentEndPoint
import com.example.projecttdm.data.model.AppointementResponse
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentRequest
import com.example.projecttdm.data.model.AppointmentSlot
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate


class BookAppointmentRepository (private  val endpoint: BookAppointmentEndPoint) {

    // In-memory storage for appointments
    private val _slots = MutableStateFlow<List<AppointmentSlot>>(emptyList())
    val slots: StateFlow<List<AppointmentSlot>> = _slots.asStateFlow()


    fun bookAppointment(request: AppointmentRequest): Flow<UiState<AppointementResponse>> = flow {
        emit(UiState.Loading)
        try {
            println("Booking appointment: $request")
            val response = endpoint.bookAppointment(request)
            println("Appointment booked successfully: $response")
            emit(UiState.Success(response))
        } catch (e: Exception) {
            println("Error booking appointment: ${e.message}")
            e.printStackTrace()
            emit(UiState.Error(e.message ?: "Unknown error occurred"))
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getSlotsByDoctorIdAndDate(doctorId: String, workingDate: LocalDate): Flow<UiState<List<AppointmentSlot>>> = flow {
        println("Fetching appointment slots with doctor ID: $doctorId and date: $workingDate")
        emit(UiState.Loading)
        try {
            val response = endpoint.getSlotsByDoctorIdAndDate(doctorId, workingDate)
            println("Successfully fetched appointment slots: $response")
            emit(UiState.Success(response))
        } catch (e: Exception) {
            println("Error fetching appointment slots: ${e.message}")
            e.printStackTrace()
            emit(UiState.Error(e.message ?: "Unknown error occurred"))
        }
    }




}