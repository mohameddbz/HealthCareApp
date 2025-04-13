package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.ui.patient.components.Appointment.GenderDropdown
import com.example.projecttdm.ui.patient.components.Appointment.LabeledTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(onBackClicked: () -> Unit, onNextClicked: () -> Unit) {
    // State management for form values
    var fullName by remember { mutableStateOf("Andrew Ainsley") }
    var gender by remember { mutableStateOf("Male") }
    var showGenderDropdown by remember { mutableStateOf(false) }
    var age by remember { mutableStateOf("27") }
    var problemDescription by remember { mutableStateOf(
        "Hello Dr. Jenny, I have a problem with my immune system. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Patient Details",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Full Name
            LabeledTextField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it }
            )

            // Gender Dropdown
            GenderDropdown(
                selectedGender = gender,
                onGenderSelected = { gender = it },
                expanded = showGenderDropdown,
                onExpandedChange = { showGenderDropdown = it }
            )

            // Age (Number input with validation)
            LabeledTextField(
                label = "Your Age",
                value = age,
                onValueChange = { if (it.all(Char::isDigit)) age = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            // Problem Description (Multi-line input)
            LabeledTextField(
                label = "Write Your Problem",
                value = problemDescription,
                onValueChange = { problemDescription = it },
                singleLine = false,
                keyboardOptions = KeyboardOptions.Default
            )

            Spacer(modifier = Modifier.weight(1f))

            // Next Button
            Button(
                onClick = onNextClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5),
                    contentColor = Color.White
                )
            ) {
                Text("Next", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
