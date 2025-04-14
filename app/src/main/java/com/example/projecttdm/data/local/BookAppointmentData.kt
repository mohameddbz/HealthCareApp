package com.example.projecttdm.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

object BookAppointmentData {
    // Simple list of available time slots
    @RequiresApi(Build.VERSION_CODES.O)
    val availableTimeSlots = listOf(
        LocalTime.of(9, 0),
        LocalTime.of(9, 30),
        LocalTime.of(10, 0),
        LocalTime.of(10, 30),
        LocalTime.of(11, 0),
        LocalTime.of(11, 30),
        LocalTime.of(14, 0),
        LocalTime.of(14, 30),
        LocalTime.of(15, 0),
        LocalTime.of(15, 30),
        LocalTime.of(16, 0),
        LocalTime.of(16, 30)
    )

    // List of unavailable time slots (for demonstration purposes)
    @RequiresApi(Build.VERSION_CODES.O)
    val unavailableTimeSlots = listOf(
        LocalTime.of(10, 0),
        LocalTime.of(11, 30),
        LocalTime.of(14, 30)
    )

    // Function to check if a time slot is available
    @RequiresApi(Build.VERSION_CODES.O)
    fun isTimeSlotAvailable(time: LocalTime): Boolean {
        return time in availableTimeSlots && time !in unavailableTimeSlots
    }
}