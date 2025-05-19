package com.example.projecttdm.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.UserProfileResponse
import com.example.projecttdm.data.repository.ProfileRepository
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    // État du profil
    var profileState by mutableStateOf<UserProfileResponse?>(null)
        private set

    // État d'édition
    var isEditing by mutableStateOf(false)
        private set

    // État de chargement
    var isLoading by mutableStateOf(false)
        private set

    // Message d'erreur
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Nouvelle image sélectionnée (URI local)
    var selectedImageUri by mutableStateOf<Uri?>(null)
        private set

    // Profil original pour annuler les modifications
    private var originalProfile: UserProfileResponse? = null

    // Format de date pour l'API
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    // Format de date pour l'affichage
    private val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val result = repository.getProfile()
                result.onSuccess { profile ->
                    profileState = profile
                    originalProfile = profile
                }.onFailure { error ->
                    errorMessage = "Erreur lors du chargement du profil: ${error.message}"
                }
            } catch (e: Exception) {
                errorMessage = "Erreur lors du chargement du profil: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleEditMode() {
        if (isEditing) {
            // Annuler les modifications
            profileState = originalProfile
            selectedImageUri = null
        } else {
            // Sauvegarder l'état original avant de commencer l'édition
            originalProfile = profileState
        }
        isEditing = !isEditing
    }

    fun updateFirstName(value: String) {
        profileState = profileState?.copy(first_name = value)
    }

    fun updateLastName(value: String) {
        profileState = profileState?.copy(last_name = value)
    }

    fun updateEmail(value: String) {
        profileState = profileState?.copy(email = value)
    }

    fun updatePhone(value: String) {
        profileState = profileState?.copy(phone = value)
    }

    fun updateBirthDate(date: String) {
        profileState = profileState?.copy(
            PATIENT = profileState?.PATIENT?.copy(
                date_birthday = date
            ) ?: return
        )
    }

    fun updateSexe(sexe: String) {
        profileState = profileState?.copy(
            PATIENT = profileState?.PATIENT?.copy(
                sexe = sexe
            ) ?: return
        )
    }

    fun setSelectedImage(uri: Uri?) {
        selectedImageUri = uri
    }

    fun saveProfile(imageFile: File?) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                profileState?.let { profile ->
                    val result = repository.updateProfile(
                        firstName = profile.first_name,
                        lastName = profile.last_name,
                        email = profile.email,
                        phone = profile.phone,
                        dateBirthday = profile.PATIENT?.date_birthday,
                        sexe = profile.PATIENT?.sexe,
                        imageFile = imageFile
                    )

                    result.onSuccess { updatedProfile ->
                        profileState = updatedProfile
                        originalProfile = updatedProfile
                        selectedImageUri = null
                        isEditing = false
                    }.onFailure { error ->
                        errorMessage = "Erreur lors de la mise à jour du profil: ${error.message}"
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Erreur lors de la mise à jour du profil: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Dans une application réelle, vous implémenteriez la déconnexion ici
            // authRepository.logout()
        }
    }

    // Convertir la date de l'API en Date
    fun parseApiDate(apiDate: String?): Date? {
        return apiDate?.let {
            try {
                apiDateFormat.parse(it)
            } catch (e: Exception) {
                // Essayer un autre format si le premier échoue
                try {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    // Formater la date pour l'affichage
    fun formatDateForDisplay(apiDate: String?): String {
        val date = parseApiDate(apiDate)
        return date?.let { displayDateFormat.format(it) } ?: "Non spécifiée"
    }

    // Convertir une Date en format API
    fun formatDateForApi(date: Date): String {
        return apiDateFormat.format(date)
    }

    fun updateProfileImage(imageUri: String) {
        profileState = profileState?.copy(image = imageUri)
        selectedImageUri = Uri.parse(imageUri)
    }

    // Update the saveProfile function to handle image conversion properly:
    fun saveProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                profileState?.let { profile ->
                    // Convert selectedImageUri to File if it exists
                    val imageFile = selectedImageUri?.let { uri ->
                        // In a real app, you would implement proper URI to File conversion here
                        // This is a placeholder implementation
                        null // Replace with actual conversion code in production
                    }

                    val result = repository.updateProfile(
                        firstName = profile.first_name,
                        lastName = profile.last_name,
                        email = profile.email,
                        phone = profile.phone,
                        dateBirthday = profile.PATIENT?.date_birthday,
                        sexe = profile.PATIENT?.sexe,
                        imageFile = imageFile
                    )

                    result.onSuccess { updatedProfile ->
                        profileState = updatedProfile
                        originalProfile = updatedProfile
                        selectedImageUri = null
                        isEditing = false
                    }.onFailure { error ->
                        errorMessage = "Erreur lors de la mise à jour du profil: ${error.message}"
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Erreur lors de la mise à jour du profil: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}