package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.ApiClient
import com.example.projecttdm.data.endpoint.AuthEndPoint
import com.example.projecttdm.data.endpoint.UserEndPoint


object RepositoryHolder {

    val authRepository by lazy {
        AuthRepository(ApiClient.create(AuthEndPoint::class.java))
    }
    val UserRepository by lazy {
        UserRepository(ApiClient.create(UserEndPoint::class.java))
    }
}