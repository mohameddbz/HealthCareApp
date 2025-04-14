package com.example.projecttdm.ui.patient.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.theme.Gray02
import com.example.projecttdm.theme.Gray01
import com.example.projecttdm.theme.Blue01
import com.example.projecttdm.theme.Blue02
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import com.example.projecttdm.ui.patient.components.Appointment.DatePicker
import com.example.projecttdm.ui.patient.components.Appointment.TimeSlotGrid


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen( onNextClicked: () -> Unit) {
    val scrollState = rememberScrollState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    val currentMonth = remember { YearMonth.now() }

    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Book Appointment",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Gray02,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Calendar Section
        Text(
            text = "Select Date",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Gray02,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .padding(bottom = 4.dp)
                .align(Alignment.Start)
        )

        DatePicker(
            selectedDate = selectedDate,
            initialMonth = currentMonth,
            onDateSelected = { date ->
                if (selectedDate == date) {
                    selectedDate = null
                    selectedTime = null
                } else {
                    selectedDate = date
                    selectedTime = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Blue02, MaterialTheme.shapes.medium)
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time Slots Section
        selectedDate?.let {
            Text(
                text = "Select Hour",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Gray02,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 4.dp)
            )

            TimeSlotGrid(
                selectedTime = selectedTime,
                onTimeSelected = { time -> selectedTime = time },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Next Button
        Button(
            onClick = onNextClicked,
            enabled = selectedDate != null && selectedTime != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue01,
                disabledContainerColor = Gray01,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Next",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp
                )
            )
        }
    }
}

