package com.example.projecttdm.ui.patient.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projecttdm.data.model.Specialty

@Composable
fun CategoryFilter(
    specialties: List<Specialty>,
    selectedSpecialty: Specialty,
    onSpecialtySelected: (Specialty) -> Unit
) {
    // Make sure we have specialties to display
    if (specialties.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(specialties, key = { it.id }) { specialty ->
                CategoryChip(
                    category = specialty.name,
                    isSelected = specialty.id == selectedSpecialty.id, // Compare by ID instead of object equality
                    onCategorySelected = { onSpecialtySelected(specialty) }
                )
            }
        }
    }
}