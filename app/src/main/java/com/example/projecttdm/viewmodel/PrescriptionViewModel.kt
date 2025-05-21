//package com.example.projecttdm.viewmodel
//
//import android.content.Context
//import android.net.Uri
//import android.os.Build
//import android.view.View
//import androidx.annotation.RequiresApi
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.projecttdm.data.model.FullPrescription
//import com.example.projecttdm.data.model.Medications
//import com.example.projecttdm.data.model.Prescription
//import com.example.projecttdm.data.model.PrescriptionResponse
//import com.example.projecttdm.data.model.Prescriptions
//import com.example.projecttdm.data.repository.RepositoryHolder
//import com.example.projecttdm.state.UiState
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.util.UUID
//
//@RequiresApi(Build.VERSION_CODES.O)
//class PrescriptionViewModel : ViewModel() {
//    private val prescriptionRepository = RepositoryHolder.prescriptionRepository
//
//    // État des opérations de prescription
//    private val _prescriptionState = MutableStateFlow<UiState<PrescriptionResponse>>(UiState.Init)
//    val prescriptionState: StateFlow<UiState<PrescriptionResponse>> = _prescriptionState.asStateFlow()
//
//    // Liste des prescriptions
//    private val _prescriptions = MutableStateFlow<UiState<List<Prescriptions>>>(UiState.Init)
//    val prescriptions: StateFlow<UiState<List<Prescriptions>>> = _prescriptions.asStateFlow()
//
//    // État d'une prescription spécifique
//    private val _selectedPrescription = MutableStateFlow<UiState<Prescriptions>>(UiState.Init)
//    val selectedPrescription: StateFlow<UiState<Prescriptions>> = _selectedPrescription.asStateFlow()
//
//    // Champs observables pour la création/modification de prescription
//    val patientId = MutableStateFlow("")
//    val doctorId = MutableStateFlow("")
//    val appointmentId = MutableStateFlow("")  // Ajout du champ appointmentId
//    val instructions = MutableStateFlow("")
//    val expiryDate = MutableStateFlow("")
//
//    // Liste de médicaments pour la prescription
//    private val _medications = MutableStateFlow<List<Medications>>(emptyList())
//    val medications: StateFlow<List<Medications>> = _medications.asStateFlow()
//
//    // Champs pour un nouveau médicament
//    val medicationName = MutableStateFlow("")
//    val medicationDosage = MutableStateFlow("")
//    val medicationFrequency = MutableStateFlow("")
//    val medicationDuration = MutableStateFlow("")
//
//    // Fonctions pour mettre à jour les champs
//    fun onPatientIdChanged(newValue: String) {
//        patientId.value = newValue
//    }
//
//    fun onDoctorIdChanged(newValue: String) {
//        doctorId.value = newValue
//    }
//
//    fun onAppointmentIdChanged(newValue: String) {
//        appointmentId.value = newValue
//    }
//
//    fun onInstructionsChanged(newValue: String) {
//        instructions.value = newValue
//    }
//
//    fun onExpiryDateChanged(newValue: String) {
//        expiryDate.value = newValue
//    }
//
//    fun onMedicationNameChanged(newValue: String) {
//        medicationName.value = newValue
//    }
//
//    fun onMedicationDosageChanged(newValue: String) {
//        medicationDosage.value = newValue
//    }
//
//    fun onMedicationFrequencyChanged(newValue: String) {
//        medicationFrequency.value = newValue
//    }
//
//    fun onMedicationDurationChanged(newValue: String) {
//        medicationDuration.value = newValue
//    }
//
//    // Ajouter un médicament à la liste
//    fun addMedication() {
//        if (medicationName.value.isNotBlank() &&
//            medicationDosage.value.isNotBlank() &&
//            medicationFrequency.value.isNotBlank() &&
//            medicationDuration.value.isNotBlank()
//        ) {
//            val newMedication = Medications(
//                id = UUID.randomUUID().toString(), // ID temporaire client-side
//                name = medicationName.value,
//                dosage = medicationDosage.value,
//                frequency = medicationFrequency.value,
//                duration = medicationDuration.value
//            )
//
//            _medications.value = _medications.value + newMedication
//
//            // Réinitialiser les champs
//            medicationName.value = ""
//            medicationDosage.value = ""
//            medicationFrequency.value = ""
//            medicationDuration.value = ""
//        }
//    }
//
//    // Supprimer un médicament de la liste
//    fun removeMedication(index: Int) {
//        val currentList = _medications.value.toMutableList()
//        if (index in currentList.indices) {
//            currentList.removeAt(index)
//            _medications.value = currentList
//        }
//    }
//
//    // Charger toutes les prescriptions
//    fun loadAllPrescriptions() {
//        viewModelScope.launch {
//            _prescriptions.value = UiState.Loading
//            try {
//                val response = prescriptionRepository.getAllPrescriptions()
//                _prescriptions.value = UiState.Success(response)
//            } catch (e: Exception) {
//                _prescriptions.value = UiState.Error(e.message ?: "Erreur inconnue")
//            }
//        }
//    }
//
//    // Charger les prescriptions d'un médecin
//    fun loadPrescriptionsByDoctor(doctorId: String) {
//        viewModelScope.launch {
//            _prescriptions.value = UiState.Loading
//            try {
//                val response = prescriptionRepository.getPrescriptionsByDoctor(doctorId)
//                _prescriptions.value = UiState.Success(response)
//            } catch (e: Exception) {
//                _prescriptions.value = UiState.Error(e.message ?: "Erreur inconnue")
//            }
//        }
//    }
//
//    // Charger les prescriptions d'un patient
//    fun loadPrescriptionsByPatient(patientId: String) {
//        viewModelScope.launch {
//            _prescriptions.value = UiState.Loading
//            try {
//                val response = prescriptionRepository.getPrescriptionsByPatient(patientId)
//                _prescriptions.value = UiState.Success(response)
//            } catch (e: Exception) {
//                _prescriptions.value = UiState.Error(e.message ?: "Erreur inconnue")
//            }
//        }
//    }
//
//
//
//    // Créer une nouvelle prescription
//    fun createPrescription() {
//        if (
//            instructions.value.isBlank() ||
//            expiryDate.value.isBlank()
//        ) {
//            _prescriptionState.value = UiState.Error("Tous les champs sont requis")
//            return
//        }
//
//        viewModelScope.launch {
//            _prescriptionState.value = UiState.Loading
//            try {
//                val response = prescriptionRepository.createPrescription(
//                    patientId = patientId.value,
//                    doctorId = doctorId.value,
//                    medications = _medications.value,
//                    instructions = instructions.value,
//                    expiryDate = expiryDate.value,
//                    appointmentId = appointmentId.value // Ajout du champ appointmentId
//                )
//                _prescriptionState.value = UiState.Success(response)
//
//                // Réinitialiser les champs après succès
//                if (response.success) {
//                    resetFields()
//                }
//            } catch (e: Exception) {
//                _prescriptionState.value = UiState.Error(e.message ?: "Erreur inconnue")
//            }
//        }
//    }
//
//    // Mettre à jour une prescription existante
//    fun updatePrescription(id: String) {
//        if (patientId.value.isBlank() ||
//            doctorId.value.isBlank() ||
//            _medications.value.isEmpty() ||
//            instructions.value.isBlank() ||
//            expiryDate.value.isBlank()
//        ) {
//            _prescriptionState.value = UiState.Error("Tous les champs sont requis")
//            return
//        }
//
//        viewModelScope.launch {
//            _prescriptionState.value = UiState.Loading
//            try {
//                val response = prescriptionRepository.updatePrescription(
//                    id = id,
//                    patientId = patientId.value,
//                    doctorId = doctorId.value,
//                    medications = _medications.value,
//                    instructions = instructions.value,
//                    expiryDate = expiryDate.value,
//                    appointmentId = appointmentId.value // Ajout du champ appointmentId
//                )
//                _prescriptionState.value = UiState.Success(response)
//
//                // Réinitialiser les champs après succès
//                if (response.success) {
//                    resetFields()
//                }
//            } catch (e: Exception) {
//                _prescriptionState.value = UiState.Error(e.message ?: "Erreur inconnue")
//            }
//        }
//    }
//
//    // Supprimer une prescription
//    fun deletePrescription(id: String) {
//        viewModelScope.launch {
//            _prescriptionState.value = UiState.Loading
//            try {
//                val response = prescriptionRepository.deletePrescription(id)
//                _prescriptionState.value = UiState.Success(response)
//            } catch (e: Exception) {
//                _prescriptionState.value = UiState.Error(e.message ?: "Erreur inconnue")
//            }
//        }
//    }
//
//    // Charger les données d'une prescription pour modification
//    fun loadPrescriptionForEdit(prescription: Prescriptions) {
//        patientId.value = prescription.patientId
//        doctorId.value = prescription.doctorId
//        appointmentId.value = prescription.appointmentId // Ajout du champ appointmentId
//        instructions.value = prescription.instructions
//        expiryDate.value = prescription.expiryDate
//        _medications.value = prescription.medications
//    }
//
//    // Réinitialiser tous les champs
//    fun resetFields() {
//        patientId.value = ""
//        doctorId.value = ""
//        appointmentId.value = "" // Réinitialisation du champ appointmentId
//        instructions.value = ""
//        expiryDate.value = ""
//        _medications.value = emptyList()
//        medicationName.value = ""
//        medicationDosage.value = ""
//        medicationFrequency.value = ""
//        medicationDuration.value = ""
//        _prescriptionState.value = UiState.Init
//    }
//
//
//}

