package com.example.projecttdm.ui.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

// Your custom colors
val Blue01 = Color(0xFF3E69FE)
val Gray01 = Color(0xFF9CA3AF)
val Gray02 = Color(0xFF1C2A3A)
val Blue02 = Color(0xFFDEE9FB)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen() {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    val currentMonth = remember { YearMonth.now() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Book Appointment",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Gray02,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Calendar Section
        Text(
            text = "Select Date",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Gray02,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .padding(bottom = 4.dp)
                .align(Alignment.Start)
        )

        DatePicker(
            selectedDate = selectedDate,
            initialMonth = currentMonth,
            onDateSelected = { date ->
                if (selectedDate == date) {
                    selectedDate = null
                    selectedTime = null
                } else {
                    selectedDate = date
                    selectedTime = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Blue02, MaterialTheme.shapes.medium)
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time Slots Section
        selectedDate?.let {
            Text(
                text = "Select Hour",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Gray02,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 4.dp)
            )

            TimeSlotGrid(
                selectedTime = selectedTime,
                onTimeSelected = { time -> selectedTime = time },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Next Button
        Button(
            onClick = { /* Handle booking */ },
            enabled = selectedDate != null && selectedTime != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue01,
                disabledContainerColor = Gray01,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Next",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    selectedDate: LocalDate?,
    initialMonth: YearMonth,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(initialMonth) }
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    Column(modifier = modifier) {
        // Month header with arrows on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Month title left-aligned
            Text(
                text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) +
                        " " + currentMonth.year,
                style = MaterialTheme.typography.titleLarge.copy(color = Gray02),
                modifier = Modifier.weight(1f)
            )

            // Arrows on right
            Row {
                IconButton(
                    onClick = { currentMonth = currentMonth.minusMonths(1) },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Blue01)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous month")
                }
                IconButton(
                    onClick = { currentMonth = currentMonth.plusMonths(1) },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Blue01)
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next month")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Day headers (Me, Tu, We...)
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Me", "Tu", "We", "Th", "Fr", "Se", "Su").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Gray02),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(200.dp)
        ) {
            items(firstDayOfWeek) {
                Box(modifier = Modifier.aspectRatio(1f))
            }

            items(daysInMonth) { day ->
                val date = currentMonth.atDay(day + 1)
                val isSelected = date == selectedDate
                val isCurrentMonth = date.month == currentMonth.month

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) Blue01 else Color.Transparent,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Blue01 else Color.Transparent
                        ),
                        onClick = { if (isCurrentMonth) onDateSelected(date) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (day + 1).toString(),
                                color = when {
                                    isSelected -> Color.White
                                    !isCurrentMonth -> Gray01.copy(alpha = 0.5f)
                                    else -> Gray02
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSlotGrid(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeSlots = remember {
        listOf(
            LocalTime.of(9, 0), LocalTime.of(9, 30),
            LocalTime.of(10, 0), LocalTime.of(10, 30),
            LocalTime.of(11, 0), LocalTime.of(11, 30),
            LocalTime.of(15, 0), LocalTime.of(15, 30),
            LocalTime.of(16, 0), LocalTime.of(16, 30),
            LocalTime.of(17, 0), LocalTime.of(17, 30)
        )
    }

    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(timeSlots) { time ->
            val isSelected = time == selectedTime

            Surface(
                shape = MaterialTheme.shapes.small,
                color = if (isSelected) Blue02 else Color.Transparent,
                border = BorderStroke(1.dp, Blue01),
                onClick = { onTimeSelected(time) },
                modifier = Modifier.height(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = time.format(timeFormatter),
                        color = Blue01,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}