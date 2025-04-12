package com.example.projecttdm.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.projecttdm.theme.Blue01

@Composable

fun PinVerificationScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val pinLength = 4
    val correctPin = "0000"

    // Remove these dialog state variables as we'll handle them through navigation
    // var showSuccessDialog by remember { mutableStateOf(false) }
    // var showFailureDialog by remember { mutableStateOf(false) }

    val verifyPin = {
        if (pin.length == pinLength) {
            if (pin == correctPin) {
                // Navigate directly instead of showing dialog
                onSuccess()
            } else {
                // Navigate directly instead of showing dialog
                onFailure()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Enter Verification PIN",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Please enter the 4-digit code sent to your phone",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // PIN Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 60.dp)
            ) {
                repeat(pinLength) { index ->
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                color = if (pin.length > index) Color(0xFFEEEEEE) else Color(0xFFF5F5F5)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pin.length > index) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                            )
                        }
                    }
                }
            }

            // Number Pad
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Row 1-3
                listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9")
                ).forEach { rowNumbers ->
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        rowNumbers.forEach { number ->
                            NumberButton(number) {
                                if (pin.length < pinLength) {
                                    pin += number
                                    if (pin.length == pinLength) {
                                        verifyPin()
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom row
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    // Empty space for symmetry
                    Box(modifier = Modifier.size(64.dp))

                    NumberButton("0") {
                        if (pin.length < pinLength) {
                            pin += "0"
                            if (pin.length == pinLength) {
                                verifyPin()
                            }
                        }
                    }

                    // Delete button
                    IconButton(
                        onClick = { if (pin.isNotEmpty()) pin = pin.dropLast(1) },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Removed internal dialogs as they're now separate screens in navigation
    }
}

@Composable
fun NumberButton(
    number: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color(0xFFF5F5F5))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        )
    }
}

@Composable
fun SuccessDialog(
    onViewAppointment: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Blue Circle with Calendar Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0x4448A5FE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF4285F4), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Calendar Icon (simplified)
                        Text(
                            text = "📅",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }

                    // Small blue dots around the main circle
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        // Top dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF4285F4), CircleShape)
                                .align(Alignment.TopCenter)
                        )

                        // Bottom dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF4285F4), CircleShape)
                                .align(Alignment.BottomCenter)
                        )

                        // Left dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF4285F4), CircleShape)
                                .align(Alignment.CenterStart)
                        )

                        // Right dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF4285F4), CircleShape)
                                .align(Alignment.CenterEnd)
                        )

                        // Top-left dot
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color(0xFF4285F4), CircleShape)
                                .align(Alignment.TopStart)
                                .offset(x = 16.dp, y = 16.dp)
                        )

                        // Top-right dot
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color(0xFF4285F4), CircleShape)
                                .align(Alignment.TopEnd)
                                .offset(x = (-16).dp, y = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Congratulations!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4285F4)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Appointment successfully booked.\nYou will receive a notification and the\ndoctor you selected will contact you.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onViewAppointment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4285F4)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "View Appointment",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun FailureDialog(
    onTryAgain: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Red Circle with X Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0x44FF6B6B), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFFF6B6B), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // X Icon
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Failure",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Small red dots around the main circle
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        // Top dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFFFF6B6B), CircleShape)
                                .align(Alignment.TopCenter)
                        )

                        // Bottom dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFFFF6B6B), CircleShape)
                                .align(Alignment.BottomCenter)
                        )

                        // Left dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFFFF6B6B), CircleShape)
                                .align(Alignment.CenterStart)
                        )

                        // Right dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFFFF6B6B), CircleShape)
                                .align(Alignment.CenterEnd)
                        )

                        // Top-left dot
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color(0xFFFF6B6B), CircleShape)
                                .align(Alignment.TopStart)
                                .offset(x = 16.dp, y = 16.dp)
                        )

                        // Top-right dot
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color(0xFFFF6B6B), CircleShape)
                                .align(Alignment.TopEnd)
                                .offset(x = (-16).dp, y = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Oops, Failed!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B6B)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Appointment failed. Please check\nyour internet connection then try\nagain.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onTryAgain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4285F4)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Try Again",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun PinVerificationFlow(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    PinVerificationScreen(
        onBackClicked = onBackClicked,
        onSuccess = onSuccess,
        onFailure = onFailure
    )
}