// PrescriptionViewModel.kt - Fix
package com.example.projecttdm.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Medications
import com.example.projecttdm.data.model.PrescriptionResponse
import com.example.projecttdm.data.model.Prescriptions
import com.example.projecttdm.data.model.PrescriptionsUiState
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class PrescriptionViewModel : ViewModel() {
    private val prescriptionRepository = RepositoryHolder.prescriptionRepository

    // État des opérations de prescription
    private val _prescriptionState = MutableStateFlow<UiState<PrescriptionResponse>>(UiState.Init)
    val prescriptionState: StateFlow<UiState<PrescriptionResponse>> = _prescriptionState.asStateFlow()


    private val _prescriptionsState = MutableStateFlow<PrescriptionsUiState>(PrescriptionsUiState.Loading)
    val prescriptionsState: StateFlow<PrescriptionsUiState> = _prescriptionsState.asStateFlow()


    // Liste des prescriptions
    private val _prescriptions = MutableStateFlow<UiState<List<Prescriptions>>>(UiState.Init)
    val prescriptions: StateFlow<UiState<List<Prescriptions>>> = _prescriptions.asStateFlow()

    // État d'une prescription spécifique
    private val _selectedPrescription = MutableStateFlow<UiState<Prescriptions>>(UiState.Init)
    val selectedPrescription: StateFlow<UiState<Prescriptions>> = _selectedPrescription.asStateFlow()

    // Message d'erreur pour l'ajout de médicament
    private val _medicationError = MutableStateFlow<String?>(null)
    val medicationError: StateFlow<String?> = _medicationError.asStateFlow()

    // Champs observables pour la création/modification de prescription
    val patientId = MutableStateFlow("")
    val doctorId = MutableStateFlow("")
    val appointmentId = MutableStateFlow("")
    val instructions = MutableStateFlow("")
    val expiryDate = MutableStateFlow("")

    // Liste de médicaments pour la prescription
    private val _medications = MutableStateFlow<List<Medications>>(emptyList())
    val medications: StateFlow<List<Medications>> = _medications.asStateFlow()

    // Champs pour un nouveau médicament
    val medicationName = MutableStateFlow("")
    val medicationDosage = MutableStateFlow("")
    val medicationFrequency = MutableStateFlow("")
    val medicationDuration = MutableStateFlow("")

    // Fonctions pour mettre à jour les champs
    fun onPatientIdChanged(newValue: String) {
        patientId.value = newValue
        println("Patient ID updated to: ${patientId.value}")
    }

    fun onDoctorIdChanged(newValue: String) {
        doctorId.value = newValue
        println("Doctor ID updated to: ${doctorId.value}")
    }

    fun onAppointmentIdChanged(newValue: String) {
        appointmentId.value = newValue
        println("Appointment ID updated to: ${appointmentId.value}")
    }

    fun onInstructionsChanged(newValue: String) {
        instructions.value = newValue
    }

    fun onExpiryDateChanged(newValue: String) {
        expiryDate.value = newValue
    }

    fun onMedicationNameChanged(newValue: String) {
        medicationName.value = newValue
        _medicationError.value = null
    }

    fun onMedicationDosageChanged(newValue: String) {
        medicationDosage.value = newValue
        _medicationError.value = null
    }

    fun onMedicationFrequencyChanged(newValue: String) {
        medicationFrequency.value = newValue
        _medicationError.value = null
    }

    fun onMedicationDurationChanged(newValue: String) {
        medicationDuration.value = newValue
        _medicationError.value = null
    }

    // Valider et ajouter un médicament à la liste
    fun addMedication() {
        // Réinitialiser l'erreur précédente
        _medicationError.value = null

        // Vérifier si les champs sont vides
        when {
            medicationName.value.isBlank() -> {
                _medicationError.value = "Le nom du médicament est requis"
                return
            }
            medicationDosage.value.isBlank() -> {
                _medicationError.value = "Le dosage est requis"
                return
            }
            medicationFrequency.value.isBlank() -> {
                _medicationError.value = "La fréquence est requise"
                return
            }
            medicationDuration.value.isBlank() -> {
                _medicationError.value = "La durée est requise"
                return
            }
        }

        // Ajouter le médicament validé à la liste
        val newMedication = Medications(
            id = UUID.randomUUID().toString(), // ID temporaire client-side
            name = medicationName.value,
            dosage = medicationDosage.value,
            frequency = medicationFrequency.value,
            duration = medicationDuration.value
        )

        _medications.value = _medications.value + newMedication

        // Réinitialiser les champs après l'ajout réussi
        medicationName.value = ""
        medicationDosage.value = ""
        medicationFrequency.value = ""
        medicationDuration.value = ""
    }

    // Supprimer un médicament de la liste
    fun removeMedication(index: Int) {
        val currentList = _medications.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _medications.value = currentList
        }
    }

    // Charger toutes les prescriptions
    fun loadAllPrescriptions() {
        viewModelScope.launch {
            _prescriptions.value = UiState.Loading
            try {
                val response = prescriptionRepository.getAllPrescriptions()
                _prescriptions.value = UiState.Success(response)
            } catch (e: Exception) {
                _prescriptions.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    // Charger les prescriptions d'un médecin
    fun loadPrescriptionsByDoctor(doctorId: String) {
        viewModelScope.launch {
            _prescriptions.value = UiState.Loading
            try {
                val response = prescriptionRepository.getPrescriptionsByDoctor(doctorId)
                _prescriptions.value = UiState.Success(response)
            } catch (e: Exception) {
                _prescriptions.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    // Charger les prescriptions d'un patient
    fun loadPrescriptionsByPatient(patientId: String) {
        viewModelScope.launch {
            _prescriptions.value = UiState.Loading
            try {
                val response = prescriptionRepository.getPrescriptionsByPatient(patientId)
                _prescriptions.value = UiState.Success(response)
            } catch (e: Exception) {
                _prescriptions.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    // Valider les champs avant création ou mise à jour
    private fun validatePrescriptionFields(): String? {
        return when {
            expiryDate.value.isBlank() -> "La date d'expiration est requise"
            _medications.value.isEmpty() -> "Au moins un médicament doit être ajouté"
            instructions.value.isBlank() -> "Les instructions sont requises"
            patientId.value.isBlank() -> "L'ID du patient est requis"
            doctorId.value.isBlank() -> "L'ID du médecin est requis"
            appointmentId.value.isBlank() -> "L'ID du rendez-vous est requis"
            else -> null
        }
    }

    // Créer une nouvelle prescription
    fun createPrescription() {
        // Debug: Afficher les valeurs actuelles
        println("DEBUG - Current values before prescription creation:")
        println("Patient ID: ${patientId.value}")
        println("Doctor ID: ${doctorId.value}")
        println("Appointment ID: ${appointmentId.value}")
        println("Instructions: ${instructions.value}")
        println("Expiry Date: ${expiryDate.value}")
        println("Medications Count: ${_medications.value.size}")

        // Valider les champs
        val validationError = validatePrescriptionFields()
        if (validationError != null) {
            _prescriptionState.value = UiState.Error(validationError)
            return
        }

        viewModelScope.launch {
            _prescriptionState.value = UiState.Loading
            try {
                val response = prescriptionRepository.createPrescription(
                    patientId = patientId.value,
                    medications = _medications.value,
                    instructions = instructions.value,
                    expiryDate = expiryDate.value,
                    appointmentId = appointmentId.value
                )

                if (response.success) {
                    _prescriptionState.value = UiState.Success(response)
                    resetFields()
                } else {
                    _prescriptionState.value = UiState.Error(response.message ?: "Erreur de création")
                }
            } catch (e: Exception) {
                _prescriptionState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    // Mettre à jour une prescription existante
    fun updatePrescription(id: String) {
        // Valider les champs
        val validationError = validatePrescriptionFields()
        if (validationError != null) {
            _prescriptionState.value = UiState.Error(validationError)
            return
        }

        viewModelScope.launch {
            _prescriptionState.value = UiState.Loading
            try {
                val response = prescriptionRepository.updatePrescription(
                    id = id,
                    patientId = patientId.value,
                    doctorId = doctorId.value,
                    medications = _medications.value,
                    instructions = instructions.value,
                    expiryDate = expiryDate.value,
                    appointmentId = appointmentId.value
                )

                if (response.success) {
                    _prescriptionState.value = UiState.Success(response)
                    resetFields()
                } else {
                    _prescriptionState.value = UiState.Error(response.message ?: "Erreur de mise à jour")
                }
            } catch (e: Exception) {
                _prescriptionState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    // Supprimer une prescription
    fun deletePrescription(id: String) {
        viewModelScope.launch {
            _prescriptionState.value = UiState.Loading
            try {
                val response = prescriptionRepository.deletePrescription(id)
                _prescriptionState.value = UiState.Success(response)
            } catch (e: Exception) {
                _prescriptionState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    // Charger les données d'une prescription pour modification
    fun loadPrescriptionForEdit(prescription: Prescriptions) {
        patientId.value = prescription.patientId
        doctorId.value = prescription.doctorId
        appointmentId.value = prescription.appointmentId
        instructions.value = prescription.instructions
        expiryDate.value = prescription.expiryDate
        _medications.value = prescription.medications
    }

    // Réinitialiser tous les champs
    fun resetFields() {
        patientId.value = ""
        doctorId.value = ""
        appointmentId.value = ""
        instructions.value = ""
        expiryDate.value = ""
        _medications.value = emptyList()
        medicationName.value = ""
        medicationDosage.value = ""
        medicationFrequency.value = ""
        medicationDuration.value = ""
        _prescriptionState.value = UiState.Init
        _medicationError.value = null
    }


    fun fetchPrescriptions(patientId: String) {
        viewModelScope.launch {
            _prescriptionsState.value = PrescriptionsUiState.Loading
            prescriptionRepository.getPrescriptions(patientId).collect { result ->
                _prescriptionsState.value = result.fold(
                    onSuccess = { PrescriptionsUiState.Success(it) },
                    onFailure = { PrescriptionsUiState.Error(it.message ?: "Unknown error") }
                )
            }
        }
    }
}