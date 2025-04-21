package com.example.projecttdm.ui.patient.components.Appointment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.AppointmentStatus

@Composable
fun EmptyAppointmentsList(
    status: AppointmentStatus,
    onFindDoctors: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = when (status) {
                AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED, AppointmentStatus.PENDING -> Icons.Outlined.EventAvailable
                AppointmentStatus.COMPLETED -> Icons.Outlined.EventAvailable
                AppointmentStatus.CANCELLED -> Icons.Outlined.EventBusy
                AppointmentStatus.RESCHEDULED -> TODO()
            },
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when (status) {
                AppointmentStatus.CONFIRMED -> "No confirmed appointments"
                AppointmentStatus.PENDING -> "No pending appointments"
                AppointmentStatus.COMPLETED -> "No completed appointments"
                AppointmentStatus.CANCELLED -> "No cancelled appointments"
                AppointmentStatus.RESCHEDULED -> TODO()
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Schedule an appointment with a doctor to get started!",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFindDoctors,
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Find Doctors")
        }
    }
}