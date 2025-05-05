package com.example.projecttdm.ui.patient.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projecttdm.data.model.Specialty

@Composable
fun FilterDialog(
    selectedSpecialty: Specialty,
    selectedRating: String,
    onDismiss: () -> Unit,
    onApplyFilter: (Specialty, String) -> Unit
) {
    var tempSelectedSpecialty by remember { mutableStateOf(selectedSpecialty) }
    var tempSelectedRating by remember { mutableStateOf(selectedRating) }

    val ratingOptions = listOf("All", "5", "4", "3", "2", "1")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Doctors") },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                // Title for the specialty section
                Text(
                    "Rating",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Rating filter
                Column {
                    ratingOptions.forEach { rating ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = tempSelectedRating == rating,
                                    onClick = { tempSelectedRating = rating }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = tempSelectedRating == rating,
                                onClick = { tempSelectedRating = rating }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (rating == "All") "All Ratings" else "$rating Stars and above",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // We could add more filter options here if needed
            }
        },
        confirmButton = {
            Button(
                onClick = { onApplyFilter(tempSelectedSpecialty, tempSelectedRating) }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}