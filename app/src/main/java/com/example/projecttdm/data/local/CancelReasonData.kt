package com.example.projecttdm.data.local

import com.example.projecttdm.data.model.RescheduleReason

object CancelReasonData {
    val cancelReasons = listOf(
        RescheduleReason("1", "I want to chabge to another doctor"),
        RescheduleReason("2", "I want to change package"),
        RescheduleReason("3", "I don't want to consult"),
        RescheduleReason("4", "I have recovered from the disease"),
        RescheduleReason("5", "I have found a suitable medicine"),
        RescheduleReason("6", "I just want to cancel"),
        RescheduleReason("7", "I don't want to tell"),
    )
}