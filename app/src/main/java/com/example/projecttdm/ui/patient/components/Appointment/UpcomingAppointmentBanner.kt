package com.example.projecttdm.ui.patient.components.Appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.R
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.ui.patient.screens.WindowSize
import com.example.projecttdm.ui.patient.screens.WindowType
import com.example.projecttdm.viewmodel.AppointmentViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpcomingAppointmentBanner(
    viewModel: AppointmentViewModel,
    windowSize: WindowSize,
    onClick: () -> Unit = {}
) {
    // Collect state from ViewModel
    val appointments by viewModel.appointments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Find the first pending appointment if any exists
    val upcomingAppointment = appointments.firstOrNull {
        it.status == AppointmentStatus.PENDING
    }

    // Display loading state if needed
    if (isLoading) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }

    // No appointment found case
    if (upcomingAppointment == null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Upcoming Appointments",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Schedule a new appointment to see it here",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
        return
    }

    // Found appointment - display it
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // When clicked, show details of this appointment
                viewModel.getAppointmentDetails(upcomingAppointment.id)
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Appointment details section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Doctor avatar placeholder
                Image(
                    painter = painterResource(id = R.drawable.doctor_image2),
                    contentDescription = "Doctor",
                    modifier = Modifier
                        .size(when (windowSize.width) {
                            WindowType.Compact -> 100.dp
                            WindowType.Medium -> 120.dp
                            WindowType.Expanded -> 140.dp
                        }),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = viewModel.getDoctorName(upcomingAppointment.doctorId),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        text =  "Specialist",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Appointment time and date
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppointmentInfo(
                            icon = Icons.Default.Schedule,
                            info = viewModel.formatTime(upcomingAppointment.time)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        AppointmentInfo(
                            icon = Icons.Default.DateRange,
                            info = viewModel.formatDate(upcomingAppointment.date)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "View Details",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun AppointmentInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    info: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = info,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}