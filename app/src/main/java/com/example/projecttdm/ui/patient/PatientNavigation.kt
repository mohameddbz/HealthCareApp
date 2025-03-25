package com.example.projecttdm.ui.patient

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PatientNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "patientHome") {
        composable("patientHome") {  }
        composable("appointment") {  }
    }
}
