package com.example.projecttdm.ui.doctor

sealed class DoctorRoutes  (val route : String){

    object topDoctors : DoctorRoutes("doctor_top_doctors")

}