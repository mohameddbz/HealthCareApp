package com.example.projecttdm.data.repository

import com.example.projecttdm.data.model.CreateSchedulesByDayRequest
import com.example.projecttdm.data.model.Docschedule
import com.yourapp.data.endpoint.DoctorScheduleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DoctorScheduleRepository @Inject constructor(
    private val api: DoctorScheduleApi
) {
    suspend fun createSchedulesByDay(
        dayName: String,
        doctorId: Int,
        startTime: String,
        endTime: String,
        appointmentDuration: Int
    ): Result<List<Docschedule>> {
        return withContext(Dispatchers.IO) {
            try {
                val request = CreateSchedulesByDayRequest(
                    doctorId = doctorId,
                    startTime = startTime,
                    endTime = endTime,
                    appointmentDuration = appointmentDuration
                )

                val response = api.createSchedulesByDay(dayName, request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception("Failed to create schedules: ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}