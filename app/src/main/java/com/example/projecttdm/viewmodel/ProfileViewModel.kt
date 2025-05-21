//package com.example.projecttdm.viewmodel
//
//import android.content.Context
//import android.net.Uri
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.projecttdm.data.model.UserProfileResponse
//import com.example.projecttdm.data.repository.ProfileRepository
//import com.example.projecttdm.data.repository.RepositoryHolder
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//@RequiresApi(Build.VERSION_CODES.O)
//class ProfileViewModel() : ViewModel() {
//
//    private val repository= RepositoryHolder.profileRepository
//    // État du profil
//    var profileState by mutableStateOf<UserProfileResponse?>(null)
//        private set
//
//    // État d'édition
//    var isEditing by mutableStateOf(false)
//        private set
//
//    // État de chargement
//    var isLoading by mutableStateOf(false)
//        private set
//
//    // Message d'erreur
//    var errorMessage by mutableStateOf<String?>(null)
//        private set
//
//    // Nouvelle image sélectionnée (URI local)
//    var selectedImageUri by mutableStateOf<Uri?>(null)
//        private set
//
//    // Profil original pour annuler les modifications
//    private var originalProfile: UserProfileResponse? = null
//
//    // Format de date pour l'API
//    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//
//    // Format de date pour l'affichage
//    private val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//
//    init {
//        loadProfile()
//    }
//
//    private fun loadProfile() {
//        viewModelScope.launch {
//            isLoading = true
//            errorMessage = null
//
//            try {
//                val result = repository.getProfile()
//                println("=============== ${result}")
//                result.onSuccess { profile ->
//                    profileState = profile
//                    originalProfile = profile
//                }.onFailure { error ->
//                    errorMessage = "Erreur lors du chargement du profil: ${error.message}"
//                }
//            } catch (e: Exception) {
//                errorMessage = "Erreur lors du chargement du profil: ${e.message}"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//    fun toggleEditMode() {
//        if (isEditing) {
//            // Annuler les modifications
//            profileState = originalProfile
//            selectedImageUri = null
//        } else {
//            // Sauvegarder l'état original avant de commencer l'édition
//            originalProfile = profileState
//        }
//        isEditing = !isEditing
//    }
//
//    fun updateFirstName(value: String) {
//        profileState = profileState?.copy(first_name = value)
//    }
//
//    fun updateLastName(value: String) {
//        profileState = profileState?.copy(last_name = value)
//    }
//
//    fun updateEmail(value: String) {
//        profileState = profileState?.copy(email = value)
//    }
//
//    fun updatePhone(value: String) {
//        profileState = profileState?.copy(phone = value)
//    }
//
//    fun updateBirthDate(date: String) {
//        profileState = profileState?.copy(
//            PATIENT = profileState?.PATIENT?.copy(
//                date_birthday = date
//            ) ?: return
//        )
//    }
//
//    fun updateSexe(sexe: String) {
//        profileState = profileState?.copy(
//            PATIENT = profileState?.PATIENT?.copy(
//                sexe = sexe
//            ) ?: return
//        )
//    }
//
//    fun setSelectedImage(uri: Uri?) {
//        selectedImageUri = uri
//    }
//
//    // Ajoutez cette fonction dans votre ViewModel
//    private suspend fun uriToFile(context: Context, uri: Uri): File? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val contentResolver = context.contentResolver
//                val file = File.createTempFile(
//                    "profile_img_${System.currentTimeMillis()}",
//                    ".jpg",
//                    context.cacheDir
//                )
//
//                contentResolver.openInputStream(uri)?.use { input ->
//                    file.outputStream().use { output ->
//                        input.copyTo(output)
//                    }
//                }
//
//                file
//            } catch (e: Exception) {
//                null
//            }
//        }
//    }
//
//    // Modifiez saveProfile pour utiliser cette conversion
//    fun saveProfile(context: Context) {
//        viewModelScope.launch {
//            isLoading = true
//            errorMessage = null
//
//            try {
//                profileState?.let { profile ->
//                    val imageFile = selectedImageUri?.let { uri ->
//                        uriToFile(context, uri)
//                    }
//
//                    val result = repository.updateProfile(
//                        firstName = profile.first_name,
//                        lastName = profile.last_name,
//                        email = profile.email,
//                        phone = profile.phone,
//                        dateBirthday = profile.PATIENT?.date_birthday,
//                        sexe = profile.PATIENT?.sexe,
//                        imageFile = imageFile
//                    )
//
//                    result.onSuccess { updatedProfile ->
//                        profileState = updatedProfile
//                        originalProfile = updatedProfile
//                        selectedImageUri = null
//                        isEditing = false
//                    }.onFailure { error ->
//                        errorMessage = "Erreur lors de la mise à jour: ${error.message}"
//                    }
//                }
//            } catch (e: Exception) {
//                errorMessage = "Erreur: ${e.message}"
//            } finally {
//                isLoading = false
//                isEditing=false
//            }
//        }
//    }
//
//    fun logout() {
//        viewModelScope.launch {
//            // Dans une application réelle, vous implémenteriez la déconnexion ici
//            // authRepository.logout()
//        }
//    }
//
//    // Convertir la date de l'API en Date
//    fun parseApiDate(apiDate: String?): Date? {
//        return apiDate?.let {
//            try {
//                apiDateFormat.parse(it)
//            } catch (e: Exception) {
//                // Essayer un autre format si le premier échoue
//                try {
//                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
//                } catch (e: Exception) {
//                    null
//                }
//            }
//        }
//    }
//
//    // Formater la date pour l'affichage
//    fun formatDateForDisplay(apiDate: String?): String {
//        val date = parseApiDate(apiDate)
//        return date?.let { displayDateFormat.format(it) } ?: "Non spécifiée"
//    }
//
//    // Convertir une Date en format API
//    fun formatDateForApi(date: Date): String {
//        return apiDateFormat.format(date)
//    }
//
//    fun updateProfileImage(imageUri: String) {
//        profileState = profileState?.copy(image = imageUri)
//        selectedImageUri = Uri.parse(imageUri)
//    }
//
//    // Update the saveProfile function to handle image conversion properly:
//    fun saveProfile() {
//        viewModelScope.launch {
//            isLoading = true
//            errorMessage = null
//
//            try {
//                profileState?.let { profile ->
//                    // Convert selectedImageUri to File if it exists
//                    val imageFile = selectedImageUri?.let { uri ->
//                        // In a real app, you would implement proper URI to File conversion here
//                        // This is a placeholder implementation
//                        null // Replace with actual conversion code in production
//                    }
//
//                    val result = repository.updateProfile(
//                        firstName = profile.first_name,
//                        lastName = profile.last_name,
//                        email = profile.email,
//                        phone = profile.phone,
//                        dateBirthday = profile.PATIENT?.date_birthday,
//                        sexe = profile.PATIENT?.sexe,
//                        imageFile = imageFile
//                    )
//
//                    result.onSuccess { updatedProfile ->
//                        profileState = updatedProfile
//                        originalProfile = updatedProfile
//                        selectedImageUri = null
//                        isEditing = false
//                    }.onFailure { error ->
//                        errorMessage = "Erreur lors de la mise à jour du profil: ${error.message}"
//                    }
//                }
//            } catch (e: Exception) {
//                errorMessage = "Erreur lors de la mise à jour du profil: ${e.message}"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//}

