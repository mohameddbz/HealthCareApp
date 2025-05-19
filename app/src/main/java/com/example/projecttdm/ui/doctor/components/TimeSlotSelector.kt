package com.example.projecttdm.ui.doctor.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeSlotSelector(
    title: String,
    slots: List<TimeSlot>,
    onTimeSelected: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            slots.forEach { slot ->
                TimeButton(
                    slot = slot,
                    onTimeSelected = { time -> onTimeSelected(time, slot.isStartTime) }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeButton(
    slot: TimeSlot,
    onTimeSelected: (String) -> Unit
) {
    val dialogState = rememberMaterialDialogState()
    var selectedTime by remember { mutableStateOf(slot.selectedTime) }

    Box(
        modifier = Modifier
            .height(48.dp)
            .width(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { dialogState.show() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Select time",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = selectedTime ?: "Select Time",
                style = MaterialTheme.typography.bodyMedium,
                color = if (selectedTime == null)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("OK") {
                // Time has been set in the onTimeChange callback
            }
            negativeButton("Cancel")
        }
    ) {
        timepicker(
            initialTime = parseTimeString(selectedTime),
            title = "Select time",
            timeRange = LocalTime.of(0, 0)..LocalTime.of(23, 59)
        ) { time ->
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            selectedTime = time.format(formatter)
            onTimeSelected(selectedTime!!)
        }
    }
}

private fun parseTimeString(timeString: String?): LocalTime {
    return timeString?.let {
        try {
            val parts = it.split(":")
            if (parts.size == 2) {
                val hour = parts[0].toInt()
                val minute = parts[1].toInt()
                LocalTime.of(hour, minute)
            } else {
                LocalTime.of(8, 0) // Default 8:00 AM
            }
        } catch (e: Exception) {
            LocalTime.of(8, 0) // Default if parsing fails
        }
    } ?: LocalTime.of(8, 0) // Default if null
}

data class TimeSlot(
    val id: String,
    val isStartTime: Boolean,
    val selectedTime: String? = null
)