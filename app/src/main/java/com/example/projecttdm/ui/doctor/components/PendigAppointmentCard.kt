package com.example.projecttdm.ui.doctor.components
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.NextAppointment

@Composable
fun AppointmentCard(appointment: NextAppointment, onConfirm: () -> Unit, onCancel: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded },
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top section with photo and basic info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile Image
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    appointment.imageUrl?.let { imageBlob ->
                        val byteArray = imageBlob.data.map { it.toByte() }.toByteArray()

                        val imageBitmap = remember(byteArray) {
                            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)?.asImageBitmap()
                        }

                        imageBitmap?.let {
                            Image(
                                bitmap = it,
                                contentDescription = "Patient ${appointment.fullname}",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Name and appointment info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dr. ${appointment.fullname}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    // Number of visits
                    Row {
                        Text(
                            text = "Previous visits:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = (appointment.numberOfVisit -1 ).toString(),
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Today | ${appointment.start_time}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Expand icon
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier
                        .rotate(rotationState)
                        .size(24.dp),
                    tint = Color.Gray
                )
            }

            // Expandable section
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Reason for visit
                    if (appointment.reason.isNotEmpty()) {
                        Text(
                            text = "Reason for visit:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = appointment.reason,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }


                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween ,
            ) {
                // Cancel Button
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(100.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    border = BorderStroke(1.dp, Color(0xFF4285F4))
                ) {
                    Text("Confirm" , fontWeight = FontWeight.SemiBold , fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Reschedule Button
                Button(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp).border(
                            border = BorderStroke(2.dp, color =androidx.compose.material3.MaterialTheme.colorScheme.primary ),
                            shape = RoundedCornerShape(100.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor =androidx.compose.material3.MaterialTheme.colorScheme.primary
                    ),

                ) {
                    Text("Refuse",fontWeight = FontWeight.SemiBold , fontSize = 18.sp)
                }
            }
        }
    }
}
