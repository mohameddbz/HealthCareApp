package com.example.projecttdm.ui.doctor

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.ui.auth.AuthRoutes
import com.example.projecttdm.ui.doctor.screens.TopDoctorScreen
import com.example.projecttdm.viewmodel.DoctorViewModel

@Composable
fun DoctorNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = DoctorRoutes.topDoctors.route) {
        composable(DoctorRoutes.topDoctors.route) {
            TopDoctorScreen(
                onBackClick = {  },
                onDoctorClick = {  }
            )
        }
    }
}