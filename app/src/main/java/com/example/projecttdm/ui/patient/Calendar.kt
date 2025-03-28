package com.example.projecttdm.ui.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendar() {
    // Vos couleurs
    val Blue01 = Color(0xFF3E69FE)
    val Gray01 = Color(0xFF9CA3AF)
    val Gray02 = Color(0xFF1C2A3A)
    val Blue02 = Color(0xFFDEE9FB)

    // Configuration du calendrier
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) } // 1 an en arrière
    val endMonth = remember { currentMonth.plusMonths(12) }    // 1 an en avant
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.MONDAY // Semaine commence lundi
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // En-tête du mois
        Text(
            text = calendarState.firstVisibleMonth.yearMonth.month
                .getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                    " " + calendarState.firstVisibleMonth.yearMonth.year,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Gray02,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Jours de la semaine
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    color = Gray01,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendrier horizontal
        HorizontalCalendar(
            state = calendarState,
            dayContent = { day ->
                val isSelected = selectedDate == day.date
                val isToday = day.date == LocalDate.now()

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(
                            color = when {
                                isSelected -> Blue01
                                isToday -> Blue02
                                else -> Color.Transparent
                            }
                        )
                        .clickable { selectedDate = day.date },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        color = when {
                            isSelected -> Color.White
                            day.position == DayPosition.MonthDate -> Gray02
                            else -> Gray01.copy(alpha = 0.5f)
                        },
                        fontSize = 14.sp
                    )
                }
            },
            monthHeader = { /* Vide car nous gérons l'en-tête nous-mêmes */ }
        )

        // Date sélectionnée
        selectedDate?.let { date ->
            Text(
                text = "Sélectionné: ${date.dayOfMonth}/${date.monthValue}/${date.year}",
                color = Blue01,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}