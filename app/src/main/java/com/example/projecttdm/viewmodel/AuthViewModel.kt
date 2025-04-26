package com.example.projecttdm.viewmodel

import android.content.Context
import android.net.Uri
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AuthViewModel : ViewModel() {
    private val authRepository = RepositoryHolder.authRepository

    fun getUsers() = authRepository.getUsers()

    private val _authState = MutableStateFlow<UiState<AuthResponse>>(UiState.Init)
    val authState: StateFlow<UiState<AuthResponse>> = _authState.asStateFlow()


    // Champs observables pour email et password
    var email = MutableStateFlow("")
        private set

    var password = MutableStateFlow("")
        private set

    // Fonctions pour mettre à jour les champs
    fun onEmailChanged(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }


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

    // Dans votre ViewModel ou repository
     fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        role: String,
        imageUri: Uri?,
        context : Context
    ) {
        _authState.value = UiState.Loading
        viewModelScope.launch {

            try {
                val firstNamePart = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
                val lastNamePart = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val passwordPart = password.toRequestBody("text/plain".toMediaTypeOrNull())
                val phonePart = phone.toRequestBody("text/plain".toMediaTypeOrNull())
                val rolePart = role.toRequestBody("text/plain".toMediaTypeOrNull())

                // Traitement de l'image
                val imagePart = imageUri?.let { uri ->
                    val contentResolver = context.contentResolver
                    val inputStream = contentResolver.openInputStream(uri)
                    val file = File(context.cacheDir, "upload_image.jpg")
                    inputStream?.use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }

                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                }

                val response = authRepository.register(
                    imagePart,
                    firstNamePart,
                    lastNamePart,
                    emailPart,
                    passwordPart,
                    phonePart,
                    rolePart
                )

                _authState.value = UiState.Success(response)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }


}