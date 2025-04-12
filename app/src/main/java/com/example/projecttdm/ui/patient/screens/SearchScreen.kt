package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.ui.common.components.NotFoundComponent
import com.example.projecttdm.ui.patient.components.CategoryFilter
import com.example.projecttdm.ui.patient.components.DoctorCard
import com.example.projecttdm.ui.patient.components.FilterDialog
import com.example.projecttdm.viewmodel.DoctorSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit = {},
    doctorSearchViewModel: DoctorSearchViewModel = viewModel()
) {
    val doctors by doctorSearchViewModel.filteredDoctors.collectAsState()
    val selectedSpecialty by doctorSearchViewModel.selectedSpecialty.collectAsState()
    val searchQuery by doctorSearchViewModel.searchQuery.collectAsState()
    val specialties by doctorSearchViewModel.allSpecialties.collectAsState()
    val resultsCount = doctors.size

    var loading by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Trigger loading when query or specialty changes
    LaunchedEffect(searchQuery, selectedSpecialty) {
        loading = true
        kotlinx.coroutines.delay(500)  // 0.5s delay
        loading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        TopAppBar(
            title = {
                TextField(
                    value = searchQuery,
                    onValueChange = { doctorSearchViewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small),
                    placeholder = {
                        Text(
                            "Search ...",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    trailingIcon = {
                        IconButton(onClick = { showFilterDialog = true }) {
                            Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
                        }
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
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        CategoryFilter(
            specialties = specialties,
            selectedSpecialty = selectedSpecialty,
            onSpecialtySelected = { doctorSearchViewModel.setSpecialty(it) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$resultsCount founds",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue
            )
        }

        // Show loading indicator before displaying results
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Blue)
            }
        } else {
            if (doctors.isEmpty()) {
                NotFoundComponent()
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

    // Filter dialog
    if (showFilterDialog) {
        FilterDialog(
            selectedSpecialty = selectedSpecialty,
            onDismiss = { showFilterDialog = false },
            onApplyFilter = {
                // Apply any additional filter logic here
                showFilterDialog = false
            }
        )
    }
}