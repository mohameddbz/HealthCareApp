package com.example.projecttdm.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.local.AppointmentData
import com.example.projecttdm.data.local.BookAppointmentData
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.Doctor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class AppointmentRepository {
    // In-memory storage for appointments
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    fun getAppointments(): Flow<List<Appointment>> = appointments

    fun getAppointmentsByStatus(status: AppointmentStatus): Flow<List<Appointment>> {
        return appointments.map { it.filter { appt -> appt.status == status }.sortedWith(compareBy({ it.date }, { it.time }))}
    }

    fun getAppointmentById(appointmentId: String): Flow<Appointment?> {
        return appointments.map { it.find { appt -> appt.id == appointmentId } }
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

    suspend fun rescheduleAppointment(
        appointmentId: String,
        newDate: LocalDate,
        newTime: LocalTime
    ): Result<Unit> {
        return try {
            delay(800)

            val appointment = _appointments.value.find { it.id == appointmentId }
                ?: return Result.failure(Exception("Appointment not found"))

            val conflict = _appointments.value.any {
                it.id != appointmentId &&
                        it.date == newDate &&
                        it.time == newTime &&
                        it.doctorId == appointment.doctorId &&
                        it.status != AppointmentStatus.CANCELLED
            }

            if (conflict) {
                Result.failure(Exception("This new time slot is already booked"))
            } else {
                _appointments.value = _appointments.value.map {
                    if (it.id == appointmentId) it.copy(date = newDate, time = newTime) else it
                }
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAppointmentNotes(appointmentId: String, notes: String): Result<Unit> {
        return try {
            _appointments.value = _appointments.value.map {
                if (it.id == appointmentId) it.copy(notes = notes) else it
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


     @RequiresApi(Build.VERSION_CODES.O)
     suspend fun refreshAppointments(): Result<Unit> {
        return try {
            delay(1000)
            _appointments.value = getSampleAppointments()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeSlotAvailable(time: LocalTime): Boolean {
        return BookAppointmentData.isTimeSlotAvailable(time)
    }


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

        return appointment
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getAvailableTimeSlots(date: LocalDate): List<LocalTime> {
        return BookAppointmentData.availableTimeSlots.filter { time ->
            BookAppointmentData.isTimeSlotAvailable(time)
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
        return AppointmentData.Appointments;
    }



}