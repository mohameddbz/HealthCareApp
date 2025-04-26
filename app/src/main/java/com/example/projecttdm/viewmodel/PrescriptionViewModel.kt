package com.example.projecttdm.viewmodel

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Prescription
import com.example.projecttdm.data.repository.PrescriptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrescriptionViewModel : ViewModel() {

    private val repository = PrescriptionRepository()

    private val _prescription = MutableStateFlow<Prescription?>(null)
    val prescription: StateFlow<Prescription?> = _prescription

    private val _downloadSuccess = MutableStateFlow<Uri?>(null)
    val downloadSuccess: StateFlow<Uri?> = _downloadSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadSamplePrescription()
    }

    private fun loadSamplePrescription() {
        _prescription.value = repository.getSamplePrescription()
    }

    fun downloadPrescription(context: Context, prescriptionView: View) {
        viewModelScope.launch {
            _isLoading.value = true
            val uri = withContext(Dispatchers.IO) {
                repository.createPdfFromView(context, prescriptionView)
            }
            _downloadSuccess.value = uri
            _isLoading.value = false
        }
    }
}
