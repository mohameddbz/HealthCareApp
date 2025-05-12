package com.example.projecttdm.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.endpoint.AppointmentEndPoint
import com.example.projecttdm.data.endpoint.UserEndPoint
import com.example.projecttdm.data.local.AppointmentData
import com.example.projecttdm.data.local.AppointmentsData
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.NextAppointementResponse
import com.example.projecttdm.data.model.NextAppointementsResponse
import com.example.projecttdm.data.model.QRCodeData
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class AppointmentRepository(private  val endpoint: AppointmentEndPoint) {

    // In-memory storage for appointments
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()


    fun getAppointments(): Flow<List<Appointment>> = appointments

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAppointmentsByStatus(status: AppointmentStatus): Flow<List<Appointment>> {

        return appointments.map { list ->
            println("Filtering appointments by status: $status")
            val filtered = list.filter { appt ->
                println("Checking appointment: ${appt.id} with status: ${appt.status}")
                appt.status == status
            }
            val sorted = filtered.sortedWith(compareBy({ it.date }, { it.time }))
            println("Sorted appointments: ${sorted.map { it.id }}")
            sorted
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getAppointmentById(appointmentId: String): Flow<UiState<Appointment>> = flow {
        println("Fetching appointment with ID: $appointmentId")
        emit(UiState.Loading)
        try {
            val response = endpoint.getAppointmentById(appointmentId)
            println("Successfully fetched appointment: $response")
            emit(UiState.Success(response))
        } catch (e: Exception) {
            println("Error fetching appointment: ${e.message}")
            e.printStackTrace()
            emit(UiState.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getNextAppointmentForDoctor() : NextAppointementResponse {
        return  endpoint.getNextAppointmentForDoctor()
    }

    suspend fun getTodaysAppointmentsForDoctor():NextAppointementsResponse {
        return  endpoint.getTodaysAppointmentsForDoctor()
    }

    suspend fun getAppointmentQRCode(appointmentId: String): Result<QRCodeData> {
        return try {
            delay(500) // Simulate network delay for QR code generation
            val appointment = _appointments.value.find { it.id == appointmentId }
                ?: return Result.failure(Exception("Appointment not found"))

            // Generate QR code data for the appointment
            val qrCodeData = QRCodeData(
                id = appointmentId,
                content = "APPT:${appointment.id}:${appointment.doctorId}:${appointment.date}:${appointment.time}",
                timestamp = System.currentTimeMillis()
            )

            Result.success(qrCodeData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun searchAppointments(query: String, doctors: List<Doctor>): Flow<List<Appointment>> {
        return appointments.map { appointmentList ->
            if (query.isBlank()) appointmentList
            else appointmentList.filter { appt ->
                val doctorName = doctors.find { it.id == appt.doctorId }?.name ?: ""
                doctorName.contains(query, ignoreCase = true) ||
                        appt.reason.contains(query, ignoreCase = true)
            }
        }
    }

    suspend fun scheduleAppointment(appointment: Appointment): Result<String> {
        return try {
            delay(1000) // Simulate network or database delay

            val conflict = _appointments.value.any {
                it.date == appointment.date &&
                        it.time == appointment.time &&
                        it.doctorId == appointment.doctorId &&
                        it.status != AppointmentStatus.CANCELLED
            }

            if (conflict) {
                Result.failure(Exception("This time slot is already booked"))
            } else {
                _appointments.value = _appointments.value + appointment
                Result.success(appointment.id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun updateAppointmentNotes(appointmentId: String, notes: String): Result<Unit> {
        return try {
            _appointments.value = _appointments.value.map {
                if (it.id == appointmentId) it.copy(reason = notes) else it
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshAppointments(): Result<Boolean> {
        return try {
            val appointmentList = endpoint.getAppointmentsByPatientId()
            _appointments.value = appointmentList
            Result.success(true)
        } catch (e: Exception) {
            println("Error in refreshAppointments: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun getUpcomingAppointment(): Flow<UiState<Appointment>> = flow {
        emit(UiState.Loading)

        try {
            val appointment = endpoint.getfirstUpcomingAppointment()

            if (appointment.status == AppointmentStatus.PENDING) {
                emit(UiState.Success(appointment))
            } else {
                emit(UiState.Error("No upcoming pending appointments found"))
            }
        } catch (e: Exception) {
            emit(UiState.Error("Error: ${e.localizedMessage ?: "Unexpected error occurred"}"))
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeSlotAvailable(time: LocalTime): Boolean {
        return AppointmentData.isTimeSlotAvailable(time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bookAppointment(
        patientId: String,
        doctorId: String,
        date: LocalDate,
        time: LocalTime,
        reason: String = ""
    ): Appointment {
        val appointment = Appointment(
            id = UUID.randomUUID().toString(),
            patientId = patientId,
            doctorId = doctorId,
            date = date,
            time = time,
            status = AppointmentStatus.PENDING,
            reason = reason
        )


        _appointments.value = _appointments.value + appointment
        AppointmentData.bookTimeSlot(time)

        return appointment
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun rescheduleAppointment(
        appointmentId: String,
        newDate: LocalDate,
        newTime: LocalTime
    ): Boolean {
        val appointment = _appointments.value.find { it.id == appointmentId } ?: return false

        // Release the old time slot
        AppointmentData.releaseTimeSlot(appointment.time)

        // Book the new time slot
        AppointmentData.bookTimeSlot(newTime)

        // Update the appointment
        _appointments.value = _appointments.value.map {
            if (it.id == appointmentId) {
                it.copy(
                    date = newDate,
                    time = newTime,
                    status = AppointmentStatus.RESCHEDULED
                )
            } else {
                it
            }
        }

        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAvailableTimeSlots(date: LocalDate): List<LocalTime> {
        return AppointmentData.availableTimeSlots.filter { time ->
            AppointmentData.isTimeSlotAvailable(time)
        }
    }

    suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
        return try {
            delay(500) // Simule un petit délai
            _appointments.value = _appointments.value.map { appointment ->
                if (appointment.id == appointmentId) {
                    appointment.copy(status = AppointmentStatus.CANCELLED)
                } else {
                    appointment
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPatientAppointments(patientId: String): List<Appointment> {
        return _appointments.value.filter { it.patientId == patientId }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSampleAppointments(): List<Appointment>{
        return AppointmentsData.Appointments;
    }

    fun getDoctorAppointments(doctorId: String): List<Appointment> {
        return _appointments.value.filter { it.doctorId == doctorId }
    }

}