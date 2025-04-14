package com.example.projecttdm.ui.patient.components.Appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.projecttdm.theme.Blue01
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    selectedDate: LocalDate?,
    initialMonth: YearMonth,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(initialMonth) }

    Column(modifier = modifier) {
        // Month navigation row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                // Handle previous month navigation
                currentMonth = currentMonth.minusMonths(1)
            }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous Month",
                    tint = Color.DarkGray
                )
            }

            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            IconButton(onClick = {
                // Handle next month navigation
                currentMonth = currentMonth.plusMonths(1)
            }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Next Month",
                    tint = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Days of week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (dayOfWeek in DayOfWeek.values()) {
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calculate days to display
        val days = remember(currentMonth) {
            val firstDay = currentMonth.atDay(1)
            val lastDay = currentMonth.atEndOfMonth()

            // Get the day of week for the first day (0 = Monday in our grid)
            val firstDayOfWeek = firstDay.dayOfWeek.value % 7

            // Create list of days, including padding for previous month
            val paddingStart = List(firstDayOfWeek) { null }
            val daysInMonth = (1..lastDay.dayOfMonth).map { day -> currentMonth.atDay(day) }

            paddingStart + daysInMonth
        }

        // Calendar days grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(days) { day ->
                if (day == null) {
                    // Empty space for padding days
                    Box(modifier = Modifier.aspectRatio(1f))
                } else {
                    val isSelected = day == selectedDate
                    val isToday = day == today
                    val isPastDate = day.isBefore(today)

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelected -> Blue01
                                    isToday -> Color.LightGray.copy(alpha = 0.3f)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable(enabled = !isPastDate) {
                                onDateSelected(day)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = when {
                                    isSelected -> Color.White
                                    isPastDate -> Color.LightGray
                                    else -> Color.Black
                                },
                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}