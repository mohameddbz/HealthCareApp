package com.example.projecttdm.data.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val date: String,
    val time: String,
    val doctorId: String,
    val patientId: String,
    val reason: String,
    val status: String










    // ajoute les autres champs nécessaires
)
fun Appointment.toEntity(): AppointmentEntity {
    return AppointmentEntity(
        id = id,
        date = date.toString(),
        time = time.toString(),
        doctorId = doctorId,
        patientId = patientId,
        reason = reason,
        status = status.name
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun AppointmentEntity.toModel(): Appointment {
    return Appointment(
        id = id,
        date = LocalDate.parse(date),
        time = LocalTime.parse(time),
        doctorId = doctorId,
        patientId = patientId,
        reason = reason,
        status = AppointmentStatus.valueOf(status)
    )
}






























































