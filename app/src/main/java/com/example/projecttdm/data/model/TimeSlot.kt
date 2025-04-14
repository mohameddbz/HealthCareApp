package com.example.projecttdm.data.model

import java.time.LocalTime

data class TimeSlot(
    val time: LocalTime,
    val isAvailable: Boolean = true
)
