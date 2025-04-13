package com.example.projecttdm.ui.patient.components.Appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


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

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Month header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentMonth.year,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { currentMonth = currentMonth.minusMonths(1) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous month")
                }

                IconButton(
                    onClick = { currentMonth = currentMonth.plusMonths(1) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next month")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Day headers
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEach { day ->
                    Text(
                        text = day,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                items(firstDayOfWeek) { Box(modifier = Modifier.aspectRatio(1f)) }

                items(daysInMonth) { day ->
                    val date = currentMonth.atDay(day + 1)
                    val isSelected = date == selectedDate
                    val isCurrentMonth = date.month == currentMonth.month
                    val isToday = date.equals(LocalDate.now())

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                isToday -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                else -> Color.Transparent
                            },
                            border = BorderStroke(
                                1.dp,
                                when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                    else -> Color.Transparent
                                }
                            ),
                            onClick = { if (isCurrentMonth) onDateSelected(date) },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = (day + 1).toString(),
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                        isToday -> MaterialTheme.colorScheme.primary
                                        !isCurrentMonth -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}