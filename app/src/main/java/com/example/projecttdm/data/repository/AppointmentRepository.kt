package com.example.projecttdm.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.dao.AppointmentDao
import com.example.projecttdm.data.db.AppDatabase
import com.example.projecttdm.data.endpoint.AppointmentEndPoint
import com.example.projecttdm.data.endpoint.UserEndPoint
import com.example.projecttdm.data.entity.toEntity
import com.example.projecttdm.data.entity.toModel
import com.example.projecttdm.data.entity.toQRCodeData
import com.example.projecttdm.data.local.AppointmentData
import com.example.projecttdm.data.local.AppointmentsData
import com.example.projecttdm.data.model.AppointementResponse
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentReviewData
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.DateRequest
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
import java.util.Date
import java.util.UUID

class AppointmentRepository(private  val endpoint: AppointmentEndPoint ,private val localDB: AppDatabase) {

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

  /*  suspend fun getQRCodeForAppointment(appointmentId: String): Result<QRCodeData> {
        return try {
            val response = endpoint.getQRCodeForAppointment(appointmentId)
            println("--------------- ${response}")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    */

    suspend fun getQRCodeForAppointment(appointmentId: String): Result<QRCodeData> {
        // D'abord, on cherche en local
        val localData = localDB.qrCodeDataDao().getByAppointmentId(appointmentId)
        if (localData != null) {
            return Result.success(localData.toQRCodeData())
        }

        // Sinon, on fait la requête réseau, et on sauvegarde en local
        return try {
            val response = endpoint.getQRCodeForAppointment(appointmentId)
            localDB.qrCodeDataDao().insert(response.toEntity(appointmentId))
            Result.success(response)
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


 /*   @RequiresApi(Build.VERSION_CODES.O)
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
    } */
  /*
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshAppointments(): Result<Boolean> {
        return try {
            val appointmentList = endpoint.getAppointmentsByPatientId()
           println("-----------------------------------------------")
            println("-----------------------------------------------")
            println("-----------------------------------------------")
            println("-----------------------------------------------")
            println(appointmentList)
            // update in-memory
            _appointments.value = appointmentList
            println("-----------------------------------------------")
            println("-----------------------------------------------")
            println("-----------------------------------------------")
            println("-----------------------------------------------")


            // save to local DB
            localDB.appointmentDao().clearAppointments()
            localDB.appointmentDao().insertAppointments(appointmentList.map { it.toEntity() })

            Result.success(true)
        } catch (e: Exception) {
            println("Remote fetch failed: ${e.message}, using local data")

            return try {
                val localData = localDB.appointmentDao().getAllAppointments().map { it.toModel() }
                _appointments.value = localData
                Result.success(true)
            } catch (localException: Exception) {
                println("Local fallback failed: ${localException.message}")
                Result.failure(localException)
            }
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshAppointments(): Result<Boolean> {
        return try {
            val remoteAppointments = endpoint.getAppointmentsByPatientId()
            _appointments.value = remoteAppointments

            val localAppointments = localDB.appointmentDao().getAllAppointments()
            val remoteMap = remoteAppointments.associateBy { it.id }
            val localMap = localAppointments.associateBy { it.id }

            val toInsertOrUpdate = remoteAppointments.filter { remote ->
                val local = localMap[remote.id]
                local == null || local.status != remote.status.toString() // comparer le contenu
            }

            val toDelete = localAppointments.filter { local ->
                !remoteMap.containsKey(local.id)
            }

            localDB.appointmentDao().deleteAppointments(toDelete.map { it.id })
            localDB.appointmentDao().insertAppointments(toInsertOrUpdate.map { it.toEntity() })

            Result.success(true)
        } catch (e: Exception) {
            println("Remote fetch failed: ${e.message}, using local data")
            return try {
                val localData = localDB.appointmentDao().getAllAppointments().map { it.toModel() }
                _appointments.value = localData
                Result.success(true)
            } catch (localException: Exception) {
                println("Local fallback failed: ${localException.message}")
                Result.failure(localException)
            }
        }
    }


    fun getUpcomingAppointment(): Flow<UiState<Appointment>> = flow {
        emit(UiState.Loading)
        try {
            val appointment = endpoint.getfirstUpcomingAppointment()
            println("dasdasdad55555555555555555 ${appointment}")
            if (appointment.status == AppointmentStatus.CONFIRMED) {
                emit(UiState.Success(appointment))
            } else {
                emit(UiState.Error("No upcoming pending appointments found"))
            }
        } catch (e: Exception) {
            emit(UiState.Error("Error: ${e.localizedMessage ?: "Unexpected error occurred"}"))
        }
    }


    fun getAppointmentDetailsById(appointmentId: String): Flow<UiState<AppointmentReviewData>> = flow {
        emit(UiState.Loading)
        try {
            val appointmentDetails = endpoint.getAppointmentDetails(appointmentId)
            emit(UiState.Success(appointmentDetails))
        } catch (e: Exception) {
            emit(UiState.Error("Error: ${e.localizedMessage ?: "Unexpected error occurred"}"))
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeSlotAvailable(time: LocalTime): Boolean {
        return AppointmentData.isTimeSlotAvailable(time)
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

//    suspend fun cancelAppointment(appointmentId: String): Result<Unit> {
//        return try {
//            delay(500) // Simule un petit délai
//            _appointments.value = _appointments.value.map { appointment ->
//                if (appointment.id == appointmentId) {
//                    appointment.copy(status = AppointmentStatus.CANCELLED)
//                } else {
//                    appointment
//                }
//            }
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }

    fun getPatientAppointments(patientId: String): List<Appointment> {
        return _appointments.value.filter { it.patientId == patientId }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getSampleAppointments(): List<Appointment>{
//        return AppointmentsData.Appointments;
//    }
//
//    fun getDoctorAppointments(doctorId: String): List<Appointment> {
//        return _appointments.value.filter { it.doctorId == doctorId }
//    }

    suspend fun cancelAppointment(appointmentId: String): UiState<AppointementResponse> {
        return try {
            val response = endpoint.cancelAppointment(appointmentId)
            UiState.Success(response)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "An unknown error occurred")
        }
    }

    suspend fun confirmAppointment(appointmentId: String): UiState<AppointementResponse> {
        return try {
            val response = endpoint.confirmAppointement(appointmentId)
            UiState.Success(response)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "An unknown error occurred")
        }
    }

    suspend fun getAppointmentOfDoctorOfDay(date: Date) :NextAppointementsResponse{
        val localDate = DateRequest(date)
        return endpoint.getAppointmentOfDoctorOfDay(localDate)
    }





}