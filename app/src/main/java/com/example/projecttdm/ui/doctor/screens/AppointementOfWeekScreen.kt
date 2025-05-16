package com.example.projecttdm.ui.doctor.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.data.model.AppointmentWeekResponse
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.doctor.components.AppointmentsList
import com.example.projecttdm.ui.doctor.components.EmptyAppointmentsView
import com.example.projecttdm.ui.doctor.components.WeekCalendar
import com.example.projecttdm.viewmodel.AppointmentsViewModelFactory
import com.example.projecttdm.viewmodel.DoctorAppointmentsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentOfWeekScreen(
    doctorId: String,
    onBrowseDoctors: () -> Unit,
    onAppointmentClick: (String) -> Unit,
    initialSelectedDate: LocalDate = LocalDate.now()
) {
    // Pas besoin de passer le repository ici
    val viewModel: DoctorAppointmentsViewModel = viewModel(factory = AppointmentsViewModelFactory())

    var selectedDate by remember { mutableStateOf(initialSelectedDate) }

    LaunchedEffect(selectedDate) {
        val formattedDate = selectedDate.format(DateTimeFormatter.ISO_DATE)
        viewModel.loadAppointmentsByDate(doctorId, formattedDate)
    }

    val state by viewModel.appointments.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "My Appointments",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        WeekCalendar(
            selectedDate = selectedDate,
            onDateSelected = { date -> selectedDate = date }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is UiState.Init -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Select a date to view appointments")
                }
            }

            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error loading appointments: ${(state as UiState.Error).message}")
                }
            }

            is UiState.Success -> {
                val appointments = (state as UiState.Success<List<AppointmentWeekResponse>>).data
                if (appointments.isEmpty()) {
                    EmptyAppointmentsView(onBrowseDoctors)
                } else {
                    AppointmentsList(
                        appointments = appointments,
                        onAppointmentClick = onAppointmentClick
                    )
                }
            }
        }
    }
}
