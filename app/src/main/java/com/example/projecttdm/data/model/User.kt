
// Modèle de données représentant un utilisateur

package com.example.projecttdm.data.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String // "doctor" or "patient"
)
