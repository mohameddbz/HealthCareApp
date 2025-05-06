package com.example.projecttdm.viewmodel


import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.data.model.FullPrescription
import com.example.projecttdm.data.model.Medications
import com.example.projecttdm.data.model.Prescription
import com.example.projecttdm.data.model.PrescriptionResponse
import com.example.projecttdm.data.model.Prescriptions
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class PrescriptionContentViewModel : ViewModel() {
    private val prescriptionRepository = RepositoryHolder.prescriptionRepository


    private val _prescription = MutableStateFlow<FullPrescription?>(null)
    val prescription: StateFlow<FullPrescription?> = _prescription

    private val _downloadSuccess = MutableStateFlow<Uri?>(null)
    val downloadSuccess: StateFlow<Uri?> = _downloadSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private fun loadSamplePrescription() {
        _prescription.value = prescriptionRepository.getSamplePrescription()
    }

    fun downloadPrescription(context: Context, prescriptionView: View) {
        viewModelScope.launch {
            _isLoading.value = true
            val uri = withContext(Dispatchers.IO) {
                prescriptionRepository.createPdfFromView(context, prescriptionView)
            }
            _downloadSuccess.value = uri
            _isLoading.value = false
        }
    }

    fun fetchPrescriptionById(it: String) {
          viewModelScope.launch {
              _isLoading.value = true
               val response :PrescriptionResponse  =  prescriptionRepository.getPrescriptionById(it)
              println("----------------------------------------------------------------------------")
              println("---------------------------------------------------------------------------")
               println(response)
               if(response.success){
                   _prescription.value = response.prescription
               }
              _isLoading.value = false
          }
    }


}