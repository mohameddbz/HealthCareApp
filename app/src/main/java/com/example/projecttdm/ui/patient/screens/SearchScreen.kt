package com.example.projecttdm.ui.patient.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    doctorSearchViewModel: DoctorSearchViewModel
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
            .fillMaxSize().padding(top = 6.dp)
            .background(MaterialTheme.colorScheme.background) // Using theme's background color
    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { doctorSearchViewModel.setSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Search ...",
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showFilterDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = "Filter",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.inverseOnSurface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.inverseOnSurface,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
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
                text = "$resultsCount found",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary // Color using the theme
            )
        }

        // Show loading indicator before displaying results
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Using theme color for indicator
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
