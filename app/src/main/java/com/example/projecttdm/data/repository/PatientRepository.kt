package com.example.projecttdm.data.repository

import com.example.projecttdm.data.local.PatientData
import com.example.projecttdm.data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class PatientRepository {
    // In-memory storage for patients
    private val _patients = MutableStateFlow<List<Patient>>(PatientData.samplePatients)
    val patients: StateFlow<List<Patient>> = _patients.asStateFlow()

    // Function to save a new patient or update existing one
    fun savePatient(
        fullName: String,
        gender: String,
        age: Int,
        problemDescription: String,
        patientId: String = ""
    ): Patient {
        // Create a new ID if not provided
        val id = if (patientId.isEmpty()) UUID.randomUUID().toString() else patientId

        // Create patient object
        val patient = Patient(
            id = id,
            fullName = fullName,
            gender = gender,
            age = age,
            problemDescription = problemDescription
        )

        // Update or add the patient
        val currentPatients = _patients.value
        val existingIndex = currentPatients.indexOfFirst { it.id == id }

        _patients.value = if (existingIndex >= 0) {
            // Replace existing patient
            currentPatients.toMutableList().apply {
                this[existingIndex] = patient
            }
        } else {
            // Add new patient
            currentPatients + patient
        }

        return patient
    }

    // Function to get a patient by ID
    fun getPatient(patientId: String): Patient? {
        return _patients.value.find { it.id == patientId }
    }

    // Function to get all patients
    fun getAllPatients(): List<Patient> {
        return _patients.value
    }

    // Function to delete a patient
    fun deletePatient(patientId: String) {
        _patients.value = _patients.value.filter { it.id != patientId }
    }
}