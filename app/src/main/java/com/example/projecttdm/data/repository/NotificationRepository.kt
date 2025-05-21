package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.NotificationEndPoint
import com.example.projecttdm.data.model.NotificationResponse
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NotificationRepository(private val endpoint: NotificationEndPoint) {


  fun getNotifications(): Flow<UiState<List<NotificationResponse>>> = flow {
    emit(UiState.Loading)
    try {
      val response = endpoint.getNotifications()
      emit(UiState.Success(response.data))
    } catch (e: Exception) {
      emit(UiState.Error(e.message ?: "Error fetching notifications"))
    }
  }.flowOn(Dispatchers.IO)
  fun markNotificationAsRead(notificationId: Int): Flow<UiState<Unit>> = flow {
    emit(UiState.Loading)
    try {
      endpoint.markNotificationAsRead(notificationId)
      emit(UiState.Success(Unit)) // No data expected back, just success
    } catch (e: Exception) {
      emit(UiState.Error(e.message ?: "Error marking notification as read"))
    }
  }.flowOn(Dispatchers.IO)
}
