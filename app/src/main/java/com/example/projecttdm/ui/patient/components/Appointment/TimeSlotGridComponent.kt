package com.example.projecttdm.ui.patient.components.Appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
                color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                border = BorderStroke(
                    1.dp,
                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                ),
                onClick = { onTimeSelected(time) },
                modifier = Modifier.height(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = time.format(timeFormatter),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}