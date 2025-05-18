package com.example.projecttdm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.example.projecttdm.data.model.QRCodeData

@Entity(
    tableName = "qr_code_data",
    foreignKeys = [
        ForeignKey(
            entity = AppointmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["appointmentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QRCodeDataEntity(
    @PrimaryKey val id: String,
    val appointmentId: String,  // clé étrangère vers Appointment
    val content: String,
    val timestamp: Long,
    val image: String // base64 image
)




fun QRCodeData.toEntity(appointmentId: String): QRCodeDataEntity {
    return QRCodeDataEntity(
        id = this.id,
        appointmentId = appointmentId,
        content = this.content,
        timestamp = this.timestamp,
        image = this.image
    )
}

fun QRCodeDataEntity.toQRCodeData(): QRCodeData {
    return QRCodeData(
        id = this.id,
        content = this.content,
        timestamp = this.timestamp,
        image = this.image
    )
}