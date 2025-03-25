// Gère la logique d'authentification

package com.example.projecttdm.data.repository

import com.example.projecttdm.data.local.StaticData
import com.example.projecttdm.data.model.User

class AuthRepository {
    fun getUsers(): List<User> = StaticData.users
}