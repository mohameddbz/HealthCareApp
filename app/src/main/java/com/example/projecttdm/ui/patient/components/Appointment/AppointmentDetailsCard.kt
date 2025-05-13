package com.example.projecttdm.ui.patient.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projecttdm.data.model.appointment2
import com.example.projecttdm.ui.patient.components.Appointment.DetailRow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentDetailsCard(appointment: appointment2) {
    val lightGrayBackground = Color(0xFFF5F5F5)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        color = lightGrayBackground
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Appointment Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            DetailRow("Date & Hour", appointment.date.toString())
            DetailRow("Package", appointment.reason)
            DetailRow("Duration", appointment.time.toString())
        }
    }
}