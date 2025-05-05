// Gère la logique d'authentification

package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.AuthEndPoint
import com.example.projecttdm.data.local.StaticData
import com.example.projecttdm.data.model.User
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.data.model.auth.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepository(private val endpoint: AuthEndPoint) {
    fun getUsers(): List<User> = StaticData.users

    suspend fun login(loginRequest: LoginRequest): AuthResponse {
        println(loginRequest)
        println("loginRequest")
        println(endpoint.login(loginRequest))
        println("login")
        return endpoint.login(loginRequest)
    }

    suspend fun  register (
        imagePart: MultipartBody.Part?,
        firstNamePart: RequestBody,
        lastNamePart: RequestBody,
        emailPart: RequestBody,
        passwordPart: RequestBody,
        phonePart: RequestBody,
        rolePart: RequestBody
    ):AuthResponse {
        return  endpoint.register(imagePart,firstNamePart,lastNamePart,emailPart,passwordPart,phonePart,rolePart)
    }
}