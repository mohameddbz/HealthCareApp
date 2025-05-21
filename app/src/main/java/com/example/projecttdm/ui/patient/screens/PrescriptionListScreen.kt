package com.example.projecttdm.ui.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.data.model.PrescriptionDoc
import com.example.projecttdm.data.model.PrescriptionsUiState
import com.example.projecttdm.viewmodel.PrescriptionViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrescriptionScreen(
    navigateToPrescriptionDetail: (Int) -> Unit,
    onclick: (String) -> Unit,
    viewModel: PrescriptionViewModel = viewModel(),
    appointId: String,
    patientName: String = "Thanina Amirat",
    patientAge: Int = 21,
    appointmentTime: String = "11:00 - 11:30"
) {
    LaunchedEffect(key1 = true) {
        viewModel.fetchPrescriptions(appointId)
    }

    val prescriptionsState by viewModel.prescriptionsState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Prescriptions",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Patient info card
        PatientInfoCard(
            patientName = patientName,
            patientAge = patientAge,
            appointmentTime = appointmentTime
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Add prescription button
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(vertical = 8.dp)
//        ) {
//            Text(
//                text = "Add a prescription",
//                fontSize = 16.sp,
//                modifier = Modifier.weight(1f)
//            )
//
//            IconButton(
//                onClick = { navigateToAddPrescription() },
//                modifier = Modifier
//                    .size(40.dp)
//                    .background(
//                        color = MaterialTheme.colorScheme.primary,
//                        shape = CircleShape
//                    )
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Add prescription",
//                    tint = Color.White
//                )
//            }
//        }

        Spacer(modifier = Modifier.height(8.dp))

        when (prescriptionsState) {
            is PrescriptionsUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
            is PrescriptionsUiState.Success -> {
                val prescriptions = (prescriptionsState as PrescriptionsUiState.Success).prescriptions
                if (prescriptions.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("No prescriptions found")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(prescriptions) { prescription ->
                            PrescriptionItem(
                                prescription = prescription,
                                onClick = {onclick(prescription.prescription_id.toString())}
                            )
                        }
                    }
                }
            }
            is PrescriptionsUiState.Error -> {
                val errorMessage = (prescriptionsState as PrescriptionsUiState.Error).message
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is PrescriptionsUiState.Error -> TODO()
            PrescriptionsUiState.Loading -> TODO()
            is PrescriptionsUiState.Success -> TODO()
        }
    }
}

@Composable
fun PatientInfoCard(
    patientName: String,
    patientAge: Int,
    appointmentTime: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for profile image
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                // You can replace this with AsyncImage for loading actual profile picture
                Text(
                    text = patientName.first().toString(),
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = patientName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Age: $patientAge Yo",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = appointmentTime,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrescriptionItem(
    prescription: PrescriptionDoc,
    onClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val createdDate = try {
        LocalDateTime.parse(prescription.created_at, formatter).toLocalDate()
    } catch (e: Exception) {
        LocalDate.now() // Fallback if parsing fails
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Persription ${prescription.prescription_id}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = createdDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "View details",
                tint = Color.White
            )
        }
    }
}