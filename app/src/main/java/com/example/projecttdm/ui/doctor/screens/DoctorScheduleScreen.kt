package com.example.projecttdm.ui.doctor.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.ui.doctor.components.DayItem
import com.example.projecttdm.ui.doctor.components.DaySelector
import com.example.projecttdm.ui.doctor.components.DurationItem
import com.example.projecttdm.ui.doctor.components.DurationSelector
import com.example.projecttdm.ui.doctor.components.TimeButton
import com.example.projecttdm.ui.doctor.components.TimePickerDialog
import com.example.projecttdm.ui.doctor.components.TimeSlot
import com.example.projecttdm.viewmodel.DoctorScheduleUiState
import com.example.projecttdm.viewmodel.DoctorScheduleViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

enum class TimePickerTarget {
    MORNING_START, MORNING_END,
    AFTERNOON_START, AFTERNOON_END,
    EVENING_START, EVENING_END
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorScheduleScreen(
    doctorId: Int,
    onNavigateBack: () -> Unit,
    viewModel: DoctorScheduleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDay by viewModel.selectedDay.observeAsState("")
    val selectedDuration by viewModel.selectedDuration.observeAsState(30)
    val startTime by viewModel.startTime.observeAsState("")
    val endTime by viewModel.endTime.observeAsState("")
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showTimePicker by remember { mutableStateOf(false) }
    var timePickerTarget by remember { mutableStateOf<TimePickerTarget?>(null) }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is DoctorScheduleUiState.Success -> {
                snackbarHostState.showSnackbar(
                    message = "Schedules created successfully!",
                    duration = SnackbarDuration.Short
                )
                viewModel.resetState()
            }
            is DoctorScheduleUiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = state.message,
                    duration = SnackbarDuration.Long
                )
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // Time picker dialog
    if (showTimePicker && timePickerTarget != null) {
        val initialHour = 8
        val initialMinute = 0
        val initialIsAm = true

        TimePickerDialog(
            showDialog = true,
            onDismiss = {
                showTimePicker = false
                timePickerTarget = null
            },
            onConfirm = { hour, minute, isAm ->
                // Convert to 24-hour format
                val formattedHour = if (isAm) {
                    if (hour == 12) 0 else hour
                } else {
                    if (hour == 12) 12 else hour + 12
                }

                val timeString = String.format("%02d:%02d", formattedHour, minute)

                when (timePickerTarget) {
                    TimePickerTarget.MORNING_START,
                    TimePickerTarget.AFTERNOON_START,
                    TimePickerTarget.EVENING_START -> viewModel.setStartTime(timeString)

                    TimePickerTarget.MORNING_END,
                    TimePickerTarget.AFTERNOON_END,
                    TimePickerTarget.EVENING_END -> viewModel.setEndTime(timeString)

                    else -> {} // Do nothing
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Planning of the week",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val days = listOf(
                    DayItem("Monday", "Mon", selectedDay == "Monday"),
                    DayItem("Tuesday", "Tue", selectedDay == "Tuesday"),
                    DayItem("Wednesday", "Wed", selectedDay == "Wednesday"),
                    DayItem("Thursday", "Thu", selectedDay == "Thursday"),
                    DayItem("Friday", "Fri", selectedDay == "Friday"),
                    DayItem("Saturday", "Sat", selectedDay == "Saturday"),
                    DayItem("Sunday", "Sun", selectedDay == "Sunday")
                )

                DaySelector(
                    days = days,
                    onDaySelected = { day ->
                        viewModel.setSelectedDay(day.name)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Duration selector
                Text(
                    text = "Time of an appointment",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val durations = listOf(
                    DurationItem(15, selectedDuration == 15),
                    DurationItem(20, selectedDuration == 20),
                    DurationItem(30, selectedDuration == 30),
                    DurationItem(40, selectedDuration == 40),
                    DurationItem(45, selectedDuration == 45),
                    DurationItem(60, selectedDuration == 60)
                )

                DurationSelector(
                    durations = durations,
                    onDurationSelected = { duration ->
                        viewModel.setSelectedDuration(duration.minutes)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Time slots section
                Text(
                    text = "Time Slots",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Morning slots
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Morning",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TimeButton(
                                slot = TimeSlot("morning_start", true, startTime.takeIf { it.isNotEmpty() }),
                                onTimeSelected = { time ->
                                    viewModel.setStartTime(time)
                                }
                            )

                            TimeButton(
                                slot = TimeSlot("morning_end", false, endTime.takeIf { it.isNotEmpty() }),
                                onTimeSelected = { time ->
                                    viewModel.setEndTime(time)
                                }
                            )
                        }
                    }
                }

                // Spacer for separation
                Spacer(modifier = Modifier.height(32.dp))

                // Aftrnoon slots
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Aftrnoon",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TimeButton(
                                slot = TimeSlot("morning_start", true, startTime.takeIf { it.isNotEmpty() }),
                                onTimeSelected = { time ->
                                    viewModel.setStartTime(time)
                                }
                            )

                            TimeButton(
                                slot = TimeSlot("morning_end", false, endTime.takeIf { it.isNotEmpty() }),
                                onTimeSelected = { time ->
                                    viewModel.setEndTime(time)
                                }
                            )
                        }
                    }
                }

                // Spacer for separation
                Spacer(modifier = Modifier.height(32.dp))

                // Create button
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.createSchedulesForDay(doctorId)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = viewModel.validateInputs() && uiState !is DoctorScheduleUiState.Loading
                ) {
                    if (uiState is DoctorScheduleUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Confirm")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cancel button
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}