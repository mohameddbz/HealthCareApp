package com.example.projecttdm.ui.doctor.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.data.model.NextAppointementsResponse
import com.example.projecttdm.doctorviewmodel.CalendarViewModel
import com.example.projecttdm.ui.doctor.components.AppointmentCard
import com.example.projecttdm.ui.doctor.components.AppointmentsPendigList
import com.example.projecttdm.ui.doctor.components.DayCell
import com.example.projecttdm.ui.doctor.components.MonthHeader
import com.example.projecttdm.ui.doctor.components.WeekdaysHeader
import com.example.projecttdm.ui.doctor.components.getCalendarDays
import com.example.projecttdm.ui.doctor.components.isSameDay
import java.text.SimpleDateFormat
import java.util.*

// Couleurs de l'application
val LightBlue = Color(0xFFBBDEFB)
val DarkBlue = Color(0xFF0D47A1)

data class CalendarDay(
    val date: Date,
    val isCurrentMonth: Boolean = true,
    val hasAppointment: Boolean = false,
    val isToday: Boolean = false
)



@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onDateSelected: (Date) -> Unit,
    appointmentsOfDaySelected: NextAppointementsResponse?,
    isLoading : Boolean,
    onNext : () -> Unit = {},
    viewModel: CalendarViewModel,
    onRefuse : () -> Unit = {},

) {
    val calendar = remember { Calendar.getInstance() }
    var currentDate by remember { mutableStateOf(calendar.time) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var showAppointments by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.onPrimary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                MonthHeader(
                    currentDate = currentDate,
                    onPreviousMonth = {
                        calendar.time = currentDate
                        calendar.add(Calendar.MONTH, -1)
                        currentDate = calendar.time
                    },
                    onNextMonth = {
                        calendar.time = currentDate
                        calendar.add(Calendar.MONTH, 1)
                        currentDate = calendar.time
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Jours de la semaine
                WeekdaysHeader()

                Spacer(modifier = Modifier.height(8.dp))

                // Grille du calendrier
                CalendarGrid(
                    currentDate = currentDate,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        showAppointments = false
                        selectedDate = date
                        onDateSelected(date)
                        showAppointments = true

                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Liste des rendez-vous
        AnimatedVisibility(
            visible = showAppointments,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AppointmentsPendigList(
                date = selectedDate,
                appointments = appointmentsOfDaySelected,
                isLoading= isLoading,
                onNext = onNext,
                viewModel =  viewModel,
                onRefuse = onRefuse
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentDate: Date,
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit
) {
    val calendarDays = remember(currentDate) {
        getCalendarDays(currentDate)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = Modifier.height(270.dp)
    ) {
        items(calendarDays) { day ->
            DayCell(
                day = day,
                isSelected = selectedDate?.let { isSameDay(it, day.date) } ?: false,
                onDateSelected = onDateSelected
            )
        }
    }
}


// Extension pour capitaliser la première lettre
fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

// Fonction principale qui montre comment utiliser ce calendrier
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarApp(
    viewModel: CalendarViewModel = viewModel(),
    onBackPressed: () -> Unit = {},
    onNext : () -> Unit = {},
    onRefuse : () -> Unit = {}
) {
    val appointmentsOftheDateSelected by viewModel.appointments.collectAsState()

    val isLoading  by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
            CalendarScreen(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                onDateSelected = { date ->
                    // Ici vous pouvez faire votre requête pour récupérer les rendez-vous
                    println("Date sélectionnée: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)}")
                    viewModel.loadAppointmentsForDate(date)
                },
                appointmentsOfDaySelected = appointmentsOftheDateSelected,
                isLoading  = isLoading,
                onNext = onNext,
                viewModel = viewModel,
                onRefuse = onRefuse
            )
        }

}
