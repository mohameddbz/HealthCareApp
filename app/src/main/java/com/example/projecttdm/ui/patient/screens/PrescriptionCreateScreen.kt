package com.example.projecttdm.ui.patient.screens

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.state.UiState
import com.example.projecttdm.viewmodel.PrescriptionViewModel
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionCreateScreen(
    patientId: String,
    doctorId: String,
    appointmentId: String,
    onNavigateBack: () -> Unit,
    viewModel: PrescriptionViewModel = viewModel()
) {
    val context = LocalContext.current

    // Ensure parameters are properly passed through
    // Set initial values synchronously to avoid race conditions
    SideEffect {
        viewModel.resetFields() // Start clean

        if (patientId.isNotBlank()) {
            viewModel.onPatientIdChanged(patientId)
        }

        if (doctorId.isNotBlank()) {
            viewModel.onDoctorIdChanged(doctorId)
        }

        if (appointmentId.isNotBlank()) {
            viewModel.onAppointmentIdChanged(appointmentId)
        }
    }

    // Also set values when parameters change (if screen is reused)
    LaunchedEffect(patientId, doctorId, appointmentId) {
        if (patientId.isNotBlank()) {
            viewModel.onPatientIdChanged(patientId)
        }

        if (doctorId.isNotBlank()) {
            viewModel.onDoctorIdChanged(doctorId)
        }

        if (appointmentId.isNotBlank()) {
            viewModel.onAppointmentIdChanged(appointmentId)
        }

        // Debug printout to verify values are set
        println("LaunchedEffect: patientId=$patientId, doctorId=$doctorId, appointmentId=$appointmentId")
    }

    val instructions by viewModel.instructions.collectAsState()
    val expiryDate by viewModel.expiryDate.collectAsState()
    val medications by viewModel.medications.collectAsState()
    val medicationError by viewModel.medicationError.collectAsState()

    val medicationName by viewModel.medicationName.collectAsState()
    val medicationDosage by viewModel.medicationDosage.collectAsState()
    val medicationFrequency by viewModel.medicationFrequency.collectAsState()
    val medicationDuration by viewModel.medicationDuration.collectAsState()

    val prescriptionState by viewModel.prescriptionState.collectAsState()
    val scrollState = rememberScrollState()

    // Observe patient, doctor and appointment IDs for debugging
    val currentPatientId by viewModel.patientId.collectAsState()
    val currentDoctorId by viewModel.doctorId.collectAsState()
    val currentAppointmentId by viewModel.appointmentId.collectAsState()

    // Debug values in a side effect
    SideEffect {
        println("Current state in Composable:")
        println("- patientId: $currentPatientId")
        println("- doctorId: $currentDoctorId")
        println("- appointmentId: $currentAppointmentId")
    }

    LaunchedEffect(prescriptionState) {
        if (prescriptionState is UiState.Success) {
            Toast.makeText(context, "Prescription créée avec succès", Toast.LENGTH_SHORT).show()
            onNavigateBack()
        } else if (prescriptionState is UiState.Error) {
            Toast.makeText(context, (prescriptionState as UiState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nouvelle Prescription") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Fermer")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Debug info card (remove in production)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Informations de Débogue", fontWeight = FontWeight.Bold)
                    Text("ID Patient: $currentPatientId")
                    Text("ID Médecin: $currentDoctorId")
                    Text("ID Rendez-vous: $currentAppointmentId")
                }
            }

            // Date d'expiration
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Informations de base", fontWeight = FontWeight.Bold)

                    // Champ de date d'expiration avec sélecteur de date
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = expiryDate,
                            onValueChange = { viewModel.onExpiryDateChanged(it) },
                            label = { Text("Date d'expiration") },
                            modifier = Modifier.weight(1f),
                            readOnly = true
                        )

                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            val year = calendar.get(Calendar.YEAR)
                            val month = calendar.get(Calendar.MONTH)
                            val day = calendar.get(Calendar.DAY_OF_MONTH)

                            DatePickerDialog(
                                context,
                                { _, selectedYear, selectedMonth, selectedDay ->
                                    val selectedDate = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                                    viewModel.onExpiryDateChanged(selectedDate)
                                },
                                year, month, day
                            ).show()
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Sélectionner date")
                        }
                    }
                }
            }

            // Section des médicaments
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Médicaments", fontWeight = FontWeight.Bold)

                    // Affichage de l'erreur du médicament
                    medicationError?.let { error ->
                        Text(
                            text = error,
                            color = Color.Red,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    // Formulaire pour ajouter un médicament
                    OutlinedTextField(
                        value = medicationName,
                        onValueChange = { viewModel.onMedicationNameChanged(it) },
                        label = { Text("Nom du médicament") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = medicationDosage,
                        onValueChange = { viewModel.onMedicationDosageChanged(it) },
                        label = { Text("Dosage") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = medicationFrequency,
                        onValueChange = { viewModel.onMedicationFrequencyChanged(it) },
                        label = { Text("Fréquence") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = medicationDuration,
                        onValueChange = { viewModel.onMedicationDurationChanged(it) },
                        label = { Text("Durée") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { viewModel.addMedication() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Ajouter")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ajouter médicament")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Liste des médicaments ajoutés en hauteur fixe avec scroll interne
                    if (medications.isNotEmpty()) {
                        Text("Médicaments prescrits:", fontWeight = FontWeight.Medium)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(4.dp)
                        ) {
                            medications.forEachIndexed { index, medication ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(medication.name, fontWeight = FontWeight.Bold)
                                            Text("Dosage: ${medication.dosage}")
                                            Text("Fréquence: ${medication.frequency}")
                                            Text("Durée: ${medication.duration}")
                                        }

                                        IconButton(onClick = { viewModel.removeMedication(index) }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Supprimer",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Instructions
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Instructions", fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { viewModel.onInstructionsChanged(it) },
                        label = { Text("Instructions pour le patient") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        minLines = 3
                    )
                }
            }

            // Affichage des erreurs s'il y en a
            if (prescriptionState is UiState.Error) {
                Text(
                    text = (prescriptionState as UiState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Bouton de création
            Button(
                onClick = {
                    println("Creating prescription with: patientId=${viewModel.patientId.value}, doctorId=${viewModel.doctorId.value}, appointmentId=${viewModel.appointmentId.value}")
                    viewModel.createPrescription(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = prescriptionState !is UiState.Loading
            ) {
                if (prescriptionState is UiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text("Créer la prescription")
            }

            // Ajouter un espace en bas pour être sûr que tout est visible
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}