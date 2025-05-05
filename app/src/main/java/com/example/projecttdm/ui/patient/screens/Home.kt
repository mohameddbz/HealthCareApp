package com.example.projecttdm.ui.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.common.components.DeconnectionButton
import com.example.projecttdm.ui.common.components.UserProfileImage
import com.example.projecttdm.ui.patient.PatientRoutes
import com.example.projecttdm.ui.patient.components.Appointment.UpcomingAppointmentBanner
import com.example.projecttdm.ui.patient.components.CostumSearchBar
import com.example.projecttdm.ui.patient.components.DoctorCard
import com.example.projecttdm.ui.patient.components.DoctorSpecialitySection
import com.example.projecttdm.ui.patient.components.UpcomingAppointmentBannner
import com.example.projecttdm.viewmodel.AppointmentViewModel
import com.example.projecttdm.viewmodel.DoctorListViewModel
import com.example.projecttdm.viewmodel.HomeViewModel

class WindowSize(val width: WindowType, val height: WindowType)

enum class WindowType { Compact, Medium, Expanded }

fun getWindowType(size: Int): WindowType = when {
    size < 600 -> WindowType.Compact
    size < 840 -> WindowType.Medium
    else -> WindowType.Expanded
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    doctorListViewModel: DoctorListViewModel = viewModel(),
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
    appintementViewModel: AppointmentViewModel = viewModel(),
    onDoctorClick: (String) -> Unit,
) {
    val searchQuery by doctorListViewModel.searchQuery.collectAsState()
    val windowSize = calculateWindowSize()
    val currentUser by homeViewModel.currentUser.collectAsState()
    val doctorsState by doctorListViewModel.doctorsState.collectAsState()
    val filteredDoctors by doctorListViewModel.filteredDoctors.collectAsState()

    Scaffold(
        topBar = {
            when (currentUser) {
                is UiState.Init -> {}
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> {
                    val user = (currentUser as UiState.Success).data
                    TopAppBar(
                        title = {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Good Morning ",
                                        fontSize = if (windowSize.width == WindowType.Compact) 14.sp else 16.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "👋",
                                        fontSize = if (windowSize.width == WindowType.Compact) 14.sp else 16.sp
                                    )
                                }
                                Text(
                                    text = "${user.first_name} ${user.last_name}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    maxLines = 1
                                )
                            }
                        },
                        navigationIcon = {
                            UserProfileImage(user.image)
                        },
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(PatientRoutes.NotificationScreen.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.Black,
                                    modifier = Modifier.size(if (windowSize.width == WindowType.Compact) 24.dp else 28.dp)
                                )
                            }
                            IconButton(onClick = { /* Handle favorites */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorites",
                                    tint = Color.Black,
                                    modifier = Modifier.size(if (windowSize.width == WindowType.Compact) 24.dp else 28.dp)
                                )
                            }
                        }
                    )
                }
                is UiState.Error -> {
                    val error = (currentUser as UiState.Error).message
                    Text("Erreur : $error", color = Color.Red)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CostumSearchBar(
                    windowSize = windowSize,
                    query = searchQuery,
                    onQueryChange = { doctorListViewModel.setSearchQuery(it) },
                    onSearch = { doctorListViewModel.setSearchQuery(it) },
                    onFilterClick = { println("Filter clicked") },
                    doctorListViewModel = doctorListViewModel,
                    navController = navController
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                UpcomingAppointmentBannner(
                    windowSize = windowSize,
                    onClick = { }
                )

            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                DoctorSpecialitySection(windowSize, homeViewModel)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Top Doctors",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = if (windowSize.width == WindowType.Compact) 18.sp else 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        modifier = Modifier.clickable {
                            navController.navigate(PatientRoutes.topDoctors.route)
                        },
                        text = "See All",
                        fontSize = if (windowSize.width == WindowType.Compact) 16.sp else 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            when (doctorsState) {
                is UiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is UiState.Success -> {
                    items(filteredDoctors) { doctor ->
                        DoctorItem(doctor, onDoctorClick)
                    }
                }
                is UiState.Error -> {
                    item {
                        val errorMessage = (doctorsState as UiState.Error).message
                        ErrorView(message = errorMessage) {
                            doctorListViewModel.refreshDoctors()
                        }
                    }
                }
                is UiState.Init -> {
                    item {
                        Text("Initial state. Please wait...", modifier = Modifier.padding(16.dp))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {  DeconnectionButton() }
        }
    }
}

@Composable
fun DoctorItem(doctor: Doctor, onDoctorClick: (String) -> Unit) {
    DoctorCard(
        doctor = doctor,
        isFavorite = false, // Update this when favorite logic is implemented
        onFavoriteClick = { /* Handle favorite */ },
        onDoctorClick = { onDoctorClick(doctor.id) },
    )
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Error: $message", color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun calculateWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    return WindowSize(
        width = getWindowType(screenWidth),
        height = getWindowType(screenHeight)
    )
}
