package com.example.projecttdm.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Notification
import com.example.projecttdm.data.model.NotificationResponse
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class NotificationViewModel : ViewModel() {

    private val notificationRepository = RepositoryHolder.notificationRepository

    private val _notifications = MutableLiveData<List<NotificationResponse>>(emptyList())
    val notifications: LiveData<List<NotificationResponse>> = _notifications

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            notificationRepository.getNotifications().collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        _isLoading.value = true
                        _error.value = null
                    }
                    is UiState.Success -> {
                        _isLoading.value = false
                        _notifications.value = state.data
                    }
                    is UiState.Error -> {
                        _isLoading.value = false
                        _error.value = state.message
                        _notifications.value = emptyList()
                    }
                    UiState.Init -> {}
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            notificationRepository.markNotificationAsRead(notificationId).collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        _isLoading.value = true
                        _error.value = null
                    }
                    is UiState.Success -> {
                        _isLoading.value = false
                        // Update locally the notification's is_read flag
                        _notifications.value = _notifications.value?.map { notification ->
                            if (notification.notification_id == notificationId) {
                                notification.copy(is_read = true)
                            } else {
                                notification
                            }
                        }
                    }
                    is UiState.Error -> {
                        _isLoading.value = false
                        _error.value = state.message
                    }
                    UiState.Init -> {}
                }
            }
        }
    }


}

