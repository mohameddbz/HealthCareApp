package com.example.projecttdm.data.model

import com.google.gson.annotations.SerializedName


data class Specialty(
    @SerializedName("specialty_id")
    val id: String,
    val name: String
)