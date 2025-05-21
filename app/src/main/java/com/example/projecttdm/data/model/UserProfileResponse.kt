//package com.example.projecttdm.data.model
//
//import java.sql.Blob
//
//data class UserProfileResponse(
//    val user_id: Int,
//    val role: String,
//    val first_name: String,
//    val last_name: String,
//    val password: String,
//    val email: String,
//    val phone: String,
//    val image: String?,  // Changed from String to ImageData blob
//    val PATIENT: PatientData
//)
//
//data class ImageData(
//    val type: String,
//    val data: List<Int>
//)
//
//data class PatientData(
//    val patient_id: Int,
//    val date_birthday: String,
//    val sexe: String,
//    val user_id: Int
//)

package com.example.projecttdm.data.model

data class UserProfileResponse(
    val user_id: Int,
    val role: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val email: String,
    val phone: String,
    val image: ImageBlob?,  // Changed from String? to ImageBlob?
    val PATIENT: PatientData
)



data class PatientData(
    val patient_id: Int,
    val date_birthday: String,
    val sexe: String,
    val user_id: Int
)