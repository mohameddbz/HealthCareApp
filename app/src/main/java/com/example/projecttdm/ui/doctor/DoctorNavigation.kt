package com.example.projecttdm.ui.doctor

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projecttdm.R
import com.example.projecttdm.doctorviewmodel.DoctorHomeViewModel
import com.example.projecttdm.ui.components.AnimatedBottomNavigationBar
import com.example.projecttdm.ui.components.BottomNavItem
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

    // Define doctor navigation items
    val doctorNavItems = listOf(
        BottomNavItem(
            title = "Home",
            route = DoctorRoutes.HomeScreen.route,
            painter = painterResource(id = R.drawable.d_home)
        ),
        BottomNavItem(
            title = "Tasks",
            route = DoctorRoutes.AppointmentValidationScreen.route,
            painter = painterResource(id = R.drawable.d_tasks)
        ),
        BottomNavItem(
            title = "Camera",
            route = DoctorRoutes.QrScanner.route,
            painter = painterResource(id = R.drawable.d_camera)
        ),
        BottomNavItem(
            title = "Week",
            route = DoctorRoutes.AppointmentOfWeek.route,
            painter = painterResource(id = R.drawable.d_account)
        )
    )

    // Define selected item provider for doctor routes
    val selectedItemProvider: (String?) -> Int = { route ->
        when {
            route == DoctorRoutes.HomeScreen.route ||
                    route?.startsWith(DoctorRoutes.HomeScreen.route) == true -> 0

            route == DoctorRoutes.AppointmentValidationScreen.route ||
                    route?.startsWith(DoctorRoutes.AppointmentValidationScreen.route) == true ||
                    route?.contains("SuccessConfirm") == true ||
                    route?.contains("SuccesRefuse") == true -> 1

            route == DoctorRoutes.QrScanner.route ||
                    route?.startsWith(DoctorRoutes.QrScanner.route) == true ||
                    route?.contains("Prescription") == true ||
                    route?.contains("PatientSummary") == true -> 2

            route == DoctorRoutes.AppointmentOfWeek.route ||
                    route?.startsWith(DoctorRoutes.AppointmentOfWeek.route) == true ||
                    route?.contains("DOCTOR_SCHEDULE") == true -> 3

            else -> 0 // Default to Home
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedBottomNavigationBar(
                navController = navController,
                items = doctorNavItems,
                selectedItemProvider = selectedItemProvider
            )
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
                    onPrimaryAction = {
                        navController.navigate(DoctorRoutes.AppointmentValidationScreen.route) {
                            popUpTo(DoctorRoutes.AppointmentValidationScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    onDismiss = { navController.popBackStack() },
                    showSecondaryButton = false
                )
            }

            composable(DoctorRoutes.SuccesRefuse.route) {
                SuccessPopup(
                    titleText = "Refuse Appointment\nSuccess!",
                    messageText = "The appointment has been refused. A notification will be sent to the patient",
                    buttonText = "OK",
                    onPrimaryAction = {
                        navController.navigate(DoctorRoutes.AppointmentValidationScreen.route) {
                            popUpTo(DoctorRoutes.AppointmentValidationScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    onDismiss = { navController.popBackStack() },
                    showSecondaryButton = false
                )
            }

            composable(DoctorRoutes.QrScanner.route) {
                val viewModel: QrViewModel = viewModel()
                QrScannerScreen(viewModel, navController)
            }

            composable(
                route = "${PatientRoutes.Prescription.route}/{prescriptionId}",
                arguments = listOf(navArgument("prescriptionId") { type = NavType.StringType })
            ) { backStackEntry ->
                val prescriptionId = backStackEntry.arguments?.getString("prescriptionId")
                val prescriptionViewModel: PrescriptionContentViewModel = viewModel()

                PrescriptionScreenContent(
                    prescriptionId = prescriptionId,
                    viewModel = prescriptionViewModel,
                )
            }

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
                        navController.navigate("${PatientRoutes.PrescriptionList.route}/$appointId/$patientId")
                    }
                )
            }

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
                    doctorId = "1", // You may want to make this dynamic
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