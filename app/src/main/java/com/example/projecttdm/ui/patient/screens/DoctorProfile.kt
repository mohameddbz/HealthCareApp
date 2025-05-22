package com.example.projecttdm.ui.patient.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Review
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.patient.components.DoctorProfile.*
import com.example.projecttdm.viewmodel.DoctorProfileViewModel

@Composable
fun DoctorProfileScreen(
    viewModel: DoctorProfileViewModel,
    doctorId: String,
    onBackClick: () -> Unit,
    navigateToAllReviews: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit,
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
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Failed to load doctor profile.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadDoctorProfile(doctorId) }) {
                        Text("Retry")
                    }
                }
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
    onBookClick: (String) -> Unit,
    navigateToAllReviews: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isFavorite = false
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Doctor Profile",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = primaryColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Toggle favorite functionality would go here */ }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Plus d'options",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = primaryColor,
                                modifier = Modifier.size(32.dp)
                            )
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
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = "Failed to load reviews.",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    UiState.Init -> {}
                }
            }

            // Nouveau bouton de prise de rendez-vous avec flèche
            item {
                BookAppointmentCard(
                    onClick = { onBookClick(doctor.id) },
                    doctor = doctor
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BookAppointmentCard(
    onClick: () -> Unit,
    doctor: Doctor
) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primaryContainer
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(colors = gradientColors)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Prendre rendez-vous",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Avec Dr. ${doctor.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }

                Button(
                    onClick = onClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Prendre rendez-vous",
                        tint = MaterialTheme.colorScheme.primary
                    )
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