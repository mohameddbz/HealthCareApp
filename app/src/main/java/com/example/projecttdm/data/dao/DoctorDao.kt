package com.example.projecttdm.data.dao

import androidx.room.*
import com.example.projecttdm.data.entity.DoctorEntity

@Dao
interface DoctorDao {

    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctors(): List<DoctorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<DoctorEntity>)

    @Query("DELETE FROM doctors WHERE id IN (:ids)")
    suspend fun deleteDoctorsByIds(ids: List<String>)

    @Query("DELETE FROM doctors")
    suspend fun clearAll()
}
