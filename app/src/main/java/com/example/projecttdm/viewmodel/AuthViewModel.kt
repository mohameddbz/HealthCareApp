package com.example.projecttdm.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.data.model.auth.LoginRequest
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.firebase.FirebaseService
import com.example.projecttdm.state.UiState
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AuthViewModel(application: Application) : AndroidViewModel(application) {
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

                val userId = when (val state = _authState.value) {
                    is UiState.Success -> state.data.userId  // Assuming userId is the field name
                    else -> null  // Handle other states appropriately
                }

                // Stocker l'ID utilisateur localement
                val sharedPrefs = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                sharedPrefs.edit().putString("user_id", userId.toString()).apply()

                // Récupérer et envoyer le token FCM
                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w("FCM", "❌ Échec de récupération du token", task.exception)
                            return@addOnCompleteListener
                        }

                        val token = task.result
                        Log.d("FCM", "✅ Token FCM récupéré après login : $token")

                        // Stocker le token également
                        val fcmPrefs = getApplication<Application>().getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
                        fcmPrefs.edit().putString("fcm_token", token).apply()

                        FirebaseService.sendTokenToBackend(token, userId.toString())
                    }
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }


    fun logout() {
        viewModelScope.launch {

            try{
                authRepository.logout()
                val sharedPrefs = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                sharedPrefs.edit().remove("user_id").apply()
            }catch(e:Exception){

            }

        }
        // Votre code de déconnexion...

        // Et effacer l'ID utilisateur des préférences

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