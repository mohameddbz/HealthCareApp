package com.example.projecttdm.ui.doctor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.projecttdm.ui.doctor.screens.CalendarDay
import com.example.projecttdm.ui.doctor.screens.DarkBlue
import java.util.Calendar
import java.util.Date
import java.util.Random


@Composable
fun WeekdaysHeader() {
    val weekdays = listOf("LUN", "MAR", "MER", "JEU", "VEN", "SAM", "DIM")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekdays.forEach { day ->
            Text(
                text = day,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = DarkBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

















// Fonctions utilitaires
fun getCalendarDays(date: Date): List<CalendarDay> {
    val calendar = Calendar.getInstance()
    calendar.time = date

    // Réinitialiser au premier jour du mois
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    // Déterminer le premier jour à afficher (lundi de la semaine contenant le 1er du mois)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val offset = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - 2
    calendar.add(Calendar.DAY_OF_MONTH, -offset)

    val today = Calendar.getInstance()

    val days = mutableListOf<CalendarDay>()
    val currentMonth = getMonth(date)

    // Générer 42 jours (6 semaines)
    repeat(42) {
        val isCurrentMonth = getMonth(calendar.time) == currentMonth
        val isToday = isSameDay(calendar.time, today.time)

        // Simuler des rendez-vous aléatoires
        val hasAppointment = isCurrentMonth && Random().nextInt(10) < 3

        days.add(
            CalendarDay(
                date = calendar.time,
                isCurrentMonth = isCurrentMonth,
                hasAppointment = hasAppointment,
                isToday = isToday
            )
        )

        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return days
}

fun getMonth(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.MONTH)
}

fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = date1
    cal2.time = date2

    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}
