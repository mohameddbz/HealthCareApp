package com.example.projecttdm.ui.doctor

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projecttdm.doctorviewmodel.DoctorHomeViewModel
import com.example.projecttdm.ui.doctor.components.AnimatedBottomNavigationBar
import com.example.projecttdm.ui.doctor.screens.CalendarApp
import com.example.projecttdm.ui.doctor.screens.AppointmentOfWeekScreen
import com.example.projecttdm.ui.doctor.screens.DoctorHomeScreen
import com.example.projecttdm.ui.doctor.screens.DoctorScheduleScreen
import com.example.projecttdm.ui.doctor.screens.QrScannerScreen
import com.example.projecttdm.ui.patient.PatientRoutes
import com.example.projecttdm.ui.patient.components.BookAppointment.SuccessPopup
import com.example.projecttdm.ui.patient.screens.AppointmentReviewScreen
import com.example.projecttdm.ui.patient.screens.PrescriptionCreateScreen
import com.example.projecttdm.ui.patient.screens.PrescriptionScreen
import com.example.projecttdm.ui.patient.screens.PrescriptionScreenContent
import com.example.projecttdm.viewmodel.AppointmentViewModel
import com.example.projecttdm.viewmodel.PrescriptionContentViewModel
import com.example.projecttdm.viewmodel.PrescriptionViewModel
import com.example.projecttdm.viewmodel.QrViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorNavigation(navController: NavHostController = rememberNavController()) {

    Scaffold(
        bottomBar = {
            AnimatedBottomNavigationBar(navController)
        }
    ) { innerPadding ->

        val appointmentViewModel: AppointmentViewModel = viewModel()
        NavHost(
            navController = navController,
            startDestination = DoctorRoutes.HomeScreen.route,
        ) {
            composable(DoctorRoutes.HomeScreen.route) {
                val homeViewModel: DoctorHomeViewModel = viewModel()
                DoctorHomeScreen(doctorHomeViewModel = homeViewModel, navController)
            }

            composable(DoctorRoutes.AppointmentValidationScreen.route) {
                CalendarApp(
                    onBackPressed = { navController.popBackStack() },
                    onNext = { navController.navigate(DoctorRoutes.SuccessConfirm.route) },
                    onRefuse = { navController.navigate(DoctorRoutes.SuccesRefuse.route) }
                )
            }

            composable(DoctorRoutes.SuccessConfirm.route) {
                SuccessPopup(
                    titleText = "Confirm Appointment\nSuccess!",
                    messageText = "The appointment has been confirmed. A notification will be sent to the patient",
                    buttonText = "OK",
                    onPrimaryAction = { navController.popBackStack() },
                    onDismiss = { /* dismiss */ },
                    showSecondaryButton = false
                )
            }

            composable(DoctorRoutes.SuccesRefuse.route) {
                SuccessPopup(
                    titleText = "Refuse Appointment\nSuccess!",
                    messageText = "The appointment has been refused. A notification will be sent to the patient",
                    buttonText = "OK",
                    onPrimaryAction = { navController.popBackStack() },
                    onDismiss = { /* dismiss */ },
                    showSecondaryButton = false
                )
            }

            composable(DoctorRoutes.QrScanner.route) {
                val viewModel: QrViewModel = viewModel()
                QrScannerScreen(viewModel, navController)
            }

            composable(
                route = "${PatientRoutes.Prescription.route}/{prescriptionId}",
                arguments = listOf(navArgument("prescriptionId") { type = NavType.StringType }))
            {
                    backStackEntry ->
                val prescriptionId = backStackEntry.arguments?.getString("prescriptionId")
                val prescriptionViewModel: PrescriptionContentViewModel = viewModel()

                PrescriptionScreenContent(
                    prescriptionId = prescriptionId,
                    viewModel = prescriptionViewModel,
                )
            }

            // Fix: Using consistent path parameter names
            composable(
                route = "${PatientRoutes.PatientSummary.route}/{appointmentId}",
                arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                AppointmentReviewScreen(
                    appointmentId = appointmentId,
                    navController = navController,
                    onBackPressed = { navController.popBackStack() },
                    onNextPressed = {
                        navController.navigate(PatientRoutes.PinVerification.route)
                    },
                    viewModel = appointmentViewModel,
                    canAddPrescription = true,
                    onAddPrescriptionClick = { appointId, patientId ->
                        // Fix: Ensure consistent parameter order
                        navController.navigate("${PatientRoutes.PrescriptionList.route}/$appointId/$patientId")
                    }
                )
            }

            // Fix: Ensuring parameter order matches what components expect
            composable(
                route = "${PatientRoutes.PrescriptionList.route}/{appointmentId}/{patientId}",
                arguments = listOf(
                    navArgument("appointmentId") { type = NavType.StringType },
                    navArgument("patientId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                val viewModel: PrescriptionViewModel = viewModel()
                PrescriptionScreen(
                    oncickAdd = { appointId, patientId ->
                        // Fix: Navigate with consistent parameter order
                        navController.navigate("${PatientRoutes.PrescriptionCreate.route}/$appointId/$patientId")
                    },
                    onclick = { prescriptionId ->
                        navController.navigate("${PatientRoutes.Prescription.route}/$prescriptionId")
                    },
                    viewModel = viewModel,
                    appointId = appointmentId,
                    patientId = patientId,
                    showAddButton = true
                )
            }

            // Fix: Consistent parameter order
            composable(
                route = "${PatientRoutes.PrescriptionCreate.route}/{appointmentId}/{patientId}",
                arguments = listOf(
                    navArgument("appointmentId") { type = NavType.StringType },
                    navArgument("patientId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                val prescriptionViewModel = PrescriptionViewModel()

                PrescriptionCreateScreen(
                    patientId = patientId,
                    doctorId = "1", // Still hardcoded but that's fine for now
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
                        navController.navigate("${PatientRoutes.PatientSummary.route}/$appointmentId")
                    }
                )
            }
        }
    }
}