package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.ui.patient.components.DoctorCard
import com.example.projecttdm.ui.patient.components.CategoryFilter
import com.example.projecttdm.viewmodel.DoctorSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit = {},
    doctorSearchViewModel: DoctorSearchViewModel = viewModel()
) {
    // Observe the doctors, selected specialty, search query, and specialties
    val doctors by doctorSearchViewModel.filteredDoctors.collectAsState()
    val selectedSpecialty by doctorSearchViewModel.selectedSpecialty.collectAsState()
    val searchQuery by doctorSearchViewModel.searchQuery.collectAsState()
    val specialties by doctorSearchViewModel.allSpecialties.collectAsState()

    Column{
        TopAppBar(
            title = {
                TextField(
                    value = searchQuery,
                    onValueChange = { doctorSearchViewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small),
                    placeholder = { Text("Search doctors...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = { /* Optional: Additional actions like filters or settings */ },
            modifier = Modifier.fillMaxWidth()
        )

        // Specialty Filter Chips
        CategoryFilter(
            specialties = specialties,
            selectedSpecialty = selectedSpecialty,
            onSpecialtySelected = { doctorSearchViewModel.setSpecialty(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Doctor Results List
        if (doctors.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No results found.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                doctors.forEach { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        isFavorite = false,
                        onFavoriteClick = { /* Optional: Handle favorites */ },
                        onDoctorClick = { /* TODO: Handle doctor click */ }
                    )
                }
            }
        }
    }
}
