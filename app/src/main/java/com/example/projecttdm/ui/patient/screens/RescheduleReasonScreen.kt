package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.ui.patient.components.Appointment.RadioGroupOptions
import com.example.projecttdm.ui.patient.components.BookAppointment.MultilineTextField
import com.example.projecttdm.viewmodel.ReasonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleReasonScreen(
    viewModel: ReasonViewModel = viewModel(factory = ReasonViewModel.Factory(LocalContext.current)),
    onNavigateBack: () -> Unit,
    onNext: () -> Unit
) {

    // Collect the state flows from the ViewModel
    val reasons by viewModel.reasons.collectAsState()
    val selectedReason by viewModel.selectedReason.collectAsState()
    val additionalInfo by viewModel.additionalInfo.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    // Snackbar host state for showing save confirmation
    val snackbarHostState = remember { SnackbarHostState() }

    // Show confirmation when custom reason is saved
    LaunchedEffect(isSaving) {
        if (!isSaving && selectedReason == "others" && additionalInfo.isNotBlank()) {
            snackbarHostState.showSnackbar("Custom reason saved for future use")
        }
    }

    // Callback to handle navigation and saving data
    val handleNext = {
        // Save any custom reason before navigating
        //viewModel.saveOtherReasonIfNeeded()
        onNext()
    }

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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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

            // Radio options using the reasons from ViewModel
            RadioGroupOptions(
                options = reasons,
                selectedOption = selectedReason,
                onOptionSelected = {
                    //viewModel.onReasonSelected(it)
                }
            )

            // Display the multiline text field only when "Others" is selected
            if (selectedReason == "others") {
                MultilineTextField(
                    label = "Please specify",
                    value = additionalInfo,
                    onValueChange = {
                        //viewModel.onAdditionalInfoChanged(it)
                    }
                )

                // Inform the user that their reason will be saved
                Text(
                    text = "Your custom reason will be saved for future use",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Next button at the bottom
            Button(
                onClick = handleNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6)
                ),
                shape = MaterialTheme.shapes.medium,
                // Disable button if no reason selected or if "others" selected but no info provided
                enabled = !isSaving && selectedReason.isNotEmpty() &&
                        (selectedReason != "others" || additionalInfo.isNotBlank())
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Next", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}