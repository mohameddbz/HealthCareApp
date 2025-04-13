package com.example.projecttdm.ui.patient.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.theme.Blue02
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSlotGridComponent(
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.height(220.dp)
    ) {
        items(timeSlots) { time ->
            val isSelected = time == selectedTime
            val formattedTime = when {
                time.hour < 12 -> String.format("%02d.%02d AM", time.hour, time.minute)
                time.hour == 12 -> String.format("%02d.%02d PM", time.hour, time.minute)
                else -> String.format("%02d.%02d PM", time.hour - 12, time.minute)
            }

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, Blue01),
                onClick = { onTimeSelected(time) },
                modifier = Modifier.height(44.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(
                        if (isSelected) Blue02 else Color.Transparent
                    )
                ) {
                    Text(
                        text = formattedTime,
                        color = Blue01,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}
