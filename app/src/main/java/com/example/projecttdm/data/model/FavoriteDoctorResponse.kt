package com.example.projecttdm.data.model

data class FavoriteDoctorResponse(
    val doctor_id: Int,
    val name: String,
    val image: ImageBlob? = null,
    val clinique_name: String?,
    val specialty: String?, // seulement le nom de la spécialité
    val rating: Float?,
    val reviewCount: Int?
)

