package com.example.projecttdm.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun calculateAgeFromIsoDate(dateString: String): Int {
    println("---------------------------------------------------------------------------")
    println("---------------------------------------------------------------------------")
   println(dateString)
    println("---------------------------------------------------------------------------")
    println("---------------------------------------------------------------------------")

    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val birthDate = LocalDate.parse(dateString, formatter.withZone(ZoneOffset.UTC))
    val currentDate = LocalDate.now(ZoneOffset.UTC)
    return ChronoUnit.YEARS.between(birthDate, currentDate).toInt()
}


@RequiresApi(Build.VERSION_CODES.O)
fun transformerDateIsoVersLisible(dateIso: String): String {
    val zonedDateTime = ZonedDateTime.parse(dateIso)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return zonedDateTime.format(formatter)
}








fun convertTo12HourFormat(time24: String): String {
    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    return try {
        val date = inputFormat.parse(time24)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        "Format invalide"
    }
}



fun getVisitText(visitNumber: Int): String {
    val number = visitNumber + 1

    val suffix = when {
        number % 100 in 11..13 -> "th"
        number % 10 == 1 -> "st"
        number % 10 == 2 -> "nd"
        number % 10 == 3 -> "rd"
        else -> "th"
    }

    return if (visitNumber == 0) {
        "First visit"
    } else {
        "$number$suffix visit"
    }
}