package com.example.projecttdm.viewmodel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecttdm.data.model.ImageBlob
import com.example.projecttdm.data.model.UserProfileResponse
import com.example.projecttdm.data.repository.ProfileRepository
import com.example.projecttdm.data.repository.RepositoryHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class ProfileViewModel() : ViewModel() {

    private val repository = RepositoryHolder.profileRepository

    // Profile state
    var profileState by mutableStateOf<UserProfileResponse?>(null)
        private set

    // Editing state
    var isEditing by mutableStateOf(false)
        private set

    // Loading state
    var isLoading by mutableStateOf(false)
        private set

    // Error message
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Selected image URI (local)
    var selectedImageUri by mutableStateOf<Uri?>(null)
        private set

    // Local image blob for new selected image
    private var localImageBlob: ImageBlob? = null

    // Original profile for cancelling changes
    private var originalProfile: UserProfileResponse? = null

    // Date format for API
    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    // Date format for display
    private val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                println("DEBUG: Loading profile from repository")
                val result = repository.getProfile()
                result.onSuccess { profile ->
                    println("DEBUG: Profile loaded successfully: ${profile.first_name} ${profile.last_name}")
                    println("DEBUG: Image blob: ${profile.image}")
                    profileState = profile
                    originalProfile = profile
                }.onFailure { error ->
                    println("DEBUG: Failed to load profile: ${error.message}")
                    errorMessage = "Error loading profile: ${error.message}"
                }
            } catch (e: Exception) {
                println("DEBUG: Exception while loading profile: ${e.message}")
                e.printStackTrace()
                errorMessage = "Error loading profile: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleEditMode() {
        if (isEditing) {
            // Cancel changes
            profileState = originalProfile
            selectedImageUri = null
            localImageBlob = null
        } else {
            // Save original state before editing
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

    // Convert URI to File
    private suspend fun uriToFile(context: Context, uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            try {
                println("DEBUG: Converting URI to file: $uri")
                val contentResolver = context.contentResolver
                val file = File.createTempFile(
                    "profile_img_${System.currentTimeMillis()}",
                    ".jpg",
                    context.cacheDir
                )

                contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                println("DEBUG: File created: ${file.absolutePath}, size: ${file.length()} bytes")
                file
            } catch (e: Exception) {
                println("DEBUG: Failed to convert URI to file: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    // Convert URI to ImageBlob
    private suspend fun uriToImageBlob(context: Context, uri: Uri): ImageBlob? {
        return withContext(Dispatchers.IO) {
            try {
                println("DEBUG: Converting URI to ImageBlob: $uri")
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(uri) ?: return@withContext null

                val bytes = inputStream.use { it.readBytes() }
                val mimeType = contentResolver.getType(uri) ?: "image/jpeg"

                // Convert bytes to List<Int>
                val intList = bytes.map { it.toInt() and 0xFF }

                println("DEBUG: Created ImageBlob of type $mimeType with ${intList.size} bytes")
                ImageBlob(type = mimeType, data = intList)
            } catch (e: Exception) {
                println("DEBUG: Failed to convert URI to ImageBlob: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    fun saveProfile(context: Context) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                println("DEBUG: Starting profile save process")
                profileState?.let { profile ->
                    val imageFile = selectedImageUri?.let { uri ->
                        println("DEBUG: Processing selected image: $uri")
                        uriToFile(context, uri)
                    }

                    println("DEBUG: Calling repository.updateProfile")
                    val result = repository.updateProfile(
                        firstName = profile.first_name,
                        lastName = profile.last_name,
                        email = profile.email,
                        phone = profile.phone,
                        dateBirthday = profile.PATIENT.date_birthday,
                        sexe = profile.PATIENT.sexe,
                        imageFile = imageFile
                    )

                    result.onSuccess { updatedProfile ->
                        println("DEBUG: Profile updated successfully")
                        profileState = updatedProfile
                        originalProfile = updatedProfile
                        selectedImageUri = null
                        localImageBlob = null
                        isEditing = false
                    }.onFailure { error ->
                        println("DEBUG: Failed to update profile: ${error.message}")
                        errorMessage = "Error updating profile: ${error.message}"
                    }
                }
            } catch (e: Exception) {
                println("DEBUG: Exception while saving profile: ${e.message}")
                e.printStackTrace()
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
                isEditing = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            println("DEBUG: Logout requested")
            // In a real app, implement logout logic here
            // authRepository.logout()
        }
    }

    // Parse API date to Date object
    fun parseApiDate(apiDate: String?): Date? {
        return apiDate?.let {
            try {
                apiDateFormat.parse(it)
            } catch (e: Exception) {
                // Try another format if first fails
                try {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
                } catch (e: Exception) {
                    println("DEBUG: Failed to parse date: $it")
                    null
                }
            }
        }
    }

    // Format date for display
    fun formatDateForDisplay(apiDate: String?): String {
        val date = parseApiDate(apiDate)
        return date?.let { displayDateFormat.format(it) } ?: "Not specified"
    }

    // Convert Date to API format
    fun formatDateForApi(date: Date): String {
        return apiDateFormat.format(date)
    }

    // Update profile image with URI and create local ImageBlob
    fun updateProfileImage(context: Context, imageUri: String) {
        println("DEBUG: Updating profile image with URI: $imageUri")
        selectedImageUri = Uri.parse(imageUri)

        viewModelScope.launch {
            try {
                if (selectedImageUri != null) {
                    localImageBlob = uriToImageBlob(context, selectedImageUri!!)
                    println("DEBUG: Created local ImageBlob: $localImageBlob")
                }
            } catch (e: Exception) {
                println("DEBUG: Failed to process local image: ${e.message}")
            }
        }
    }

}