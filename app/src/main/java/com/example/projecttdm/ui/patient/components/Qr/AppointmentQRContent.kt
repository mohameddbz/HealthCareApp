package com.example.projecttdm.ui.patient.components.Qr

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.utils.QRCodeGenerator
import com.example.projecttdm.utils.shareQRCode
import com.example.projecttdm.viewmodel.AppointmentViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentQRContent(
    appointment: Appointment,
    qrCodeContent: String,
    viewModel: AppointmentViewModel,
    onCancelClick: () -> Unit
) {
    val context = LocalContext.current
    var showNotesDialog by remember { mutableStateOf(false) }
    var appointmentNotes by remember { mutableStateOf(appointment.notes ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Doctor name header
        Text(
            text = "Appointment with",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Dr. ${viewModel.getDoctorName(appointment.doctorId)}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date and time with calendar icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Date",
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(8.dp))

            val dateString = appointment.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val timeString = appointment.time.format(DateTimeFormatter.ofPattern("HH:mm"))

            Text(
                text = "$dateString | $timeString",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Display reason for appointment
        if (appointment.reason.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Reason: ${appointment.reason}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

//        // Display location (mock data since not in model)
//        Spacer(modifier = Modifier.height(8.dp))
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Icon(
//                imageVector = Icons.Default.LocationOn,
//                contentDescription = "Location",
//                tint = MaterialTheme.colorScheme.primary
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Text(
//                text = "Medical Center, Floor 3, Room 305",
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }

//        Spacer(modifier = Modifier.height(24.dp))
//
        // QR Code
        val qrBitmap = QRCodeGenerator.generate(qrCodeContent, 512)
        qrBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Appointment QR Code",
                modifier = Modifier.size(300.dp)
            )
        }

        // Info text
        Text(
            text = "Show this QR code at reception",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Share button
//            Button(
//                onClick = {
//                    qrBitmap?.let { bitmap ->
//                        shareQRCode(context, bitmap, appointment)
//                    }
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(48.dp)
//                    .padding(end = 8.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.secondary
//                ),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Share,
//                    contentDescription = "Share",
//                    modifier = Modifier.size(20.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Share")
//            }
//
//            // Add Notes button
//            Button(
//                onClick = { showNotesDialog = true },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(48.dp)
//                    .padding(start = 8.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                ),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Notes,
//                    contentDescription = "Notes",
//                    modifier = Modifier.size(20.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Notes")
//            }
            // Cancel button
            Button(
                onClick = {onCancelClick()},
                modifier = Modifier
                    .weight(0.7f)
                    .height(48.dp)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Cancel")
            }
        }

        //Spacer(modifier = Modifier.height(16.dp))


    }

    // Notes dialog
    if (showNotesDialog) {
        AlertDialog(
            onDismissRequest = { showNotesDialog = false },
            title = { Text("Appointment Notes") },
            text = {
                OutlinedTextField(
                    value = appointmentNotes,
                    onValueChange = { appointmentNotes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = { Text("Add notes about your appointment...") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateAppointmentNotes(appointment.id, appointmentNotes)
                        showNotesDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showNotesDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}