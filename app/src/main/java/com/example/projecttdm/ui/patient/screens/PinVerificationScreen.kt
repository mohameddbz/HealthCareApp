package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.ui.patient.components.BookAppointment.PinBoard


@Composable
fun PinVerificationScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val correctPin = "0000"
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                        tint = colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Enter Verification PIN",
                style = typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Description
            Text(
                text = "Please enter the 4-digit code sent to your phone",
                style = typography.bodyMedium.copy(
                    color = colorScheme.onSurfaceVariant,
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

