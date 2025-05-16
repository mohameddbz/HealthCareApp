package com.example.projecttdm.ui.patient.screens

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.state.UiState
import com.example.projecttdm.viewmodel.PrescriptionViewModel
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionCreateScreen(
    onNavigateBack: () -> Unit,
    viewModel: PrescriptionViewModel = viewModel()
) {
    val patientId by viewModel.patientId.collectAsState()
    val doctorId by viewModel.doctorId.collectAsState()
    val appointmentId by viewModel.appointmentId.collectAsState()
    val instructions by viewModel.instructions.collectAsState()
    val expiryDate by viewModel.expiryDate.collectAsState()
    val medications by viewModel.medications.collectAsState()

    val medicationName by viewModel.medicationName.collectAsState()
    val medicationDosage by viewModel.medicationDosage.collectAsState()
    val medicationFrequency by viewModel.medicationFrequency.collectAsState()
    val medicationDuration by viewModel.medicationDuration.collectAsState()

    val prescriptionState by viewModel.prescriptionState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(prescriptionState) {
        if (prescriptionState is UiState.Success) {
            // La prescription a été créée avec succès, on peut naviguer vers une autre page
            onNavigateBack()
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
                .verticalScroll(scrollState) // Ajout du scroll vertical pour toute la page
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Informations patient et médecin
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

                    OutlinedTextField(
                        value = patientId,
                        onValueChange = { viewModel.onPatientIdChanged(it) },
                        label = { Text("ID du patient") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = doctorId,
                        onValueChange = { viewModel.onDoctorIdChanged(it) },
                        label = { Text("ID du médecin") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                            value = appointmentId,
                            onValueChange = { viewModel.onAppointmentIdChanged(it) },
                            label = { Text("ID du Appointment") },
                            modifier = Modifier.fillMaxWidth()
                        )


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
                onClick = { viewModel.createPrescription() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = prescriptionState !is UiState.Loading
            ) {
                when (prescriptionState) {
                    is UiState.Init -> {
                        // Ne rien afficher ou un écran vide
                    }
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    is UiState.Success -> {
                        Toast.makeText(context,"mchat 9owa",Toast.LENGTH_SHORT).show()
                            }

                    is UiState.Error -> Toast.makeText(context,"mhbtch tmchi",Toast.LENGTH_SHORT).show()
                }

                Text("Créer la prescription")
            }

            // Ajouter un espace en bas pour être sûr que tout est visible
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}