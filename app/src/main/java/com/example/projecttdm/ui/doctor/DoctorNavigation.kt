package com.example.projecttdm.ui.doctor

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun DoctorNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "doctorHome") {
        composable("doctorHome") {  }
        composable("patientList") { }
    }
}
