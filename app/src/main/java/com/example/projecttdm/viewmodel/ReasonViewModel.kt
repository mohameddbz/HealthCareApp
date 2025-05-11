package com.example.projecttdm.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.RescheduleReason
import com.example.projecttdm.data.repository.ReasonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReasonViewModel(
    private val repository: ReasonRepository
) : ViewModel() {

    private val _reasons = MutableStateFlow<List<RescheduleReason>>(emptyList())
    val reasons: StateFlow<List<RescheduleReason>> = _reasons

    private val _selectedReason = MutableStateFlow("")
    val selectedReason: StateFlow<String> = _selectedReason

    private val _additionalInfo = MutableStateFlow("")
    val additionalInfo: StateFlow<String> = _additionalInfo

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    init {
        loadReasons()
    }

    private fun loadReasons() {
       // _reasons.value = repository.getRescheduleReasons()
    }

//    fun onReasonSelected(reason: String) {
//        _selectedReason.value = reason
//        if (reason != "others") {
//            _additionalInfo.value = ""
//        } else {
//            // Load the last used custom reason if available
//            _additionalInfo.value = repository.getLastCustomReason()
//        }
//    }
//
//    fun onAdditionalInfoChanged(info: String) {
//        _additionalInfo.value = info
//
//        // Save as last used custom reason for convenience
//        if (_selectedReason.value == "others") {
//            repository.saveLastCustomReason(info)
//        }
//    }

//    fun saveOtherReasonIfNeeded() {
//        if (_selectedReason.value == "others" && _additionalInfo.value.isNotBlank()) {
//            _isSaving.value = true
//
//            viewModelScope.launch {
//                try {
//                    // Save the custom reason
//                    repository.saveOtherReason(_additionalInfo.value)
//
//                    // Refresh reasons list
//                    loadReasons()
//                } finally {
//                    _isSaving.value = false
//                }
//            }
//        }
//    }

    // Factory to provide dependencies
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReasonViewModel::class.java)) {
                return ReasonViewModel(ReasonRepository(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}