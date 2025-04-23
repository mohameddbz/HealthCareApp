// Gère la logique d'authentification

package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.AuthEndPoint
import com.example.projecttdm.data.local.StaticData
import com.example.projecttdm.data.model.User
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.data.model.auth.LoginRequest

class AuthRepository(private val endpoint: AuthEndPoint) {
    fun getUsers(): List<User> = StaticData.users

    suspend fun login(loginRequest: LoginRequest): AuthResponse {
        return endpoint.login(loginRequest)
    }
}