package com.example.projecttdm.ui.patient.components.Appointment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.RescheduleReason

@Composable
fun RadioGroupOptions(
    options: List<RescheduleReason>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .selectableGroup()
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .selectable(
                        selected = (option.id == selectedOption),
                        onClick = { onOptionSelected(option.id) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option.id == selectedOption),
                    onClick = null, // null because we handle click on the parent Row
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF3B82F6),
                        unselectedColor = Color(0xFF9CA3AF)
                    )
                )
                Text(
                    text = option.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}