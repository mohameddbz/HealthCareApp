package com.example.projecttdm.ui.patient.components.Prescription

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projecttdm.data.model.Medication


@Composable
fun MedicationsList(medications: List<Medication>) {
    Column {
        medications.forEachIndexed { index, medication ->
            MedicationCard(medication)

            if (index < medications.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}