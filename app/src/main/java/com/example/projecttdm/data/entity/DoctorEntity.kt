package com.example.projecttdm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.ImageBlob
import com.example.projecttdm.data.model.Specialty
import com.google.gson.Gson

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialty: String, // On convertit Specialty en String
    val hospital: String,
    val rating: Float,
    val reviewCount: Int,
    val imageResId: Int?,
    val imageUrl: String?, // On convertit ImageBlob en String JSON
    val about: String?,
    val yearsExperience: Int?,
    val hospitalLocation: String?,
    val patients: Int?,
    val workingHours: String?
)


fun DoctorEntity.toModel(): Doctor {
    return Doctor(
        id = id,
        name = name,
        specialty = Specialty(id= "12" , name = specialty),
        hospital = hospital,
        rating = rating,
        reviewCount = reviewCount,
        imageResId = imageResId,
        imageUrl = Gson().fromJson(imageUrl, ImageBlob::class.java),
        about = about,
        yearsExperience = yearsExperience,
        hospitalLocation = hospitalLocation,
        patients = patients,
        workingHours = workingHours
    )
}


fun Doctor.toEntity(): DoctorEntity {
    return DoctorEntity(
        id = id,
        name = name,
        specialty = specialty.name,
        hospital = hospital,
        rating = rating,
        reviewCount = reviewCount,
        imageResId = imageResId,
        imageUrl = Gson().toJson(imageUrl),
        about = about,
        yearsExperience = yearsExperience,
        hospitalLocation = hospitalLocation,
        patients = patients,
        workingHours = workingHours
    )
}