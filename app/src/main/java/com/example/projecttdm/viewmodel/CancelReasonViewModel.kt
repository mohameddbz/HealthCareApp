package com.example.projecttdm.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.local.CancelReasonData
import com.example.projecttdm.data.model.AppointementResponse
import com.example.projecttdm.data.model.RescheduleReason
import com.example.projecttdm.data.repository.ReasonRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class CancelReasonViewModel() : ViewModel() {

    private val repository = RepositoryHolder.appointmentRepository

    private val _reasons = MutableStateFlow<List<RescheduleReason>>(emptyList())
    val reasons: StateFlow<List<RescheduleReason>> = _reasons

    private val _selectedReason = MutableStateFlow("")
    val selectedReason: StateFlow<String> = _selectedReason

    private val _additionalInfo = MutableStateFlow("")
    val additionalInfo: StateFlow<String> = _additionalInfo

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _cancelAppointmentState = MutableStateFlow<UiState<AppointementResponse>>(UiState.Loading)
    val cancelAppointmentState: StateFlow<UiState<AppointementResponse>> = _cancelAppointmentState

    init {
        loadReasons()
    }

    private fun loadReasons() {
        val standardReasons = CancelReasonData.cancelReasons
        val allReasons = standardReasons.toMutableList()
        _reasons.value = allReasons
    }

    fun onReasonSelected(reason: String) {
        _selectedReason.value = reason
//        if (reason != "others") {
//            _additionalInfo.value = ""
//        } else {
//            // Load the last used custom reason if available
//            _additionalInfo.value = repository.getLastCustomCancelReason()
//        }
    }

//    fun onAdditionalInfoChanged(info: String) {
//        _additionalInfo.value = info
//
//        // Save as last used custom reason for convenience
//        if (_selectedReason.value == "others") {
//            repository.saveLastCustomCancelReason(info)
//        }
//    }

//    fun saveOtherReasonIfNeeded() {
//        if (_selectedReason.value == "others" && _additionalInfo.value.isNotBlank()) {
//            _isSaving.value = true
//
//            viewModelScope.launch {
//                try {
//                    // Save the custom reason
//                    repository.saveOtherCancelReason(_additionalInfo.value)
//
//                    // Refresh reasons list
//                    loadReasons()
//                } finally {
//                    _isSaving.value = false
//                }
//            }
//        }
//    }

    fun cancelAppointment(appointmentId: String) {
        _cancelAppointmentState.value = UiState.Loading

        viewModelScope.launch {
            val state = repository.cancelAppointment(appointmentId)
            _cancelAppointmentState.value = state
        }
    }

}