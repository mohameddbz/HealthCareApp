package com.example.projecttdm.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
object AppointmentData {
    // List of available time slots for appointments
    val availableTimeSlots = listOf(
        LocalTime.of(8, 0),
        LocalTime.of(8, 30),
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

    // Initially unavailable time slots (fixed unavailability)
    private val initiallyUnavailableSlots = listOf(
        LocalTime.of(10, 0),
        LocalTime.of(11, 30),
        LocalTime.of(14, 30)
    )

    // Dynamic booking tracking
    private val bookedTimeSlots = mutableSetOf<LocalTime>().apply {
        // Add initially unavailable slots to booked slots
        addAll(initiallyUnavailableSlots)
    }

    // Function to check if a time slot is available
    fun isTimeSlotAvailable(time: LocalTime): Boolean {
        return time in availableTimeSlots && time !in bookedTimeSlots
    }

    // Function to book a time slot
    fun bookTimeSlot(time: LocalTime) {
        bookedTimeSlots.add(time)
    }

    // Function to release a time slot
    fun releaseTimeSlot(time: LocalTime) {
        // Only release if it's not in the initially unavailable slots
        if (time !in initiallyUnavailableSlots) {
            bookedTimeSlots.remove(time)
        }
    }

    // Get all available time slots for a specific day
    fun getAvailableTimeSlotsForDay(): List<LocalTime> {
        return availableTimeSlots.filter { time -> time !in bookedTimeSlots }
    }
}