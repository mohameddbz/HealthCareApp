package com.example.projecttdm.data.local



import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.Patient
import java.time.LocalDate
import java.time.LocalTime

/**
 * Sample data for appointments
 */
object AppointmentsData {
    @RequiresApi(Build.VERSION_CODES.O)
    val Appointments = listOf(
        Appointment(
            id = "1",
            patientId = "1",
            doctorId = "101",
            date = LocalDate.now(),
            time = LocalTime.of(11, 0),
            status = AppointmentStatus.PENDING,
            reason = "Annual checkup"
        ),
        Appointment(
            id = "2",
            patientId = "1",
            doctorId = "101",
            date = LocalDate.now(),
            time = LocalTime.of(13, 30),
            status = AppointmentStatus.PENDING,
            reason = "Follow-up consultation"
        ),
        Appointment(
            id = "3",
            patientId = "2",
            doctorId = "102",
            date = LocalDate.now(),
            time = LocalTime.of(11, 30),
            status = AppointmentStatus.PENDING,
            reason = "Routine examination"
        ),
        Appointment(
            id = "4",
            patientId = "3",
            doctorId = "103",
            date = LocalDate.now(),
            time = LocalTime.of(13, 0),
            status = AppointmentStatus.PENDING,
            reason = "Health concern"
        ),
        Appointment(
            id = "5",
            patientId = "2",
            doctorId = "102",
            date = LocalDate.now().plusDays(1),
            time = LocalTime.of(10, 0),
            status = AppointmentStatus.PENDING,
            reason = "Lab results discussion"
        ),
        Appointment(
            id = "6",
            patientId = "3",
            doctorId = "101",
            date = LocalDate.now().plusDays(2),
            time = LocalTime.of(14, 0),
            status = AppointmentStatus.PENDING,
            reason = "Prescription renewal"
        )
    )

    // Available time slots for booking
    @RequiresApi(Build.VERSION_CODES.O)
    val availableTimeSlots = listOf(
        LocalTime.of(9, 0),
        LocalTime.of(9, 30),
        LocalTime.of(10, 0),
        LocalTime.of(10, 30),
        LocalTime.of(11, 0),
        LocalTime.of(11, 30),
        LocalTime.of(13, 0),
        LocalTime.of(13, 30),
        LocalTime.of(14, 0),
        LocalTime.of(14, 30),
        LocalTime.of(15, 0),
        LocalTime.of(15, 30),
        LocalTime.of(16, 0),
        LocalTime.of(16, 30)
    )

    private val bookedSlots = mutableSetOf<LocalTime>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeSlotAvailable(time: LocalTime): Boolean {
        return time in availableTimeSlots && time !in bookedSlots
    }

    fun bookTimeSlot(time: LocalTime) {
        bookedSlots.add(time)
    }

    fun releaseTimeSlot(time: LocalTime) {
        bookedSlots.remove(time)
    }
}



