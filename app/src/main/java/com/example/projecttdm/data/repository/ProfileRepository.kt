package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.UserEndPoint
import com.example.projecttdm.data.model.UserProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileRepository(private val profileApiService: UserEndPoint) {

    suspend fun getProfile(): Result<UserProfileResponse> = withContext(Dispatchers.IO) {
        try {
            val response = profileApiService.getProfile()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@withContext Result.success(it)
                } ?: return@withContext Result.failure(Exception("Empty response"))
            } else {
                return@withContext Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun updateProfile(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        dateBirthday: String?,
        sexe: String?,
        imageFile: File?
    ): Result<UserProfileResponse> = withContext(Dispatchers.IO) {
        try {
            // Créer les RequestBody
            val firstNameBody = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
            val lastNameBody = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())

            // Créer les RequestBody pour les paramètres optionnels
            val dateBirthdayBody = dateBirthday?.toRequestBody("text/plain".toMediaTypeOrNull())
            val sexeBody = sexe?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Traiter l'image si présente
            val imagePart = imageFile?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", it.name, requestFile)
            }

            // Effectuer l'appel API
            val response = profileApiService.updateProfile(
                firstNameBody,
                lastNameBody,
                emailBody,
                phoneBody,
                dateBirthdayBody,
                sexeBody,
                imagePart
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    return@withContext Result.success(it)
                } ?: return@withContext Result.failure(Exception("Empty response"))
            } else {
                return@withContext Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}