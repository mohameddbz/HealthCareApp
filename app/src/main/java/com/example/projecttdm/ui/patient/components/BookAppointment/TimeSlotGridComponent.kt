package com.example.projecttdm.ui.patient.components.Appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.viewmodel.BookAppointmentViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.util.Log


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSlotGrid(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookAppointmentViewModel
) {
    // Collect state from ViewModel
    val availableSlots by viewModel.availableSlots.collectAsState()

    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }

    LaunchedEffect(true) {
        println("Available slots: $availableSlots") // Print the available slots to Logcat
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
    ) {
        items(availableSlots) { slot ->
            val startTime = slot.start_time
            val isSelected = selectedTime == startTime
            val isAvailable = !slot.is_book

            val backgroundColor = when {
                isSelected -> Blue01
                isAvailable -> Color.Transparent
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            }

            val borderColor = when {
                isSelected -> Blue01
                isAvailable -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.outline
            }

            val textColor = when {
                isSelected -> Color.White
                isAvailable -> Blue01
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = backgroundColor,
                border = BorderStroke(1.dp, borderColor),
                onClick = { if (isAvailable) onTimeSelected(startTime) },
                enabled = isAvailable,
                modifier = Modifier.height(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = startTime.format(timeFormatter),
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
