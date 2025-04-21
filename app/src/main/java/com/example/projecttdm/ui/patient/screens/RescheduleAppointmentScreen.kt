package com.example.projecttdm.ui.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.ui.patient.components.BookAppointment.DatePicker
import com.example.projecttdm.ui.patient.components.Appointment.TimeSlotGrid
import com.example.projecttdm.viewmodel.RescheduleAppointmentViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleAppointmentScreen(
    appointmentId: String,
    onRescheduleSuccess: () -> Unit,
    viewModel: RescheduleAppointmentViewModel = viewModel()
) {
    val colorScheme = MaterialTheme.colorScheme

    // Set up the appointment to reschedule
    LaunchedEffect(appointmentId) {
        viewModel.setAppointmentToReschedule(appointmentId)
    }

    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()
    val currentMonth by viewModel.currentMonth.collectAsState()
    val appointmentToReschedule by viewModel.appointmentToReschedule.collectAsState()
    val rescheduleSuccessful by viewModel.rescheduleSuccessful.collectAsState()

    // Navigate away on success
    LaunchedEffect(rescheduleSuccessful) {
        if (rescheduleSuccessful) {
            viewModel.resetSuccess()
            onRescheduleSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Reschedule Appointment",
            style = MaterialTheme.typography.titleLarge.copy(
                color = colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Show original appointment details
        appointmentToReschedule?.let { appointment ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Current Appointment",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Date: ${appointment.date}")
                    Text("Time: ${appointment.time}")
                    if (appointment.reason.isNotEmpty()) {
                        Text("Reason: ${appointment.reason}")
                    }
                }
            }
        }

        Text(
            text = "Select New Date",
            style = MaterialTheme.typography.titleMedium.copy(
                color = colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        DatePicker(
            selectedDate = selectedDate,
            initialMonth = currentMonth,
            onDateSelected = { date ->
                if (selectedDate == date) {
                    viewModel.setSelectedDate(null)
                } else {
                    viewModel.setSelectedDate(date)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select New Time",
            style = MaterialTheme.typography.titleMedium.copy(
                color = colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        TimeSlotGrid(
            selectedTime = selectedTime,
            onTimeSelected = { time -> viewModel.setSelectedTime(time) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.debugReschedule()
                if (viewModel.rescheduleAppointment()) {
                    // The success will be handled by the LaunchedEffect above
                }
            },
            enabled = selectedDate != null && selectedTime != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                disabledContainerColor = colorScheme.secondaryContainer,
                contentColor = colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Confirm Reschedule",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}