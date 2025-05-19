package com.example.projecttdm.ui.doctor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.doctorviewmodel.DoctorHomeViewModel
import com.example.projecttdm.ui.doctor.screens.DoctorHomeScreen
<<<<<<< Updated upstream
=======
import com.example.projecttdm.ui.doctor.screens.DoctorScheduleScreen
import com.example.projecttdm.ui.doctor.screens.QrScannerScreen
import com.example.projecttdm.ui.patient.PatientRoutes
import com.example.projecttdm.ui.patient.components.BookAppointment.SuccessPopup
import com.example.projecttdm.ui.patient.screens.AppointmentReviewScreen
import com.example.projecttdm.ui.patient.screens.PrescriptionCreateScreen
import com.example.projecttdm.viewmodel.AppointmentViewModel
import com.example.projecttdm.viewmodel.PrescriptionViewModel
import com.example.projecttdm.viewmodel.QrViewModel
>>>>>>> Stashed changes


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = DoctorRoutes.HomeScreen.route,
    ) {
        composable(DoctorRoutes.HomeScreen.route){
            val homeViewModel : DoctorHomeViewModel = viewModel();
           // DoctorHomePage(homeViewModel)
            DoctorHomeScreen(doctorHomeViewModel = homeViewModel , navController)
        }

<<<<<<< Updated upstream
    }
}
=======
    Scaffold (
        bottomBar = { BottomNavigationBar(navController) }
    ){   innerPadding ->

        val appointmentViewModel: AppointmentViewModel = viewModel()
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

            composable(DoctorRoutes.QrScanner.route) {
                val viewModel  : QrViewModel = viewModel();
                QrScannerScreen(viewModel,navController)
            }

            composable(
                route = "${PatientRoutes.PatientSummary.route}/{appointmentid}",
                arguments = listOf(navArgument("appointmentid") { type = NavType.StringType })
            ) { backStackEntry ->
                val appointmentid = backStackEntry.arguments?.getString("appointmentid") ?: ""
                AppointmentReviewScreen(
                    appointmentId = appointmentid,
                    navController = navController,
                    onBackPressed = { navController.popBackStack() },
                    onNextPressed = {
                        navController.navigate(PatientRoutes.PinVerification.route)
                    },
                    viewModel = appointmentViewModel,
                    canAddPrescription = true,
                    onAddPrescriptionClick = { appointId, patientId ->
                        // Example: navigate to a screen to add a prescription with these IDs
                        navController.navigate("${PatientRoutes.PrescriptionCreate.route}/${patientId}/${appointId}")
                    }
                )
            }



            composable(
                route = "${PatientRoutes.PrescriptionCreate.route}/{patientId}/{appointmentId}",
                arguments = listOf(
                    navArgument("patientId") { type = NavType.StringType },
                    navArgument("appointmentId") { type = NavType.StringType }
                )
            ){  backStackEntry ->
                val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                val prescriptionViewModel = PrescriptionViewModel()

                PrescriptionCreateScreen(
                    patientId = patientId,
                    doctorId = "1",
                    appointmentId = appointmentId,
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = prescriptionViewModel
                )
            }

            composable(
                route = "${DoctorRoutes.DOCTOR_SCHEDULE.route}/{doctorId}",
                arguments = listOf(
                    navArgument("doctorId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: -1
                DoctorScheduleScreen(
                    doctorId = doctorId,
                    onNavigateBack = { navController.popBackStack() },
                )
            }


            composable(DoctorRoutes.AppointmentOfWeek.route) {
            AppointmentOfWeekScreen(
                onBrowseDoctors = {
                    navController.navigate(DoctorRoutes.HomeScreen.route)
                },
                onAppointmentClick = { appointmentId ->
                    // Handle appointment click
                }
            )
        }}}}



>>>>>>> Stashed changes
