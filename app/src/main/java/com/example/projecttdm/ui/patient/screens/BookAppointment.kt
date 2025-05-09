package com.example.projecttdm.ui.patient.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.theme.Gray02
import com.example.projecttdm.theme.Blue02
import com.example.projecttdm.ui.patient.components.Appointment.TimeSlotGrid
import com.example.projecttdm.ui.patient.components.BookAppointment.DatePicker
import com.example.projecttdm.viewmodel.BookAppointmentViewModel
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookAppointmentScreen(
    doctorId: String,
    patientId: String,
    onNextClicked: () -> Unit,
    appointmentViewModel: BookAppointmentViewModel = viewModel()
) {
    val selectedDate by appointmentViewModel.selectedDate.collectAsState()
    val selectedTime by appointmentViewModel.selectedTime.collectAsState()
    val currentMonth by appointmentViewModel.currentMonth.collectAsState()
    val scrollState = rememberScrollState()


    // Initialize the screen
    LaunchedEffect(doctorId, patientId) {
        Log.d("BookAppointmentScreen", "LaunchedEffect with doctorId: $doctorId, patientId: $patientId")
        appointmentViewModel.setDoctorId(doctorId)
        appointmentViewModel.setPatientId(patientId)
        appointmentViewModel.setSelectedDate(LocalDate.of(2025, 5, 18)) // Default initial date
    }

    // Fetch slots whenever the selected date changes
    LaunchedEffect(selectedDate) {
        selectedDate?.let {
            Log.d("BookAppointmentScreen", "Fetching slots for date: $it")
            appointmentViewModel.fetchSlotsByDoctorIdAndDate(doctorId)
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize().padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Book Appointment",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Gray02,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Calendar Section
            Text(
                text = "Select Date",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Gray02,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .align(Alignment.Start)
            )

            DatePicker(
                selectedDate = selectedDate,
                initialMonth = currentMonth,
                onDateSelected = { date ->
                    Log.d("BookAppointmentScreen", "Date selected: $date")
                    if (selectedDate == date) {
                        appointmentViewModel.setSelectedDate(null)
                    } else {
                        appointmentViewModel.setSelectedDate(date)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blue02, MaterialTheme.shapes.medium)
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Time Slots Section
            selectedDate?.let {
                Text(
                    text = "Select Hour",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Gray02,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp)
                )

                TimeSlotGrid(
                    selectedTime = selectedTime,
                    onTimeSelected = { time ->
                        Log.d("BookAppointmentScreen", "Time selected: $time")
                        appointmentViewModel.setSelectedTime(time)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    viewModel = appointmentViewModel
                )

            }
        }
    }

}