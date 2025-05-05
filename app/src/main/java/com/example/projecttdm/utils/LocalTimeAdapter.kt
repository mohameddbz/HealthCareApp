package com.example.projecttdm.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    // Define the DateTimeFormatter to match the format (e.g., "HH:mm:ss")
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
