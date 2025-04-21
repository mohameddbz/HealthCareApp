package com.example.projecttdm.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.AppointmentReviewData
import com.example.projecttdm.data.repository.AppointmentSummaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AppointmentSummaryViewModel(
    private val repository: AppointmentSummaryRepository
) : ViewModel() {

    // UI state as a StateFlow
    private val _uiState = MutableStateFlow<AppointmentUiState>(AppointmentUiState.Loading)
    val uiState: StateFlow<AppointmentUiState> = _uiState

    // Sealed class to represent different UI states
    sealed class AppointmentUiState {
        object Loading : AppointmentUiState()
        data class Success(val data: AppointmentReviewData) : AppointmentUiState()
        data class Error(val message: String) : AppointmentUiState()
    }

    /**
     * Loads appointment review data for a specific appointment ID
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadAppointmentReviewData(appointmentId: String) {
        viewModelScope.launch {
            _uiState.value = AppointmentUiState.Loading

            repository.getAppointmentReviewData(appointmentId)
                .catch { exception ->
                    _uiState.value = AppointmentUiState.Error(
                        message = exception.localizedMessage ?: "Unknown error occurred"
                    )
                }
                .collect { appointmentData ->
                    _uiState.value = AppointmentUiState.Success(appointmentData)
                }
        }
    }


}