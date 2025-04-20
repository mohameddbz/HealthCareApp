package com.example.projecttdm.data.local

import com.example.projecttdm.data.model.RescheduleReason

object RescheduleReasonData {
    val rescheduleReasons = listOf(
        RescheduleReason("schedule_clash", "I'm having a schedule clash"),
        RescheduleReason("unavailable", "I'm not available on schedule"),
        RescheduleReason("activity", "I have a activity that can't be left behind"),
        RescheduleReason("privacy", "I don't want to tell"),
        RescheduleReason("others", "Others")
    )
}