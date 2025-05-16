package com.example.projecttdm.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import java.time.LocalDate

object SampleDataProvider {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAppointmentsByDate(date: LocalDate = LocalDate.now()): List<Appointment> {
        return AppointmentsData.Appointments
            .filter { it.date == date && it.status != AppointmentStatus.CANCELLED }
            .sortedBy { it.time }
    }
}