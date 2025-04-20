package com.example.projecttdm.ui.patient.components.Appointment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.theme.BluePrimary
import com.example.projecttdm.theme.BlueSecondary
import com.example.projecttdm.theme.ErrorRed
import com.example.projecttdm.theme.SuccessGreen
import com.example.projecttdm.theme.canceledNotification
import com.example.projecttdm.theme.successNotification
import com.example.projecttdm.theme.textNotification
import com.example.projecttdm.theme.yellowNotification
import com.example.projecttdm.ui.patient.screens.capitalizeFirst
import com.example.projecttdm.ui.patient.screens.formattedDate
import com.example.projecttdm.ui.patient.screens.formattedTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PendingCard(
    onClick: () -> Unit,
    appointment: Appointment,
    doctor: Doctor?,
    onCardClick: () -> Unit,
    onCancelClick: () -> Unit,
    onRescheduleClick: () -> Unit
) {


    val statusColors = mapOf(
        AppointmentStatus.PENDING to yellowNotification,       // Yellow notification background for pending
        AppointmentStatus.CONFIRMED to successNotification,    // Success notification bg for confirmed
        AppointmentStatus.COMPLETED to BlueSecondary,          // Blue secondary for completed
        AppointmentStatus.CANCELLED to canceledNotification    // Canceled notification bg for cancelled
    )

    val statusTextColors = mapOf(
        AppointmentStatus.PENDING to BluePrimary,       // Primary blue for pending text
        AppointmentStatus.CONFIRMED to SuccessGreen,    // Success green for confirmed text
        AppointmentStatus.COMPLETED to Blue01,          // Blue variant for completed text
        AppointmentStatus.CANCELLED to ErrorRed         // Error red for cancelled text
    )

    val statusColor = statusColors[appointment.status] ?: BlueSecondary
    val statusTextColor = statusTextColors[appointment.status] ?: Blue01

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        Box {
            // Status badge row with left alignment
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.Absolute.Right,  // Changed to Start for left alignment
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = statusColor,
                    border = BorderStroke(1.dp, statusTextColor.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusTextColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = appointment.status.name.lowercase().capitalizeFirst(),
                            fontSize = 12.sp,
                            color = textNotification,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            ) {

                // Doctor Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center

                    ) {
                        when {
                            doctor?.imageResId != null -> {
                                Image(
                                    painter = painterResource(id = doctor.imageResId),
                                    contentDescription = "Doctor ${doctor.name}",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            else -> {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Default profile icon",
                                    tint = Color.Black,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Doctor info + date/time
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Dr. ${doctor?.name}" ?: "Unknown Doctor",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        doctor?.specialty?.let { specialty ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.MedicalServices,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = specialty.name,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Date and Time Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = appointment.formattedDate(),
                                color = MaterialTheme.colorScheme.outlineVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                            )
                            Text(
                                text = " | ",
                                color = MaterialTheme.colorScheme.outlineVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Icon(
                                imageVector = Icons.Outlined.AccessTime,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = appointment.formattedTime(),
                                color = MaterialTheme.colorScheme.outlineVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }


                // Reason for appointment (if present)
                if (appointment.reason.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = appointment.reason,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Only show actions for PENDING or CONFIRMED appointments
                if (appointment.status == AppointmentStatus.PENDING ||
                    appointment.status == AppointmentStatus.CONFIRMED
                ) {

                    Divider(color = Color.LightGray.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        OutlinedButton(
                            onClick = onRescheduleClick,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White,
                            ),
                            border = BorderStroke(1.dp, Color(0xFF3F51B5)),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reschedule", fontSize = 13.sp)
                        }

                        Button(
                            onClick = onCancelClick,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.5f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cancel", fontSize = 13.sp)
                        }
                    }
                }

                if (
                    appointment.status == AppointmentStatus.COMPLETED
                ) {

                    Divider(color = Color.LightGray.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        OutlinedButton(
                            onClick = { },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = Color.White,
                            ),
                            border = BorderStroke(1.dp, Color(0xFF3F51B5)),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Book Again", fontSize = 13.sp)
                        }

                        Button(
                            onClick = {  },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.5f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Persccription", fontSize = 13.sp)
                        }
                    }
                }

            }
            if (appointment.status == AppointmentStatus.CONFIRMED) {
                IconButton(
                    onClick = { onClick() },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.QrCode2,
                        contentDescription = "QR Code",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }


        }

    }
}