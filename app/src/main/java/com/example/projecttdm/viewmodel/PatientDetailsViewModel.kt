package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.local.PatientData
import com.example.projecttdm.data.model.Patient
import com.example.projecttdm.data.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PatientDetailsViewModel : ViewModel() {
    private val repository = PatientRepository()

    // Full name of the patient
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    // Gender of the patient
    private val _gender = MutableStateFlow(PatientData.genderOptions.first())
    val gender: StateFlow<String> = _gender.asStateFlow()

    // Age of the patient
    private val _age = MutableStateFlow("")
    val age: StateFlow<String> = _age.asStateFlow()

    // Problem description
    private val _problemDescription = MutableStateFlow("")
    val problemDescription: StateFlow<String> = _problemDescription.asStateFlow()

    // Current patient ID (null if creating new)
    private val _patientId = MutableStateFlow<String?>(null)
    val patientId: StateFlow<String?> = _patientId.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error message
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Success state
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    // Function to set patient name
    fun setFullName(name: String) {
        _fullName.value = name
    }

    // Function to set gender
    fun setGender(gender: String) {
        _gender.value = gender
    }

    // Function to set age (with validation)
    fun setAge(age: String) {
        if (age.isEmpty() || age.all { it.isDigit() }) {
            _age.value = age
        }
    }

    // Function to set problem description
    fun setProblemDescription(description: String) {
        _problemDescription.value = description
    }

    // Function to load an existing patient
    fun loadPatient(id: String) {
        _isLoading.value = true
        _errorMessage.value = null

        val patient = repository.getPatient(id)
        if (patient != null) {
            _patientId.value = patient.id
            _fullName.value = patient.fullName
            _gender.value = patient.gender
            _age.value = patient.age.toString()
            _problemDescription.value = patient.problemDescription
            _isLoading.value = false
        } else {
            _errorMessage.value = "Patient not found"
            _isLoading.value = false
        }
    }

    // Function to save the patient
    fun savePatient(): Boolean {
        // Reset error and loading states
        _errorMessage.value = null
        _isLoading.value = true

        // Validate inputs
        if (_fullName.value.isBlank()) {
            _errorMessage.value = "Full name is required"
            _isLoading.value = false
            return false
        }

        if (_age.value.isBlank()) {
            _errorMessage.value = "Age is required"
            _isLoading.value = false
            return false
        }

        val ageValue = try {
            _age.value.toInt()
        } catch (e: NumberFormatException) {
            _errorMessage.value = "Please enter a valid age"
            _isLoading.value = false
            return false
        }

        if (ageValue <= 0 || ageValue > 120) {
            _errorMessage.value = "Please enter a valid age between 1 and 120"
            _isLoading.value = false
            return false
        }

        // Save patient
        try {
            repository.savePatient(
                fullName = _fullName.value,
                gender = _gender.value,
                age = ageValue,
                problemDescription = _problemDescription.value,
                patientId = _patientId.value ?: ""
            )

            _isSuccess.value = true
            _isLoading.value = false
            return true
        } catch (e: Exception) {
            _errorMessage.value = "Failed to save patient: ${e.message}"
            _isLoading.value = false
            return false
        }
    }

    // Function to reset the form
    fun resetForm() {
        _patientId.value = null
        _fullName.value = ""
        _gender.value = PatientData.genderOptions.first()
        _age.value = ""
        _problemDescription.value = ""
        _errorMessage.value = null
        _isSuccess.value = false
    }

    // Function to get all patients
    fun getAllPatients(): List<Patient> {
        return repository.getAllPatients()
    }
}