package com.example.projecttdm.ui.doctor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.data.repository.RepositoryHolder
import com.example.projecttdm.doctorviewmodel.DoctorHomeViewModel
import com.example.projecttdm.ui.doctor.screens.AppointmentOfWeekScreen
import com.example.projecttdm.ui.doctor.screens.DoctorHomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DoctorRoutes.HomeScreen.route
    ) {
        composable(DoctorRoutes.HomeScreen.route) {
            val homeViewModel: DoctorHomeViewModel = viewModel()
            DoctorHomeScreen(
                doctorHomeViewModel = homeViewModel,
                navController = navController
            )
        }

        composable("${DoctorRoutes.AppointmentOfWeek.route}/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: "1"  // Default to "1"
            AppointmentOfWeekScreen(
                doctorId = doctorId,
                onBrowseDoctors = {
                    navController.navigate(DoctorRoutes.HomeScreen.route)
                },
                onAppointmentClick = { appointmentId ->
                    // Handle appointment click
                }
            )
        }
    }
}
