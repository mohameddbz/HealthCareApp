package com.example.projecttdm.ui.doctor

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.doctorviewmodel.DoctorHomeViewModel
import com.example.projecttdm.ui.doctor.components.BottomNavigationBar
import com.example.projecttdm.ui.doctor.screens.CalendarApp
import com.example.projecttdm.ui.doctor.screens.CalendarScreen
import com.example.projecttdm.ui.doctor.screens.AppointmentOfWeekScreen
import com.example.projecttdm.ui.doctor.screens.DoctorHomeScreen
import com.example.projecttdm.ui.patient.components.BookAppointment.SuccessPopup


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorNavigation(navController: NavHostController = rememberNavController()) {

    Scaffold (
        bottomBar = { BottomNavigationBar(navController) }
    ){   innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DoctorRoutes.HomeScreen.route,
        ) {
            composable(DoctorRoutes.HomeScreen.route){
                val homeViewModel : DoctorHomeViewModel = viewModel();
                // DoctorHomePage(homeViewModel)
                DoctorHomeScreen(doctorHomeViewModel = homeViewModel , navController)
            }
            composable(DoctorRoutes.AppointmentValidationScreen.route){
                CalendarApp(
                    onBackPressed = {navController.popBackStack()},
                    onNext = {navController.navigate(DoctorRoutes.SuccessConfirm.route)},
                    onRefuse = {navController.navigate(DoctorRoutes.SuccesRefuse.route)}

                )
            }


            composable(DoctorRoutes.SuccessConfirm.route) {
                SuccessPopup(
                    titleText = "Confirm Appointment\nSuccess!",
                    messageText = "The appointment has been confirmed. A notification will be sent to the patient",
                    buttonText = "OK",
                    onPrimaryAction = { navController.popBackStack() },
                    onDismiss = { /* dismiss */ },
                    showSecondaryButton = false // 👈 hides the second button
                )
            }
            composable(DoctorRoutes.SuccesRefuse.route) {
                SuccessPopup(
                    titleText = "Refuse Appointment\nSuccess!",
                    messageText = "The appointment has been refused. A notification will be sent to the patient",
                    buttonText = "OK",
                    onPrimaryAction = { navController.popBackStack() },
                    onDismiss = { /* dismiss */ },
                    showSecondaryButton = false // 👈 hides the second button
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
        }}}}



