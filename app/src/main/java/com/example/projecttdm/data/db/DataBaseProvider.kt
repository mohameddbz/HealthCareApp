package com.example.projecttdm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projecttdm.data.dao.AppointmentDao
import com.example.projecttdm.data.dao.DoctorDao
import com.example.projecttdm.data.dao.PrescriptionDao
import com.example.projecttdm.data.dao.QRCodeDao
import com.example.projecttdm.data.entity.AppointmentEntity
import com.example.projecttdm.data.entity.DoctorEntity
import com.example.projecttdm.data.entity.PrescriptionEntity
import com.example.projecttdm.data.entity.QRCodeDataEntity

@Database(entities = [AppointmentEntity::class, QRCodeDataEntity::class ,DoctorEntity::class , PrescriptionEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
    abstract fun qrCodeDataDao(): QRCodeDao
    abstract fun doctorDao(): DoctorDao
    abstract fun presctriptionDao(): PrescriptionDao

}


object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tdm_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}