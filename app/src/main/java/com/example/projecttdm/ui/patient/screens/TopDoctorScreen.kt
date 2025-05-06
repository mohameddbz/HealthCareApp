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
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.state.UiState
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
    doctorListViewModel: DoctorListViewModel
) {
    val doctorsState by doctorListViewModel.doctorsState.collectAsState()
    val filteredDoctors by doctorListViewModel.filteredDoctors.collectAsState()
    val specialties by doctorListViewModel.allSpecialties.collectAsState()
    val selectedSpecialty by doctorListViewModel.selectedSpecialty.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Top Doctors",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CategoryFilter(
                specialties = specialties,
                selectedSpecialty = selectedSpecialty,
                onSpecialtySelected = { doctorListViewModel.setSpecialty(it) }
            )

            when (val state = doctorsState) {
                is UiState.Loading -> LoadingView()
                is UiState.Success -> DoctorList(filteredDoctors, onDoctorClick)
                is UiState.Error -> ErrorView(message = state.message) {
                    doctorListViewModel.refreshDoctors()
                }
                is UiState.Init -> {
                    Text("Initial state. Please wait...", modifier = Modifier.padding(16.dp))
                }

                is UiState.Error -> TODO()
                UiState.Init -> TODO()
                UiState.Loading -> TODO()
                is UiState.Success -> TODO()
            }
        }
    }
}


@Composable
fun DoctorList(doctors: List<Doctor>, onDoctorClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(doctors, key = { it.id }) { doctor ->
            DoctorCard(
                doctor = doctor,
                isFavorite = false, // Update when favorite logic is implemented
                onFavoriteClick = { /* handle favorite */ },
                onDoctorClick = { onDoctorClick(doctor.id) }
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

