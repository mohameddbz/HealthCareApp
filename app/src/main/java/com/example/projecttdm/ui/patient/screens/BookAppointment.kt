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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.theme.Blue02
import com.example.projecttdm.ui.patient.components.Appointment.DatePicker
import com.example.projecttdm.ui.patient.components.Appointment.TimeSlotGrid
import com.example.projecttdm.viewmodel.BookAppointmentViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    doctorId: String,
    patientId: String,
    onNextClicked: () -> Unit,
    appointmentViewModel: BookAppointmentViewModel = viewModel()
) {
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(doctorId, patientId) {
        appointmentViewModel.setDoctorId(doctorId)
        appointmentViewModel.setPatientId(patientId)
    }

    val selectedDate by appointmentViewModel.selectedDate.collectAsState()
    val selectedTime by appointmentViewModel.selectedTime.collectAsState()
    val currentMonth by appointmentViewModel.currentMonth.collectAsState()
    val reason by appointmentViewModel.reason.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Book Appointment",
            style = MaterialTheme.typography.titleLarge.copy(
                color = colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Select Date",
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
                    appointmentViewModel.setSelectedDate(null)
                } else {
                    appointmentViewModel.setSelectedDate(date)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Hour",
            style = MaterialTheme.typography.titleMedium.copy(
                color = colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        TimeSlotGrid(
            selectedTime = selectedTime,
            onTimeSelected = { time -> appointmentViewModel.setSelectedTime(time) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (appointmentViewModel.bookAppointment()) {
                    onNextClicked()
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
                "Book Appointment",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
