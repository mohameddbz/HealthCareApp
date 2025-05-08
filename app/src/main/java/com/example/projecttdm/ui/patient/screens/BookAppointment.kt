//package com.example.projecttdm.ui.patient.screens
//
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.projecttdm.theme.Gray02
//import com.example.projecttdm.theme.Gray01
//import com.example.projecttdm.theme.Blue01
//import com.example.projecttdm.theme.Blue02
//import com.example.projecttdm.ui.patient.components.Appointment.TimeSlotGrid
//import com.example.projecttdm.ui.patient.components.BookAppointment.DatePicker
//import com.example.projecttdm.viewmodel.BookAppointmentViewModel
//import java.time.LocalDate
//
//
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BookAppointmentScreen(doctorId: String,
//                          patientId: String,
//                          onNextClicked: () -> Unit,
//                          appointmentViewModel: BookAppointmentViewModel = viewModel()
//) {
//    val scrollState = rememberScrollState()
//    val selectedDate by appointmentViewModel.selectedDate.collectAsState()
//    val selectedTime by appointmentViewModel.selectedTime.collectAsState()
//    val currentMonth by appointmentViewModel.currentMonth.collectAsState()
//    val reason by appointmentViewModel.reason.collectAsState()
//
//    LaunchedEffect(doctorId, patientId) {
//        appointmentViewModel.setDoctorId(doctorId)
//        appointmentViewModel.setPatientId(patientId)
//        appointmentViewModel.setSelectedDate(LocalDate.of(2025, 5, 18)) // <-- set the date first
//        appointmentViewModel.fetchSlotsByDoctorIdAndDate('2'.toString())
//    }
//
//    Scaffold() {paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize().padding(paddingValues)
//                .verticalScroll(scrollState)
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "Book Appointment",
//                style = MaterialTheme.typography.titleLarge.copy(
//                    color = Gray02,
//                    fontSize = 20.sp
//                ),
//                modifier = Modifier.padding(bottom = 12.dp)
//            )
//
//            // Calendar Section
//            Text(
//                text = "Select Date",
//                style = MaterialTheme.typography.titleMedium.copy(
//                    color = Gray02,
//                    fontSize = 16.sp
//                ),
//                modifier = Modifier
//                    .padding(bottom = 4.dp)
//                    .align(Alignment.Start)
//            )
//
//            DatePicker(
//                selectedDate = selectedDate,
//                initialMonth = currentMonth,
//                onDateSelected = { date ->
//                    if (selectedDate == date) {
//                        appointmentViewModel.setSelectedDate(null)
//                    } else {
//                        appointmentViewModel.setSelectedDate(date)
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Blue02, MaterialTheme.shapes.medium)
//                    .padding(12.dp)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Time Slots Section
//            selectedDate?.let {
//                Text(
//                    text = "Select Hour",
//                    style = MaterialTheme.typography.titleMedium.copy(
//                        color = Gray02,
//                        fontSize = 16.sp
//                    ),
//                    modifier = Modifier
//                        .align(Alignment.Start)
//                        .padding(bottom = 4.dp)
//                )
//
//                TimeSlotGrid(
//                    selectedTime = selectedTime,
//                    onTimeSelected = { time -> appointmentViewModel.setSelectedTime(time) },
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Next Button
//            Button(
//                onClick = onNextClicked,
//                enabled = selectedDate != null && selectedTime != null,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Blue01,
//                    disabledContainerColor = Gray01,
//                    contentColor = Color.White
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(44.dp),
//                shape = MaterialTheme.shapes.medium
//            ) {
//                Text(
//                    "Next",
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 15.sp
//                    )
//                )
//            }
//        }
//    }
//}
package com.example.projecttdm.ui.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.data.model.AppointmentSlot
import com.example.projecttdm.state.UiState
import com.example.projecttdm.theme.Gray02
import com.example.projecttdm.theme.Gray01
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.theme.Blue02
import com.example.projecttdm.ui.patient.components.Appointment.TimeSlotGrid
import com.example.projecttdm.ui.patient.components.BookAppointment.DatePicker
import com.example.projecttdm.viewmodel.BookAppointmentViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    doctorId: String,
    patientId: String,
    onNextClicked: () -> Unit,
    appointmentViewModel: BookAppointmentViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val selectedDate by appointmentViewModel.selectedDate.collectAsState()
    val selectedTime by appointmentViewModel.selectedTime.collectAsState()
    val currentMonth by appointmentViewModel.currentMonth.collectAsState()
    val reason by appointmentViewModel.reason.collectAsState()
    val isLoading by appointmentViewModel.isLoading.collectAsState()
    val slotsUiState by appointmentViewModel.slotsUiState.collectAsState()
    val availableSlots by appointmentViewModel.availableSlots.collectAsState()


    LaunchedEffect(patientId) {
        appointmentViewModel.setPatientId(patientId)
        appointmentViewModel.setSelectedDate(LocalDate.of(2025, 5, 18)) // <-- set the initial date
        appointmentViewModel.fetchSlotsByDoctorIdAndDate("2")
    }

    Scaffold() { paddingValues ->
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
                    println("Date selected: $date") // Debugging the date selection
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

                when (slotsUiState) {
                    is UiState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            CircularProgressIndicator(color = Blue01)
                        }
                    }
                    is UiState.Error -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            Text(
                                text = "Failed to load time slots",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    is UiState.Success -> {
                        // Debugging print for slots
                        println("Slots UIState is Success. Available slots: ${(slotsUiState as UiState.Success<List<AppointmentSlot>>).data}")

                        TimeSlotGrid(
                            selectedTime = selectedTime,
                            onTimeSelected = { time ->
                                println("Time selected: $time") // Debugging time selection
                                appointmentViewModel.setSelectedTime(time)
                            },
                            modifier = Modifier.padding(vertical = 4.dp),
                            viewModel = appointmentViewModel
                        )
                    }
                    UiState.Init -> {
                        // Show nothing or a placeholder
                        println("UIState is Init. No slots available.")
                    }
                }
            }
        }
            Spacer(modifier = Modifier.height(20.dp))

            // Next Button
            Button(
                onClick = onNextClicked,
                enabled = selectedDate != null && selectedTime != null && !isLoading,
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
