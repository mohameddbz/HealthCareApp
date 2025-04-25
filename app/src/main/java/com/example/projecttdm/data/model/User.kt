
// Modèle de données représentant un utilisateur

package com.example.projecttdm.data.model
/*
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String // "doctor" or "patient"
)

 */
// Créer une classe pour représenter l'objet BLOB
data class ImageBlob(
    val type: String,
    val data: List<Int>  // Ou ByteArray si vous utilisez un adaptateur personnalisé
)

data class User(
    val user_id: Int,
    val role: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val image: ImageBlob?
)