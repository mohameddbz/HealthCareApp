package com.example.projecttdm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.Notification
import com.example.projecttdm.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {
    private val notificationRepository : NotificationRepository =  NotificationRepository() ;

    private val _notificationData = MutableStateFlow<List<Notification>>(emptyList())
    val notificationData : StateFlow<List<Notification>> = _notificationData.asStateFlow()

    fun getNotifications() {
        viewModelScope.launch {
            val notificationsResults = notificationRepository.getNotificationsOfUser()
            _notificationData.value = notificationsResults
        }
    }
}