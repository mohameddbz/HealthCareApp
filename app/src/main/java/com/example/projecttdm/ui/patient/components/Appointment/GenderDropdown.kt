package com.example.projecttdm.ui.patient.components.Appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GenderDropdown(
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    val background = Color(0xFFEEEEEE)
    val genderOptions = listOf("Male", "Female")

    Column {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        GenderDropdownSelector(
            selectedGender = selectedGender,
            background = background,
            onClick = { onExpandedChange(true) }
        )

        GenderDropdownMenu(
            options = genderOptions,
            expanded = expanded,
            onDismiss = { onExpandedChange(false) },
            onSelect = { gender ->
                onGenderSelected(gender)
                onExpandedChange(false)
            }
        )
    }
}

@Composable
private fun GenderDropdownSelector(
    selectedGender: String,
    background: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = selectedGender, color = Color.Black)
        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown")
    }
}

@Composable
private fun GenderDropdownMenu(
    options: List<String>,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        options.forEach { gender ->
            DropdownMenuItem(
                text = { Text(gender) },
                onClick = { onSelect(gender) }
            )
        }
    }
}
