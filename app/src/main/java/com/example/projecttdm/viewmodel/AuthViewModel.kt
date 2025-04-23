package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.data.model.auth.LoginRequest
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val authRepository = RepositoryHolder.authRepository

    fun getUsers() = authRepository.getUsers()

    private val _authState = MutableStateFlow<UiState<AuthResponse>>(UiState.Init)
    val authState: StateFlow<UiState<AuthResponse>> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        val request = LoginRequest(email, password)

        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                val response = authRepository.login(request)
                _authState.value = UiState.Success(response)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }
}