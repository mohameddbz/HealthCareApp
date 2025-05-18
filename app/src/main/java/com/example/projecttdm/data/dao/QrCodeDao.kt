package com.example.projecttdm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projecttdm.data.entity.QRCodeDataEntity

@Dao
interface QRCodeDao {

    @Query("SELECT * FROM qr_code_data WHERE appointmentId = :appointmentId LIMIT 1")
    suspend fun getByAppointmentId(appointmentId: String): QRCodeDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(qrCodeDataEntity: QRCodeDataEntity)
}
