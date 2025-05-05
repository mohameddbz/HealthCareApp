package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.projecttdm.ui.patient.components.DoctorProfile.AboutSection
import com.example.projecttdm.ui.patient.components.DoctorProfile.ProfileHeader
import com.example.projecttdm.ui.patient.components.DoctorProfile.ReviewsSection
import com.example.projecttdm.ui.patient.components.DoctorProfile.StatsSection
import com.example.projecttdm.ui.patient.components.DoctorProfile.WorkingHoursSection
import com.example.projecttdm.viewmodel.DoctorListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(
    viewModel: DoctorListViewModel,
    doctorId: String,
    onBackClick: () -> Unit,
    navigateToAllReviews: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val doctorState by viewModel.doctorState.collectAsState()
    val reviewsState by viewModel.reviewsState.collectAsState()

    val context = LocalContext.current

    // Load doctor profile when screen is first displayed
    LaunchedEffect(doctorId) {
        viewModel.getDoctorById(doctorId)
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
                modifier = modifier
            )

        }

        is UiState.Error -> {
            val errorMsg = (doctorState as UiState.Error).message
            LaunchedEffect(errorMsg) {
                //errorMsg.makeToast(context)
                println("jkjk ${errorMsg}")
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Failed to load doctor profile.",)
            }
        }

        UiState.Init -> TODO()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileContent(
    doctor: Doctor,
    reviewsState: UiState<List<Review>>,
    onBackClick: () -> Unit,
    navigateToAllReviews: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isFavorite =  false

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
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
                    BookAppointmentButton(onClick = {})
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