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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
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
import com.example.projecttdm.theme.BluePrimary
import com.example.projecttdm.theme.BlueSecondary
import com.example.projecttdm.theme.Gray01
import com.example.projecttdm.theme.Gray02
import com.example.projecttdm.theme.Gray03
import com.example.projecttdm.theme.SurfaceColor
import com.example.projecttdm.theme.TextPrimary
import com.example.projecttdm.theme.TextSecondaryLight
import com.example.projecttdm.viewmodel.PrescriptionViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrescriptionScreen(
    oncickAdd: (appointId: String, patientId: String) -> Unit = { _, _ -> },
    onclick: (String) -> Unit,
    onBack: () -> Unit, // <- nouveau paramètre pour le retour
    viewModel: PrescriptionViewModel = viewModel(),
    appointId: String,
    patientId: String,
    showAddButton: Boolean = false
) {
    LaunchedEffect(Unit) {
        viewModel.fetchPrescriptions(appointId)
    }

    val prescriptionsState by viewModel.prescriptionsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Prescriptions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (val state = prescriptionsState) {
                is PrescriptionsUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PrescriptionsUiState.Success -> {
                    Column {
                        ImprovedPatientInfoCard(
                            patientName = state.patientName,
                            patientAge = state.patientAge,
                            appointmentTime = state.appointmentTime
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // ✅ Afficher le bouton "Add" seulement si showAddButton est true
                        if (showAddButton) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Text(
                                    text = "Add a prescription",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = { oncickAdd(appointId, patientId) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            color = Color(0xFF3D7FFF),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        if (state.prescriptions.isEmpty()) {
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
                                items(state.prescriptions) { prescription ->
                                    ImprovedPrescriptionItem(
                                        prescription = prescription,
                                        onClick = { onclick(prescription.prescription_id.toString()) }
                                    )
                                }
                            }
                        }
                    }
                }

                is PrescriptionsUiState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImprovedPatientInfoCard(
    patientName: String,
    patientAge: Int,
    appointmentTime: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BlueSecondary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Patient avatar with improved styling
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(BluePrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Patient",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = patientName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Gray02
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Age: $patientAge",
                        color = TextSecondaryLight,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Divider(
                        modifier = Modifier
                            .height(12.dp)
                            .width(1.dp),
                        color = Gray01
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Appointment time",
                        tint = BluePrimary,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = appointmentTime,
                        color = TextSecondaryLight,
                        fontSize = 14.sp
                    )
                }
            }

            // Add a subtle chevron or arrow indicator to show the card is interactive
            IconButton(
                onClick = { /* Handle click */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View patient details",
                    tint = BluePrimary
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ImprovedPrescriptionItem(
    prescription: PrescriptionDoc,
    onClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val createdDate = try {
        LocalDateTime.parse(prescription.created_at, formatter).toLocalDate()
    } catch (e: Exception) {
        LocalDate.now() // Fallback if parsing fails
    }

    val formattedDate = createdDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left accent bar indicating prescription status
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 50.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(BluePrimary)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Prescription #${prescription.prescription_id}",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formattedDate,
                    color = Gray01,
                    fontSize = 14.sp
                )
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BlueSecondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View details",
                    tint = BluePrimary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

// Bonus: Comprehensive card that combines patient info and their prescription status
@Composable
fun PatientPrescriptionCard(
    patientName: String,
    patientAge: Int,
    appointmentTime: String,
    prescriptionCount: Int,
    lastPrescriptionDate: String,
    onViewDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onViewDetailsClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top section with patient info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Patient avatar
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(BluePrimary)
                ) {
                    Text(
                        text = patientName.first().toString(),
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = patientName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )

                    Text(
                        text = "Age: $patientAge • $appointmentTime",
                        color = Gray01,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Divider between sections
            Divider(color = Gray03.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom section with prescription info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$prescriptionCount Prescriptions",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )

                    Text(
                        text = "Last updated: $lastPrescriptionDate",
                        fontSize = 12.sp,
                        color = Gray01
                    )
                }

                Button(
                    onClick = onViewDetailsClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BluePrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("View Details")
                }
            }
        }
    }
}