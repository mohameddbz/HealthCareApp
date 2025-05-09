package com.example.projecttdm.ui.patient.components.Prescription

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.*

@Composable
fun PrescriptionContent(prescription: FullPrescription) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Header section with gradient
            PrescriptionHeader(prescription.Doctor)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Patient information
                PatientInfoSection(prescription.Patient)

                Spacer(modifier = Modifier.height(24.dp))

                // Rx with styled divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Rx",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Divider(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        thickness = 2.dp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Medications list
                MedicationsList(prescription.MEDICATIONs)

                Spacer(modifier = Modifier.height(20.dp))

                // Footer with date and hospital
                PrescriptionFooter(prescription)
            }
        }
    }
}
















