package com.example.projecttdm.utils

import com.example.projecttdm.data.model.AppointmentStatus
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class AppointmentStatusTypeAdapter : TypeAdapter<AppointmentStatus>() {
    override fun write(out: JsonWriter, value: AppointmentStatus?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.name)
        }
    }

    override fun read(input: JsonReader): AppointmentStatus? {
        if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            return null
        }
        return AppointmentStatus.fromString(input.nextString())
    }
}