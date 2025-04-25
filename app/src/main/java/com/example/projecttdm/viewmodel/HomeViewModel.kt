package com.example.projecttdm.viewmodel

import com.example.projecttdm.data.model.User



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val userRepository = RepositoryHolder.UserRepository


    private val _currentUser = MutableStateFlow<UiState<User>>(UiState.Init)
    val currentUser: StateFlow<UiState<User>> = _currentUser.asStateFlow()


    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = UiState.Loading
            try {
                val response = userRepository.getCurrentUser()
                _currentUser.value = UiState.Success(response)
            } catch (e: Exception) {
                _currentUser.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    init {
        getCurrentUser()
    }




}