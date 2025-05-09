package com.example.projecttdm.ui.patient.components.Appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.state.UiState
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.theme.Blue02
import com.example.projecttdm.theme.Gray01
import com.example.projecttdm.theme.Gray02
import com.example.projecttdm.viewmodel.BookAppointmentViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSlotGrid(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookAppointmentViewModel,
    onNextClicked: (String) -> Unit
) {
    val availableSlots by viewModel.availableSlots.collectAsState()
    val slotsUiState by viewModel.slotsUiState.collectAsState()

    when (slotsUiState) {
        is UiState.Error -> {
            val errorMessage = (slotsUiState as UiState.Error).message
            val message = if (errorMessage.contains("HTTP 404")) {
                "No appointments available for this date"
            } else {
                "Error loading slots: $errorMessage"
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (errorMessage.contains("HTTP 404")) Gray02 else MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }

        is UiState.Success -> {
            if (availableSlots.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Text(
                        text = "No appointment slots available today",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray02,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val availableTimes = availableSlots.map { slot ->
                    Triple(
                        LocalTime.parse(slot.start_time.toString(), DateTimeFormatter.ofPattern("HH:mm")),
                        slot.is_book,
                        LocalDate.parse(slot.working_date.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    )
                }.sortedBy { it.first }


                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            contentPadding = PaddingValues(4.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(availableTimes) { (time, isBooked) ->
                                TimeSlotItem(
                                    time = time,
                                    isSelected = time == selectedTime,
                                    isBooked = isBooked,
                                    onSelected = { onTimeSelected(time) },
                                    modifier = Modifier.padding(4.dp)
                                )
                            }

                        }
                    }

                    Button(
                        onClick = {
                            selectedTime?.let { selected ->
                                val selectedSlot = availableSlots.find {
                                    LocalTime.parse(it.start_time.toString(), DateTimeFormatter.ofPattern("HH:mm")) == selected
                                }
                                selectedSlot?.let { slot ->
                                    //viewModel.bookAppointment(slot_id = slot.slot_id.toString())
                                    onNextClicked(slot.slot_id.toString())
                                }
                            }
                        },
                        enabled = selectedTime != null,
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
        }

        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (slotsUiState is UiState.Loading) {
                    CircularProgressIndicator(color = Blue01)
                } else {
                    Text(
                        text = "Select a date to view available slots",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray02,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSlotItem(
    time: LocalTime,
    isSelected: Boolean,
    isBooked: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
)
{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = time.format(formatter)

    val backgroundColor = when {
        isBooked -> Gray01
        isSelected -> Blue01
        else -> Blue02
    }
    val textColor = if (isSelected) Color.White else Gray02
    val borderColor = if (isSelected) Blue01 else Gray01

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .height(44.dp)
            .border(1.dp, borderColor, MaterialTheme.shapes.small)
            .then(
                if (!isBooked) Modifier.clickable { onSelected() } else Modifier
            )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = formattedTime,
                color = textColor,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}