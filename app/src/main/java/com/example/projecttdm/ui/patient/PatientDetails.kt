package com.example.projecttdm.ui.patient

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.theme.*

@Composable
fun PatientDetailsScreen(onNextClicked: () -> Unit) {
    var fullName by remember { mutableStateOf("Andrew Ainsley") }
    var gender by remember { mutableStateOf("Male") }
    var age by remember { mutableStateOf("27") }
    var problemDescription by remember { mutableStateOf(
        "Hello Dr. Jenny, I have a problem with my immune system. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua."
    ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Patient Details",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Blue01
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Full Name Section
        Column {
            Text(
                text = "Full Name",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Gray01,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Blue01,
                    unfocusedIndicatorColor = Gray02,
                    cursorColor = Blue01
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                singleLine = true
            )
        }

        // Gender Section
        Column {
            Text(
                text = "Gender",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Gray01,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenderOption(
                    text = "Male",
                    isSelected = gender == "Male",
                    onSelect = { gender = "Male" }
                )
                GenderOption(
                    text = "Female",
                    isSelected = gender == "Female",
                    onSelect = { gender = "Female" }
                )
            }
        }

        // Age Section
        Column {
            Text(
                text = "Your Age",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Gray01,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = age,
                onValueChange = { if (it.all { char -> char.isDigit() }) age = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Blue01,
                    unfocusedIndicatorColor = Gray02,
                    cursorColor = Blue01
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )
        }

        // Problem Description Section
        Column {
            Text(
                text = "Write Your Problem",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Gray01,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = problemDescription,
                onValueChange = { problemDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Blue01,
                    unfocusedIndicatorColor = Gray02,
                    cursorColor = Blue01
                ),
                shape = RoundedCornerShape(12.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                maxLines = Int.MAX_VALUE,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        Button(
            onClick = onNextClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue01,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Text(
                text = "Next",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
private fun GenderOption(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val containerColor = if (isSelected) Blue02 else Color.White
    val contentColor = if (isSelected) Blue01 else Gray01
    val borderColor = if (isSelected) Blue01 else Gray02

    OutlinedButton(
        onClick = onSelect,

        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp,

        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp) // Changed from outlinedButtonElevation
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}