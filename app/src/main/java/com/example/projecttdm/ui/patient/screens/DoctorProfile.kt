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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.ui.patient.BottomNavigationBar
import com.example.projecttdm.ui.patient.components.Appointment.BookAppointmentButton
import com.example.projecttdm.ui.patient.components.DoctorProfile.AboutSection
import com.example.projecttdm.ui.patient.components.DoctorProfile.ProfileHeader
import com.example.projecttdm.ui.patient.components.DoctorProfile.ReviewsSection
import com.example.projecttdm.ui.patient.components.DoctorProfile.StatsSection
import com.example.projecttdm.ui.patient.components.DoctorProfile.WorkingHoursSection
import com.example.projecttdm.viewmodel.DoctorProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(
    viewModel: DoctorProfileViewModel,
    onBackClick: () -> Unit,
    navigateToAllReviews: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val doctorData by viewModel.doctorData.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Chargement initial
    LaunchedEffect(Unit) {
        viewModel.loadDoctorProfile("dr-jenny-watson")
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    doctorData?.let { doctor ->
        Scaffold(
            topBar = {
                androidx.compose.material3.TopAppBar(
                    title = {
                        Text(text = doctor.name,
                            fontWeight = FontWeight.Bold ,
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
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
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
                            .fillMaxWidth().border(
                                width = 1.dp,
                        color = Color.Gray,
                                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp) // Assure-toi que le shape est le même que la surface
                    )
                            ,
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp), // Coins arrondis
                        tonalElevation = 4.dp
                    ) {
                        Row (Modifier.padding(16.dp,8.dp)) {
                            BookAppointmentButton (onClick = {})
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
                    ReviewsSection(
                        reviews = reviews,
                        onSeeAllClick = { navigateToAllReviews(doctor.id) }
                    )
                }

                item {
                    BookAppointmentButton(onClick = { viewModel.bookAppointment(doctor.id) })
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
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
