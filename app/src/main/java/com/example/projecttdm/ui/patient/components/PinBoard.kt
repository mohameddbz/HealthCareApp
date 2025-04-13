package com.example.projecttdm.ui.patient.components



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinBoard(
    pinLength: Int = 4,
    onPinComplete: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }

    val handleDigitClick: (String) -> Unit = { digit ->
        if (pin.length < pinLength) {
            pin += digit
            if (pin.length == pinLength) {
                onPinComplete(pin)
            }
        }
    }

    val handleDelete: () -> Unit = {
        if (pin.isNotEmpty()) {
            pin = pin.dropLast(1)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // PIN Fields
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
        listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9")
        ).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                row.forEach { num ->
                    NumberButton(number = num) {
                        handleDigitClick(num)
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Box(modifier = Modifier.size(64.dp)) // empty

            NumberButton("0") {
                handleDigitClick("0")
            }

            IconButton(
                onClick = { handleDelete() },
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
