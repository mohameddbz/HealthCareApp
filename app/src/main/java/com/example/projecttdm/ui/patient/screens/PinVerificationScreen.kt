package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.ui.patient.components.Appointment.PinBoard


@Composable
fun PinVerificationScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val correctPin = "0000" // Correct pin for verification

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
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

            // Title
            Text(
                text = "Enter Verification PIN",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Description
            Text(
                text = "Please enter the 4-digit code sent to your phone",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // PinBoard for entering PIN
            PinBoard(
                pinLength = 4,
                onPinComplete = { enteredPin ->
                    if (enteredPin == correctPin) onSuccess() else onFailure()
                }
            )
        }
    }
}
