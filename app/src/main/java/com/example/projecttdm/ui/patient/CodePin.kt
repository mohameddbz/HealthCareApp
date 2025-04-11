package com.example.projecttdm.ui.patient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.DialogProperties
import com.example.projecttdm.R
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.theme.Blue02
import com.example.projecttdm.theme.Gray01
import com.example.projecttdm.theme.Gray02

@Composable
fun PinVerificationScreen(
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val pinLength = 4
    val correctPin = "0000"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Enter Verification PIN",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Blue01,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            repeat(pinLength) { index ->
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .border(
                            width = 2.dp,
                            color = if (pin.length > index) Blue01 else Gray01,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = if (pin.length > index) Blue02 else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (pin.length > index) {
                        Text("•", style = MaterialTheme.typography.headlineLarge, color = Blue01)
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row 1-3
            listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9")
            ).forEach { rowNumbers ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowNumbers.forEach { number ->
                        RectangularNumberButton(number) {
                            if (pin.length < pinLength) pin += number
                        }
                    }
                }
            }

            // Bottom row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RectangularIconButton(Icons.Default.Delete) {
                    if (pin.isNotEmpty()) pin = pin.dropLast(1)
                }
                RectangularNumberButton("0") {
                    if (pin.length < pinLength) pin += "0"
                }
                RectangularActionButton(
                    text = "OK",
                    onClick = { if (pin == correctPin) onSuccess() else onFailure() },
                    enabled = pin.length == pinLength
                )
            }
        }
    }
}

@Composable
fun RectangularNumberButton(
    number: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(72.dp)
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Gray02
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Gray01)
    ) {
        Text(number, style = MaterialTheme.typography.headlineMedium, fontSize = 24.sp)
    }
}

@Composable
fun RectangularIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(72.dp)
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Gray02
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Gray01)
    ) {
        Icon(icon, contentDescription = null, tint = Gray02)
    }
}

@Composable
fun RectangularActionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(72.dp)
            .height(60.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue01,
            contentColor = Color.White,
            disabledContainerColor = Gray01.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, fontSize = 18.sp)
    }
}

@Composable
fun SuccessPopup(
    onViewAppointment: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.checked),
                    contentDescription = "Success",
                    modifier = Modifier.size(64.dp),

                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Congratulations!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Gray02,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Column(Modifier.padding(vertical = 8.dp)) {
                    Text("Appointment successfully booked.", textAlign = TextAlign.Center)
                    Text("You will receive a notification and the", textAlign = TextAlign.Center)
                    Text("doctor you selected will contact you.", textAlign = TextAlign.Center)
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onViewAppointment,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue01,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("View Appointment")
                    }

                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Gray01)
                    ) {
                        Text("Cancel", color = Gray02)
                    }
                }
            }
        }
    }
}

@Composable
fun FailurePopup(
    onTryAgain: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.cancel),
                    contentDescription = "Failure",
                    modifier = Modifier.size(64.dp),

                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Oops, Failed!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Gray02,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Appointment failed. Please check your internet connection then try again.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onTryAgain,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue01,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Try Again")
                    }

                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Gray01)
                    ) {
                        Text("Cancel", color = Gray02)
                    }
                }
            }
        }
    }
}

@Composable

fun PinVerificationFlow(
    onVerified: (isSuccess: Boolean) -> Unit
) {
    var showSuccess by remember { mutableStateOf(false) }
    var showFailure by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Always show the PIN screen in the background
        PinVerificationScreen(
            onSuccess = {
                showSuccess = true
                onVerified(true)
            },
            onFailure = {
                showFailure = true
                onVerified(false)
            }
        )

        // Show popups on top when needed
        if (showSuccess) {
            SuccessPopup(
                onViewAppointment = { showSuccess = false },
                onCancel = { showSuccess = false }
            )
        }

        if (showFailure) {
            FailurePopup(
                onTryAgain = { showFailure = false },
                onCancel = { showFailure = false }
            )
        }
    }
}