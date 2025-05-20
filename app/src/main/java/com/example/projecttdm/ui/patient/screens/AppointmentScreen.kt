package com.example.projecttdm.ui.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.patient.PatientRoutes
import com.example.projecttdm.ui.patient.components.Appointment.EmptyAppointmentsList
import com.example.projecttdm.ui.patient.components.Appointment.PendingCard
import com.example.projecttdm.ui.patient.components.Appointment.SearchBar
import com.example.projecttdm.ui.patient.components.Qr.AppointmentQRDialog
import com.example.projecttdm.viewmodel.AppointmentViewModel
import com.example.projecttdm.viewmodel.DoctorListViewModel
import com.example.projecttdm.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    navController: NavController,
    viewModel: AppointmentViewModel,
    doctorViewModel: DoctorListViewModel,
) {

    val selectedTab by viewModel.selectedTab.collectAsState()
    val appointments by viewModel.appointments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val filteredDoctors by doctorViewModel.filteredDoctors.collectAsState()
    var showSearchBar by remember { mutableStateOf(false) }
    val showDialog by viewModel.showQRCodeDialog.collectAsState()


//    if (showDialog && viewModel.selectedAppointment.value != null) {
//        AppointmentQRDialog(
//            appointment = viewModel.selectedAppointment.value!!,
//            qrCodeContent = viewModel.qrCodeContent.value ?: "",
//            viewModel = viewModel,
//            onDismiss = { viewModel.closeQRCodeDialog() }
//        )
//    }


//    LaunchedEffect(true) {
////        doctorViewModel.loadDoctors()
////        doctorViewModel.loadSpecialties()
////        doctorViewModel.setupFiltering()
//
//    }

    LaunchedEffect(key1 = true) {
        viewModel.refreshAppointments()
        viewModel.errorMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    var showRescheduleDialog by remember { mutableStateOf(false) }
    var appointmentToReschedule by remember { mutableStateOf<Appointment?>(null) }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "My Appointment",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "App Logo",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSearchBar = !showSearchBar }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        IconButton(onClick = { /* More options if needed */ }) {
                            Text(
                                text = "⋯",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                )

                AnimatedVisibility(
                    visible = showSearchBar || searchQuery.isNotEmpty(),
                    enter = fadeIn(tween(300)),
                    exit = fadeOut(tween(300)),
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = {  },
                        onClearQuery = {  },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ScrollableTabRow(
                selectedTabIndex = AppointmentStatus.values().indexOf(selectedTab),
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    val index = AppointmentStatus.values().indexOf(selectedTab)
                    if (index in tabPositions.indices) {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                            height = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                AppointmentStatus.values()
                    .filter { it != AppointmentStatus.RESCHEDULED }
                    .forEach { status ->
                        Tab(
                            selected = status == selectedTab,
                            onClick = { viewModel.selectTab(status) },
                            text = {
                                Text(
                                    text = status.name.lowercase().capitalizeFirst(),
                                    color = if (status == selectedTab) MaterialTheme.colorScheme.primary else Color.Gray,
                                    fontWeight = if (status == selectedTab) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }

            }

            if (appointments.isEmpty() && !isLoading) {
                EmptyAppointmentsList(
                    status = selectedTab,
                    onFindDoctors = { navController.navigate("doctor_search") }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(appointments, key = { it.id }) { appointment ->
                        val doctor = filteredDoctors.find { it.id == appointment.doctorId }
                        PendingCard(
                            onClick = { appointmentId ->
                                navController.navigate("${PatientRoutes.AppointmentQR.route}/$appointmentId") },
                            appointment = appointment,
                            doctor = doctor,
                            onCardClick = { navController.navigate("appointment_details/${appointment.id}") },
                            onCancelClick = { appointmentId ->
                                navController.navigate("${PatientRoutes.CancelReason.route}/$appointmentId") },
                            onRescheduleClick = { navController.navigate(PatientRoutes.RescheduleReason.route) }
                        )
                    }
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        // Future: Reschedule Dialog
        if (showRescheduleDialog && appointmentToReschedule != null) {
            // DateTimePickerDialog(...) - your custom logic here
        }
    }
}

// Helper function to capitalize first letter
fun String.capitalizeFirst(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

// Extension functions with null checks
@RequiresApi(Build.VERSION_CODES.O)
fun Appointment.formattedDate(): String {
    val today = LocalDate.now()
    return when {
        date == null -> "Date not set"
        date.isEqual(today) -> "Today"
        date.isEqual(today.plusDays(1)) -> "Tomorrow"
        date.isEqual(today.minusDays(1)) -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Appointment.formattedTime(): String {
    return if (time == null) {
        "Time not set"
    } else {
        time.format(DateTimeFormatter.ofPattern("h:mm a"))
    }
}