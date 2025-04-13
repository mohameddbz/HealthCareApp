package com.example.projecttdm.ui.patient.components.Appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinBoardNumberButton(
    number: String,
    onClick: () -> Unit
) {
    val buttonBackgroundColor = Color(0xFFF5F5F5)

    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(buttonBackgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        ButtonText(number = number)
    }
}

@Composable
private fun ButtonText(number: String) {
    Text(
        text = number,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    )
}
