package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projecttdm.ui.patient.components.CategoryFilter
import com.example.projecttdm.ui.patient.components.DoctorCard
import com.example.projecttdm.viewmodel.DoctorListViewModel
import com.example.projecttdm.viewmodel.DoctorSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDoctorScreen(
    onBackClick: () -> Unit,
    onDoctorClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    doctorSearchViewModel: DoctorSearchViewModel = viewModel(), // No need to pass as argument, use default ViewModel
    doctorListViewModel: DoctorListViewModel = viewModel(factory = viewModelFactory {
        initializer { DoctorListViewModel(doctorSearchViewModel) }
    })
) {
    val doctors by doctorListViewModel.doctors.collectAsState()
    val selectedSpecialty by doctorSearchViewModel.selectedSpecialty.collectAsState()
    val specialties by doctorSearchViewModel.allSpecialties.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Top Doctors",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge // Apply theme typography
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary // Use theme color for icon
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onPrimary // Apply theme color for icon
                        )
                    }
                    IconButton(onClick = { /* TODO: Implement more options */ }) {
                        Text(
                            text = "⋯",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary // Apply theme color for text
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary // Apply primary color from theme
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category Filter
            CategoryFilter(
                specialties = specialties,
                selectedSpecialty = selectedSpecialty,
                onSpecialtySelected = { doctorSearchViewModel.setSpecialty(it) }
            )

            // Doctor List
            if (doctors.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No doctors found.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface // Apply theme color for text
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(doctors) { doctor ->
                        DoctorCard(
                            doctor = doctor,
                            isFavorite = false, // Could be managed via favorites list
                            onFavoriteClick = { doctorListViewModel.toggleFavorite(doctor.id) },
                            onDoctorClick = { onDoctorClick(doctor.id) }
                        )
                    }
                }
            }
        }
    }
}
