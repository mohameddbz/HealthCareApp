package com.example.projecttdm.utils

import androidx.room.TypeConverter
import com.example.projecttdm.data.model.ImageBlob
import com.example.projecttdm.data.model.Specialty
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DoctorConverters {
    private val gson = Gson()

    // Specialty
    @TypeConverter
    fun fromSpecialty(specialty: Specialty): String = specialty.name

    @TypeConverter
    fun toSpecialty(name: String): Specialty = Specialty(id="1",name = name)

    // ImageBlob
    @TypeConverter
    fun fromImageBlob(blob: ImageBlob?): String? {
        return if (blob == null) null else gson.toJson(blob)
    }

    @TypeConverter
    fun toImageBlob(json: String?): ImageBlob? {
        return if (json == null) null else gson.fromJson(json, ImageBlob::class.java)
    }
}