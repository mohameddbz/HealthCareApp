package com.example.projecttdm.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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