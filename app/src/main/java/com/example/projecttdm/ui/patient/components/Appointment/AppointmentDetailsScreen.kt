package com.example.projecttdm.ui.patient.components.Appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.QRCodeData
import com.example.projecttdm.viewmodel.AppointmentViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(
    viewModel: AppointmentViewModel,
    appointmentId: String,
    navController: NavController
) {
    val appointment by viewModel.selectedAppointment.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showQRCodeDialog by viewModel.showQRCodeDialog.collectAsState()
    val qrCodeData by viewModel.qrCodeData.collectAsState()

    // Local state for notes editing
    val (isEditingNotes, setEditingNotes) = remember { mutableStateOf(false) }
    val (notesText, setNotesText) = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Load appointment details when screen is displayed
    LaunchedEffect(appointmentId) {
        viewModel.getAppointmentDetails(appointmentId)
    }

    // Update local notes when appointment changes
    LaunchedEffect(appointment) {
//        appointment?.let {
//            setNotesText(it.notes ?: "")
//        }
    }

    // Show QR Code dialog if triggered
    if (showQRCodeDialog && qrCodeData != null) {
        QRCodeDialog(
            qrCodeData = qrCodeData!!,
            onDismiss = { viewModel.closeQRCodeDialog() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appointment Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Loading state
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // Error state - no appointment found
            else if (appointment == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Appointment not found",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            // Content - appointment details
            else {
                AppointmentDetailsContent(
                    appointment = appointment!!,
                    doctorName = viewModel.getDoctorName(appointment!!.doctorId),
                    isEditingNotes = isEditingNotes,
                    notesText = notesText,
                    onNotesChange = { setNotesText(it) },
                    onEditNotes = { setEditingNotes(!isEditingNotes) },
                    onSaveNotes = {
                        coroutineScope.launch {
                            viewModel.updateAppointmentNotes(appointment!!.id, notesText)
                            setEditingNotes(false)
                        }
                    },
                    onCancelAppointment = {
                        viewModel.cancelAppointment(appointment!!.id)
                        navController.navigateUp()
                    },
                    onShowQRCode = { viewModel.showAppointmentQRCode(appointment!!.id) },
                    formatTime = { viewModel.formatTime(it) },
                    formatDate = { viewModel.formatDate(it) }
                )
            }
        }
    }
}

@Composable
fun AppointmentDetailsContent(
    appointment: Appointment,
    doctorName: String,
    isEditingNotes: Boolean,
    notesText: String,
    onNotesChange: (String) -> Unit,
    onEditNotes: () -> Unit,
    onSaveNotes: () -> Unit,
    onCancelAppointment: () -> Unit,
    onShowQRCode: () -> Unit,
    formatTime: (time: java.time.LocalTime) -> String,
    formatDate: (date: java.time.LocalDate) -> String
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Doctor info section with banner background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doctor_image2),
                    contentDescription = "Doctor",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = doctorName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Specialist",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Appointment status banner
        StatusBanner(status = appointment.status)

        // Appointment details
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Appointment Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date and time
                DetailRow(
                    icon = Icons.Default.CalendarToday,
                    title = "Date",
                    value = formatDate(appointment.date)
                )

                DetailRow(
                    icon = Icons.Default.Schedule,
                    title = "Time",
                    value = formatTime(appointment.time)
                )

                // Hospital & department
                DetailRow(
                    icon = Icons.Default.LocalHospital,
                    title = "Hospital",
                    value ="Main Hospital"
                )

                DetailRow(
                    icon = Icons.Default.MedicalServices,
                    title = "Department",
                    value = "General"
                )

                // Reason for visit
                DetailRow(
                    icon = Icons.Default.Info,
                    title = "Reason for Visit",
                    value = appointment.reason ?: "Regular checkup"
                )
            }
        }

        // Notes section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Personal Notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    IconButton(onClick = onEditNotes) {
                        Icon(
                            imageVector = Icons.Outlined.EditNote,
                            contentDescription = "Edit Notes",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isEditingNotes) {
                    // Edit notes field
                    OutlinedTextField(
                        value = notesText,
                        onValueChange = onNotesChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        placeholder = { Text("Add your notes here...") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Save button
                    Button(
                        onClick = onSaveNotes,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Save Notes")
                    }
                } else {
                    // Display notes
                    Text(
                        text = if (notesText.isNotEmpty()) notesText else "No notes added yet.",
                        fontSize = 16.sp,
                        color = if (notesText.isNotEmpty()) Color.DarkGray else Color.Gray
                    )
                }
            }
        }

        // Action buttons
        AnimatedVisibility(
            visible = appointment.status == AppointmentStatus.PENDING,
            enter = fadeIn(animationSpec = tween(300))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // QR Code button
                OutlinedButton(
                    onClick = onShowQRCode,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.QrCode,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Show QR Code")
                }

                // Cancel button
                Button(
                    onClick = onCancelAppointment,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel Appointment")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun StatusBanner(status: AppointmentStatus) {
    val (backgroundColor, textColor) = when (status) {
        AppointmentStatus.PENDING -> Pair(Color(0xFFFFF9C4), Color(0xFF8B6F00))
        AppointmentStatus.COMPLETED -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        AppointmentStatus.CANCELLED -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        AppointmentStatus.CONFIRMED -> TODO()
        AppointmentStatus.RESCHEDULED -> TODO()
    }

    val statusText = when (status) {
        AppointmentStatus.PENDING -> "Upcoming Appointment"
        AppointmentStatus.COMPLETED -> "Completed"
        AppointmentStatus.CANCELLED -> "Cancelled"
        AppointmentStatus.CONFIRMED -> TODO()
        AppointmentStatus.RESCHEDULED -> TODO()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = statusText,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontSize = 16.sp
        )
    }
}

@Composable
fun DetailRow(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun QRCodeDialog(
    qrCodeData: QRCodeData,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Appointment QR Code",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display QR Code image
                // In a real app, you would render the QR code from the data
                // Here we'll show a placeholder
                Image(
                    painter = painterResource(id = R.drawable.checked),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Show this QR code at the reception desk",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}