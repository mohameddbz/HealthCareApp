package com.example.projecttdm.ui.patient.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.viewmodel.DoctorListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    selectedSpecialty: Specialty,
    onDismiss: () -> Unit,
    onApplyFilter: () -> Unit,
    doctorListViewModel: DoctorListViewModel = viewModel()
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val specialties by doctorListViewModel.allSpecialties.collectAsState()

    val selectedRatingVM by doctorListViewModel.selectedRating.collectAsState()
    var selectedRating by remember { mutableStateOf(selectedRatingVM) }

    // Apply the custom theme to the ModalBottomSheet
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        containerColor = MaterialTheme.colorScheme.background, // Using theme color
        shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 24.dp)
        ) {
            // Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Filter",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            )

            // Specialty section
            Text(
                text = "Speciality",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Specialty chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // "All" option
                CategoryFilter(
                    specialties = specialties,
                    selectedSpecialty = selectedSpecialty,
                    onSpecialtySelected = { doctorListViewModel.setSpecialty(it) }
                )
            }

            // Rating section
            Text(
                text = "Rating",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Rating chips
            val ratingOptions = listOf("All", "5", "4", "3", "2")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RatingFilter(
                    ratings = ratingOptions,
                    selectedRating = selectedRating,
                    onRatingSelected = { selectedRating = it }
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            )

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        doctorListViewModel.setSpecialty(Specialty(id = "all", name = "All"))
                        selectedRating = "All"
                        doctorListViewModel.setRating("All")
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(50.dp) // Fully rounded button
                ) {
                    Text(
                        text = "Reset",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Optional spacing between buttons

                Button(
                    onClick = {
                        doctorListViewModel.setRating(selectedRating)
                        // The filtering gets applied automatically through the applyFiltering function
                        onApplyFilter()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(50.dp) // Fully rounded button
                ) {
                    Text(
                        text = "Apply",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Add some padding at the bottom to account for system bars
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}