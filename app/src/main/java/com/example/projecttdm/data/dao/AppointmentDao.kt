package com.example.projecttdm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projecttdm.data.entity.AppointmentEntity

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointments")
    suspend fun getAllAppointments(): List<AppointmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentEntity>)

    @Query("DELETE FROM appointments")
    suspend fun clearAppointments()

    @Query("DELETE FROM appointments WHERE id IN (:appointmentIds)")
    suspend fun deleteAppointments(appointmentIds: List<String>)

}