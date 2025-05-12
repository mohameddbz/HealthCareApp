// Gère la logique d'authentification

package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.AuthEndPoint
import com.example.projecttdm.data.local.StaticData
import com.example.projecttdm.data.model.User
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.data.model.auth.LoginRequest
import com.example.projecttdm.data.model.auth.LogoutResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepository(private val endpoint: AuthEndPoint) {
    fun getUsers(): List<User> = StaticData.users

    suspend fun login(loginRequest: LoginRequest): AuthResponse {
        return endpoint.login(loginRequest)
    }

    suspend fun  logout(): LogoutResponse {
        return endpoint.logout()
    }

    suspend fun  register (
        imagePart: MultipartBody.Part?,
        firstNamePart: RequestBody,
        lastNamePart: RequestBody,
        emailPart: RequestBody,
        passwordPart: RequestBody,
        phonePart: RequestBody,
        rolePart: RequestBody,
        sexe : RequestBody,
        date_birthday : RequestBody
    ):AuthResponse {
        return  endpoint.register(imagePart,firstNamePart,lastNamePart,emailPart,passwordPart,phonePart,sexe,date_birthday,rolePart)
    }
}