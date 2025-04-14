package com.example.projecttdm.ui.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.data.model.AppointmentReviewData
import com.example.projecttdm.data.repository.AppointmentSummaryRepository
import com.example.projecttdm.ui.patient.components.AppointmentDetailsCard
import com.example.projecttdm.ui.patient.components.DoctorInfoCard
import com.example.projecttdm.ui.patient.components.PatientDetailsCard
import com.example.projecttdm.viewmodel.AppointmentSummaryViewModel

import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentReviewScreen(
    appointmentId: String = "APT-123456",
    navController: NavController = rememberNavController(),
    viewModel: AppointmentSummaryViewModel = AppointmentSummaryViewModel(repository =  AppointmentSummaryRepository()  ),
    onBackPressed: () -> Unit = {},
    onNextPressed: () -> Unit = {}
) {
    val viewModel: AppointmentSummaryViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
        AppointmentSummaryViewModel(repository = AppointmentSummaryRepository())
    }

    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    // Load data when the screen is first composed
    LaunchedEffect(appointmentId) {
        viewModel.loadAppointmentReviewData(appointmentId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top bar with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    scope.launch {
                        onBackPressed()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Review Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Content based on UI state
            when (uiState) {
                is AppointmentSummaryViewModel.AppointmentUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is AppointmentSummaryViewModel.AppointmentUiState.Error -> {
                    val errorMessage = (uiState as AppointmentSummaryViewModel.AppointmentUiState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Error",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                viewModel.loadAppointmentReviewData(appointmentId)
                            }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                is AppointmentSummaryViewModel.AppointmentUiState.Success -> {
                    val data = (uiState as AppointmentSummaryViewModel.AppointmentUiState.Success).data
                    AppointmentReviewContent(
                        data = data,
                        onNextPressed = onNextPressed
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AppointmentReviewContent(
    data: AppointmentReviewData,
    onNextPressed: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // Doctor information card
        DoctorInfoCard(doctor = data.doctor)

        // Appointment details card
        AppointmentDetailsCard(appointment = data.appointment)

        // Patient details card
        PatientDetailsCard(patient = data.patient)

        Spacer(modifier = Modifier.weight(1f))

        // Bottom button
        Button(
            onClick = {
                scope.launch {
                    onNextPressed()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3D7BF6)
            )
        ) {
            Text(
                text = "Next",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AppointmentReview() {
    val navController = rememberNavController()
    AppointmentReviewScreen(
        navController = navController,
        onBackPressed = {  },
        onNextPressed = { }
    )
}