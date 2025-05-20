package com.example.projecttdm.ui.doctor.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projecttdm.data.model.NextAppointementsResponse
import com.example.projecttdm.doctorviewmodel.CalendarViewModel
import com.example.projecttdm.ui.doctor.screens.LightBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppointmentsPendigList(
    date: Date?,
    appointments: NextAppointementsResponse?,
    isLoading: Boolean,
    onNext : () -> Unit = {},
    viewModel : CalendarViewModel,
    onRefuse : () -> Unit = {},

) {
    val scrollState = rememberLazyListState()

    if (isLoading) {
        // Show loading indicator
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading appointments...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                // Title with selected date
                date?.let {
                    val dateFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())
                    Text(
                        text = dateFormat.format(it).replaceFirstChar { c -> c.uppercase() },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Divider(color = LightBlue, thickness = 1.dp)
            }

            if (appointments != null) {
                if (appointments.appointments.isNullOrEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No appointments for this day",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(appointments.appointments) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            onCancel = {
                                viewModel.cancelAppointment(appointment.appointement_id)
                                onRefuse()},
                            onConfirm = {
                                viewModel.confirmAppointment(appointment.appointement_id)
                               onNext()
                            }
                        )
                        Divider(
                            color = LightBlue,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
