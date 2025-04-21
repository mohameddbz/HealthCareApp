package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.local.RescheduleReasonData
import com.example.projecttdm.ui.patient.components.Appointment.RadioGroupOptions
import com.example.projecttdm.ui.patient.components.BookAppointment.MultilineTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleReasonScreen(
    onNavigateBack: () -> Unit,
    onNext: () -> Unit
) {
    var selectedReason by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reschedule Appointment") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Reason for Schedule Change",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Radio options
            RadioGroupOptions(
                options = RescheduleReasonData.rescheduleReasons,
                selectedOption = selectedReason,
                onOptionSelected = { selectedReason = it }
            )

            // Display the multiline text field only when "Others" is selected
            if (selectedReason == "others") {
                MultilineTextField(
                    label = "Please specify",
                    value = additionalInfo,
                    onValueChange = { additionalInfo = it }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Next button at the bottom
            Button(
                onClick = { onNext()} ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Next", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}