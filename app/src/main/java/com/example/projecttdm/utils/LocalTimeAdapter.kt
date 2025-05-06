package com.example.projecttdm.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    // Serialize LocalTime to string
    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(formatter))  // Convert LocalTime to string
    }

    // Deserialize string to LocalTime
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime {
        return LocalTime.parse(json?.asString, formatter)  // Convert string back to LocalTime
    }
}