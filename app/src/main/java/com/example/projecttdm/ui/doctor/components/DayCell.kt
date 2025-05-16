package com.example.projecttdm.ui.doctor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.projecttdm.ui.doctor.screens.CalendarDay
import com.example.projecttdm.ui.doctor.screens.LightBlue
import java.util.Calendar
import java.util.Date


@Composable
fun DayCell(
    day: CalendarDay,
    isSelected: Boolean,
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.time = day.date
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)


    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        day.isToday -> LightBlue
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> Color.White
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = day.isCurrentMonth) {
                onDateSelected(day.date)
            },
           /* .then(
                if (day.hasAppointment && !isSelected)
                    Modifier.border(BorderStroke(2.dp, AccentBlue), CircleShape)
                else
                    Modifier
            ),*/
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayOfMonth.toString(),
            fontSize = 14.sp,
            fontWeight = if (day.isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )

       /* if ( !isSelected) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(AccentBlue)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp)
            )
        } */
    }
}
