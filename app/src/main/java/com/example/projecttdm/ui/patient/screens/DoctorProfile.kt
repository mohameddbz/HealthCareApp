package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Review
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.patient.components.Appointment.BookAppointmentButton
import com.example.projecttdm.ui.patient.components.DoctorProfile.*
import com.example.projecttdm.viewmodel.DoctorProfileViewModel


@Composable
fun DoctorProfileScreen(
    viewModel: DoctorProfileViewModel,
    doctorId: String,
    onBackClick: () -> Unit,
    navigateToAllReviews: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit ,
) {
    val doctorState by viewModel.doctorState.collectAsState()
    val reviewsState by viewModel.reviewsState.collectAsState()


    // Load doctor profile and reviews when screen is first displayed
    LaunchedEffect(doctorId) {
        viewModel.loadDoctorProfile(doctorId)
    }

    when (doctorState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is UiState.Success -> {
            val doctor = (doctorState as UiState.Success<Doctor>).data
            DoctorProfileContent(
                doctor = doctor,
                reviewsState = reviewsState,
                onBackClick = onBackClick,
                navigateToAllReviews = navigateToAllReviews,
                modifier = modifier,
                onBookClick = onBookClick
            )
        }

        is UiState.Error -> {
            val errorMsg = (doctorState as UiState.Error).message
            LaunchedEffect(errorMsg) {
                println("Error loading doctor profile: $errorMsg")
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Failed to load doctor profile.")
            }
        }

        UiState.Init -> {
            // Optionally show nothing or a placeholder
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileContent(
    doctor: Doctor,
    reviewsState: UiState<List<Review>>,
    onBackClick: () -> Unit,
    onBookClick: (String) -> Unit ,
    navigateToAllReviews: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isFavorite =  false

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Doctor Profile",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Toggle favorite functionality would go here */ }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Plus d'options")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                tonalElevation = 4.dp
            )  {
                Row(Modifier.padding(16.dp, 8.dp)) {
                    BookAppointmentButton(onClick = { onBookClick(doctor.id) })
                }
            }
        },
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ProfileHeader(doctor = doctor)
            }

            item {
                StatsSection(doctor = doctor)
            }

            item {
                doctor.about?.let { AboutSection(about = it) }
            }

            item {
                doctor.workingHours?.let { WorkingHoursSection(workingHours = it) }
            }

            item {
                when (reviewsState) {
                    is UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    is UiState.Success -> {
                        val reviews = (reviewsState as UiState.Success<List<Review>>).data
                        ReviewsSection(
                            reviews = reviews,
                            onSeeAllClick = { navigateToAllReviews(doctor.id) }
                        )
                    }
                    is UiState.Error -> {
                        Text(
                            text = "Failed to load reviews.",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    UiState.Init -> {}
                }
            }


            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Utilitaire pour formater les nombres
fun formatNumber(number: Int): String {
    return when {
        number >= 1000 -> String.format("%.1fK", number / 1000.0)
        else -> number.toString()
    }
}