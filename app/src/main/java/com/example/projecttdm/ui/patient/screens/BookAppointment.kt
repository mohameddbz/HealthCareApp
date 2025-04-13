package com.example.projecttdm.ui.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun BookAppointmentScreen(onNextClicked: () -> Unit) {
    // State for selected date and time
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    // Current month to initialize the date picker
    val currentMonth = remember { YearMonth.now() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title Section
        Text(
            text = "Book Appointment",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Date Selection Section
        Text(
            text = "Select Date",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Date Picker Component
        DatePicker(
            selectedDate = selectedDate,
            initialMonth = currentMonth,
            onDateSelected = { date ->
                // Deselect date if the same one is selected again
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
                .background(Blue02, RoundedCornerShape(16.dp))
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time Selection Section
        Text(
            text = "Select Hour",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Time Slot Grid Component
        TimeSlotGrid(
            selectedTime = selectedTime,
            onTimeSelected = { time -> selectedTime = time },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        // Next Button
        Button(
            onClick = onNextClicked,
            enabled = selectedDate != null && selectedTime != null,
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
                "Next",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
