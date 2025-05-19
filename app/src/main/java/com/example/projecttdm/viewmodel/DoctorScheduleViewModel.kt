package com.example.projecttdm.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Docschedule
import com.example.projecttdm.data.repository.DoctorScheduleRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class DoctorScheduleViewModel : ViewModel() {

    private val repository = RepositoryHolder.doctorScheduleRepository
    private val _uiState = MutableStateFlow<DoctorScheduleUiState>(DoctorScheduleUiState.Initial)
    val uiState: StateFlow<DoctorScheduleUiState> = _uiState.asStateFlow()

    // Selected values for schedule creation
    private val _selectedDay = MutableLiveData<String>()
    val selectedDay: LiveData<String> = _selectedDay

    private val _selectedDuration = MutableLiveData(30) // Default 30 minutes
    val selectedDuration: LiveData<Int> = _selectedDuration

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String> = _startTime

    private val _endTime = MutableLiveData<String>()
    val endTime: LiveData<String> = _endTime

    // Set selected values
    fun setSelectedDay(day: String) {
        _selectedDay.value = day
    }

    fun setSelectedDuration(duration: Int) {
        _selectedDuration.value = duration
    }

    fun setStartTime(time: String) {
        _startTime.value = time
    }

    fun setEndTime(time: String) {
        _endTime.value = time
    }

    fun validateInputs(): Boolean {
        return !_selectedDay.value.isNullOrEmpty() &&
                !_startTime.value.isNullOrEmpty() &&
                !_endTime.value.isNullOrEmpty() &&
                _selectedDuration.value != null
    }

    // Create schedules for selected day
    fun createSchedulesForDay(doctorId: Int) {
        if (!validateInputs()) {
            _uiState.value = DoctorScheduleUiState.Error("All fields must be filled")
            return
        }

        val day = _selectedDay.value!!
        val startTime = _startTime.value!!
        val endTime = _endTime.value!!
        val duration = _selectedDuration.value!!

        viewModelScope.launch {
            _uiState.value = DoctorScheduleUiState.Loading

            val result = repository.createSchedulesByDay(
                dayName = translateDayToFrench(day),
                doctorId = 1,
                startTime = startTime,
                endTime = endTime,
                appointmentDuration = duration
            )

            _uiState.value = result.fold(
                onSuccess = { DoctorScheduleUiState.Success(it) },
                onFailure = { DoctorScheduleUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    // Convert English day name to French for the API
    private fun translateDayToFrench(englishDay: String): String {
        return when (englishDay.lowercase()) {
            "monday" -> "lundi"
            "tuesday" -> "mardi"
            "wednesday" -> "mercredi"
            "thursday" -> "jeudi"
            "friday" -> "vendredi"
            "saturday" -> "samedi"
            "sunday" -> "dimanche"
            else -> englishDay // Return as is if already in French or invalid
        }
    }

    // Reset the state after handling
    fun resetState() {
        _uiState.value = DoctorScheduleUiState.Initial
    }
}

sealed class DoctorScheduleUiState {
    object Initial : DoctorScheduleUiState()
    object Loading : DoctorScheduleUiState()
    data class Success(val schedules: List<Docschedule>) : DoctorScheduleUiState()
    data class Error(val message: String) : DoctorScheduleUiState()
}