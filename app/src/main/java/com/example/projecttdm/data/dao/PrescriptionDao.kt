package com.example.projecttdm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.projecttdm.data.entity.PrescriptionEntity

@Dao
interface PrescriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: PrescriptionEntity)

    @Query("SELECT * FROM prescriptions")
    suspend fun getAllPrescriptions(): List<PrescriptionEntity>

    @Transaction
    @Query("SELECT * FROM prescriptions WHERE isSynced = 0")
    suspend fun getUnsyncedPrescriptionsWithMedications(): List<PrescriptionEntity>

    @Query("DELETE FROM prescriptions WHERE id = :prescriptionId")
    suspend fun deletePrescriptionWithMedications(prescriptionId: Int)
}