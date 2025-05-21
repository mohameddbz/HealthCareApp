//package com.example.projecttdm.data.repository
//
//import com.example.projecttdm.data.endpoint.UserEndPoint
//import com.example.projecttdm.data.model.UserProfileResponse
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import java.io.File
//
//class ProfileRepository(private val profileApiService: UserEndPoint) {
//
//    suspend fun getProfile(): Result<UserProfileResponse> = withContext(Dispatchers.IO) {
//        try {
//            val response = profileApiService.getProfile()
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    return@withContext Result.success(it)
//                } ?: return@withContext Result.failure(Exception("Empty response"))
//            } else {
//                return@withContext Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            return@withContext Result.failure(e)
//        }
//    }
//
//    suspend fun updateProfile(
//        firstName: String,
//        lastName: String,
//        email: String,
//        phone: String,
//        dateBirthday: String?,
//        sexe: String?,
//        imageFile: File?
//    ): Result<UserProfileResponse> = withContext(Dispatchers.IO) {
//        try {
//            // Créer les RequestBody
//            val firstNameBody = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
//            val lastNameBody = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
//            val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
//            val phoneBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
//
//            // Créer les RequestBody pour les paramètres optionnels
//            val dateBirthdayBody = dateBirthday?.toRequestBody("text/plain".toMediaTypeOrNull())
//            val sexeBody = sexe?.toRequestBody("text/plain".toMediaTypeOrNull())
//
//            // Traiter l'image si présente
//            val imagePart = imageFile?.let {
//                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
//                MultipartBody.Part.createFormData("image", it.name, requestFile)
//            }
//            println("Prénom=$firstName, Nom=$lastNameBody, Email=$emailBody, Téléphone=$phoneBody, DateNaissance=$dateBirthdayBody, Sexe=$sexeBody, image=$imagePart")
//            println("Image file exists? ${imageFile?.exists()}")
//            println("Image path: ${imageFile?.absolutePath}")
//            println("Image size: ${imageFile?.length()} bytes")
//            // Effectuer l'appel API
//            val response = profileApiService.updateProfile(
//                firstNameBody,
//                lastNameBody,
//                emailBody,
//                phoneBody,
//                dateBirthdayBody,
//                sexeBody,
//                imagePart
//            )
//
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    return@withContext Result.success(it)
//                } ?: return@withContext Result.failure(Exception("Empty response"))
//            } else {
//                return@withContext Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            return@withContext Result.failure(e)
//        }
//    }
//}

package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.UserEndPoint
import com.example.projecttdm.data.model.UserProfileResponse
import com.example.projecttdm.data.model.ImageBlob
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
            println("DEBUG: Fetching user profile")
            val response = profileApiService.getProfile()
            if (response.isSuccessful) {
                response.body()?.let {
                    println("DEBUG: Profile fetched successfully: ${it.first_name} ${it.last_name}")
                    println("DEBUG: Image data: ${it.image?.type}, size: ${it.image?.data?.size ?: 0} bytes")
                    return@withContext Result.success(it)
                } ?: run {
                    println("DEBUG: Profile response body is null")
                    return@withContext Result.failure(Exception("Empty response"))
                }
            } else {
                println("DEBUG: Error fetching profile: ${response.code()} - ${response.message()}")
                return@withContext Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            println("DEBUG: Exception when fetching profile: ${e.message}")
            e.printStackTrace()
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
            println("DEBUG: Updating profile for $firstName $lastName")

            // Create RequestBody objects
            val firstNameBody = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
            val lastNameBody = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())

            // Create RequestBody objects for optional parameters
            val dateBirthdayBody = dateBirthday?.toRequestBody("text/plain".toMediaTypeOrNull())
            val sexeBody = sexe?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Process image if present
            val imagePart = imageFile?.let {
                println("DEBUG: Image file exists: ${it.exists()}")
                println("DEBUG: Image path: ${it.absolutePath}")
                println("DEBUG: Image size: ${it.length()} bytes")
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", it.name, requestFile)
            }

            println("DEBUG: Sending profile update request")
            println("DEBUG: firstName=$firstName")
            println("DEBUG: lastName=$lastName")
            println("DEBUG: email=$email")
            println("DEBUG: phone=$phone")
            println("DEBUG: dateBirthday=$dateBirthday")
            println("DEBUG: sexe=$sexe")
            println("DEBUG: image=${imageFile}")

            // Make API call
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
                    println("DEBUG: Profile updated successfully")
                    println("DEBUG: Updated image data: ${it.image?.type}, size: ${it.image?.data?.size ?: 0} bytes")
                    return@withContext Result.success(it)
                } ?: run {
                    println("DEBUG: Profile update response body is null")
                    return@withContext Result.failure(Exception("Empty response"))
                }
            } else {
                println("DEBUG: Error updating profile: ${response.code()} - ${response.message()}")
                return@withContext Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            println("DEBUG: Exception when updating profile: ${e.message}")
            e.printStackTrace()
            return@withContext Result.failure(e)
        }
    }
}