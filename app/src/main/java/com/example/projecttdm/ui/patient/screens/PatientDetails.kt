package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.ui.patient.components.BookAppointment.GenderDropdown
import com.example.projecttdm.ui.patient.components.BookAppointment.LabeledTextField
import com.example.projecttdm.viewmodel.PatientDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    onBackClicked: () -> Unit,
    onNextClicked: () -> Unit,
    patientId: String? = null,
    viewModel: PatientDetailsViewModel = viewModel()
) {
    // Load patient data if ID is provided
    LaunchedEffect(patientId) {
        patientId?.let {
            viewModel.loadPatient(it)
        }
    }

    // Collect UI state from ViewModel
    val fullName by viewModel.fullName.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val age by viewModel.age.collectAsState()
    val problemDescription by viewModel.problemDescription.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    // Handle successful save
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onNextClicked()
        }
    }

    // Gender dropdown state
    var showGenderDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header with back button and title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Text(
                text = "Patient Details",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Full Name Section


            Box(
                modifier = Modifier
                    .fillMaxWidth()


            ) {
                LabeledTextField(
                    label = "Full Name",
                    value = fullName,
                    onValueChange = { viewModel.setFullName(it) }
                )
            }



            Box(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                GenderDropdown(
                    selectedGender = gender,
                    onGenderSelected = { viewModel.setGender(it) },
                    expanded = showGenderDropdown,
                    onExpandedChange = { showGenderDropdown = it }
                )
            }



            Box(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                LabeledTextField(
                    label = "Your Age",
                    value = age,
                    onValueChange = { viewModel.setAge(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }



            Box(
                modifier = Modifier
                    .fillMaxWidth()

                    .heightIn(min = 120.dp)
            ) {
                LabeledTextField(
                    label = "Write Your Problem",
                    value = problemDescription,
                    onValueChange = { viewModel.setProblemDescription(it) },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions.Default
                )
            }

            // Error message if any
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next Button
        Button(
            onClick = { viewModel.savePatient() },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue01,
                disabledContainerColor = Color(0xFFBBCBF1),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Save Patient Details",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        // Loading indicator
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = Blue01)
            }
        }
    }
}