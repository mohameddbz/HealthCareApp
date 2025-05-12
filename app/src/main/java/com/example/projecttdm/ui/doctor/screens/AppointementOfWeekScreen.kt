package com.example.projecttdm.ui.doctor.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.Patient
import com.example.projecttdm.ui.doctor.components.AppointmentsList
import com.example.projecttdm.ui.doctor.components.EmptyAppointmentsView
import com.example.projecttdm.ui.doctor.components.WeekCalendar
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentOfWeekScreen(
    appointments: List<Appointment>,
    patients: Map<String, Patient>,
    onBrowseDoctors: () -> Unit,
    onAppointmentClick: (String) -> Unit,
    initialSelectedDate: LocalDate = LocalDate.now()
) {
    // Add state for selected date
    var selectedDate by remember { mutableStateOf(initialSelectedDate) }

    // Filter appointments for the selected date
    val filteredAppointments = appointments.filter { appointment ->
        appointment.date == selectedDate
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "My Appointments",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        WeekCalendar(
            selectedDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date // Update the selected date
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Use filtered appointments instead of all appointments
        if (filteredAppointments.isEmpty()) {
            EmptyAppointmentsView(onBrowseDoctors)
        } else {
            AppointmentsList(
                appointments = filteredAppointments,
                patients = patients,
                onAppointmentClick = onAppointmentClick
            )
        }
    }
}