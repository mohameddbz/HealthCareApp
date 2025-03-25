package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.repository.AuthRepository


class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    fun getUsers() = authRepository.getUsers()
}