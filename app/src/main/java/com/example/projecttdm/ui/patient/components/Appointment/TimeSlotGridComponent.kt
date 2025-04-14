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
import com.example.projecttdm.data.local.BookAppointmentData
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.theme.Blue02
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSlotGrid(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeSlots = remember { BookAppointmentData.availableTimeSlots }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(timeSlots) { time ->
            val isSelected = time == selectedTime
            val isAvailable = remember { BookAppointmentData.isTimeSlotAvailable(time) }

            // Determine colors based on selection and availability
            val backgroundColor = when {
                isSelected -> Blue01
                isAvailable -> Color.Transparent
                else -> Color.LightGray.copy(alpha = 0.3f)
            }

            val borderColor = when {
                isSelected -> Blue01
                isAvailable -> Blue02
                else -> Color.LightGray
            }

            val textColor = when {
                isSelected -> Color.White
                isAvailable -> Blue01  // Changed to Blue01 as requested
                else -> Color.Gray
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = backgroundColor,
                border = BorderStroke(1.dp, borderColor),
                onClick = { if (isAvailable) onTimeSelected(time) },
                enabled = isAvailable,
                modifier = Modifier.height(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = time.format(timeFormatter),
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}