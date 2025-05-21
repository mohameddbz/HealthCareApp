package com.example.projecttdm.ui.patient

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projecttdm.ui.patient.components.AnimatedBottomNavigationBar
import com.example.projecttdm.ui.notifications.NotificationsScreen
import com.example.projecttdm.ui.patient.components.BookAppointment.FailurePopup
import com.example.projecttdm.ui.patient.components.BookAppointment.SuccessPopup
import com.example.projecttdm.ui.patient.components.CancelDialog
import com.example.projecttdm.ui.patient.screens.AppointmentScreen
import com.example.projecttdm.ui.patient.screens.AppointmentReviewScreen
import com.example.projecttdm.ui.patient.screens.BookAppointmentScreen
import com.example.projecttdm.ui.patient.screens.CancelReasonScreen
import com.example.projecttdm.ui.patient.screens.DoctorProfileScreen
import com.example.projecttdm.ui.patient.screens.FavoriteDoctorsScreen
import com.example.projecttdm.ui.patient.screens.HomeScreen
import com.example.projecttdm.ui.patient.screens.PatientDetailsScreen
import com.example.projecttdm.ui.patient.screens.PinVerificationScreen
import com.example.projecttdm.ui.patient.screens.PrescriptionCreateScreen
import com.example.projecttdm.ui.patient.screens.PrescriptionScreen
import com.example.projecttdm.ui.patient.screens.PrescriptionScreenContent
import com.example.projecttdm.ui.patient.screens.RescheduleAppointmentScreen
import com.example.projecttdm.ui.patient.screens.RescheduleReasonScreen
import com.example.projecttdm.ui.patient.screens.SearchScreen
import com.example.projecttdm.ui.patient.screens.TopDoctorScreen
import com.example.projecttdm.ui.screens.AppointmentQRScreen
import com.example.projecttdm.viewmodel.AppointmentViewModel
import com.example.projecttdm.viewmodel.DoctorListViewModel
import com.example.projecttdm.viewmodel.BookAppointmentViewModel
import com.example.projecttdm.viewmodel.CancelReasonViewModel
import com.example.projecttdm.viewmodel.DoctorProfileViewModel
import com.example.projecttdm.viewmodel.FavoriteDoctorsViewModel
import com.example.projecttdm.viewmodel.HomeViewModel
import com.example.projecttdm.viewmodel.NotificationViewModel
import com.example.projecttdm.viewmodel.PrescriptionContentViewModel
import com.example.projecttdm.viewmodel.PrescriptionViewModel
import com.example.projecttdm.viewmodel.ReasonViewModel
import com.example.projecttdm.viewmodel.RescheduleAppointmentViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientNavigation(navController: NavHostController = rememberNavController(), modifier: Modifier = Modifier) {
    Scaffold(
        bottomBar = {
            AnimatedBottomNavigationBar(navController)
        }
    ) { innerPadding ->
        val appointmentViewModel: AppointmentViewModel = viewModel()
        val homeViewModel: HomeViewModel = viewModel()
        val doctorListViewModel: DoctorListViewModel = viewModel()
        val doctorProfileViewModel: DoctorProfileViewModel = viewModel()

        NavHost(
            navController = navController,
            startDestination = PatientRoutes.HomeScreen.route,
            modifier = modifier
        ) {
            // Navigation graph content (same as original)
            composable(PatientRoutes.RescheduleAppointment.route) {
                RescheduleAppointmentScreen(
                    appointmentId = "98",
                    onRescheduleSuccess = {navController.navigate(PatientRoutes.Success.route)},
                    viewModel = RescheduleAppointmentViewModel()
                )
            }

            composable(PatientRoutes.HomeScreen.route) {
                val favoriteViewModel : FavoriteDoctorsViewModel = viewModel()
                HomeScreen(
                    navController, homeViewModel,
                    onDoctorClick = { doctorId ->
                        navController.navigate("${PatientRoutes.doctorProfile.route}/$doctorId")
                    },
                    favoriteViewModel = favoriteViewModel,
                )
            }

            composable(PatientRoutes.RescheduleReason.route) {
                val reasonViewModel: ReasonViewModel = viewModel(factory = ReasonViewModel.Factory(LocalContext.current))
                RescheduleReasonScreen(
                    viewModel = reasonViewModel,
                    onNavigateBack = { },
                    onNext = {
                        navController.navigate(PatientRoutes.RescheduleAppointment.route)
                    }
                )
            }

            composable(PatientRoutes.PinVerification.route) {
                PinVerificationScreen(
                    onSuccess = {
                        navController.navigate(PatientRoutes.Success.route) {
                            popUpTo(PatientRoutes.PinVerification.route) { inclusive = true }
                        }
                    },
                    onFailure = {
                        navController.navigate(PatientRoutes.Failure.route) {
                            popUpTo(PatientRoutes.PinVerification.route) { inclusive = true }
                        }
                    },
                    onBackClicked = {
                        navController.navigate(PatientRoutes.Success.route) {
                            popUpTo(PatientRoutes.PinVerification.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(PatientRoutes.FavoriteDoctors.route) {
                FavoriteDoctorsScreen(
                    onBackClick = { navController.popBackStack() },
                )
            }

            composable(PatientRoutes.Success.route) {
                SuccessPopup(
                    titleText = "Congratulations!",
                    messageText = "Appointment successfully booked.\nYou will receive a notification and the\ndoctor you selected will contact you.",
                    buttonText = "View Appointment", // Primary button text
                    onPrimaryAction = {
                        navController.navigate(PatientRoutes.PatientSummary.route) {
                            popUpTo(0)
                        }
                    },
                    onDismiss = {
                        navController.navigate(PatientRoutes.Appointment.route) {
                            popUpTo(0)
                        }
                    },
                    showSecondaryButton = true, // Show the cancel button
                    secondaryButtonText = "Cancel" // Optional: override default "Cancel" text
                )
            }

            composable(PatientRoutes.SuccessCancel.route) {
                SuccessPopup(
                    titleText = "Cancel Appointment\nSuccess!",
                    messageText = "We are very sad that you have canceled your appointment. We will always improve our service to satisfy you in the next appointment.",
                    buttonText = "OK",
                    onPrimaryAction = { navController.navigate(PatientRoutes.Appointment.route) },
                    onDismiss = { /* dismiss */ },
                    showSecondaryButton = false // Hides the second button
                )
            }

            composable(PatientRoutes.Failure.route) {
                FailurePopup(
                    onTryAgain = {
                        navController.navigate(PatientRoutes.PinVerification.route) {
                            popUpTo(PatientRoutes.Failure.route) { inclusive = true }
                        }
                    },
                    onCancel = {
                        navController.navigate(PatientRoutes.BookAppointment.route) {
                            popUpTo(0)
                        }
                    }
                )
            }

        composable(PatientRoutes.NotificationScreen.route) {
            val notificationViewModel : NotificationViewModel = NotificationViewModel()
            notificationViewModel.loadNotifications()
            NotificationsScreen(notificationViewModel,navController)
        }

            composable(PatientRoutes.topDoctors.route) {
                TopDoctorScreen(
                    onBackClick = { navController.popBackStack() },
                    onDoctorClick = { doctorId ->
                        navController.navigate("${PatientRoutes.doctorProfile.route}/$doctorId")
                    },
                    onSearchClick = { navController.navigate(PatientRoutes.searchDoctor.route) },
                    doctorListViewModel = doctorListViewModel
                )
            }

            composable(PatientRoutes.searchDoctor.route) {
                SearchScreen(
                    onBackClick = { navController.popBackStack() },
                    doctorListViewModel
                )
            }

            composable(PatientRoutes.Appointment.route) {
                AppointmentScreen(navController, appointmentViewModel, doctorListViewModel)
            }

            composable(
                route = "${PatientRoutes.doctorProfile.route}/{doctorId}",
                arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
                DoctorProfileScreen(
                    viewModel = doctorProfileViewModel,
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    navigateToAllReviews = { },
                    onBookClick = { doctorId ->
                        navController.navigate("${PatientRoutes.BookAppointment.route}/$doctorId")
                    },
                )
            }

            composable(
                route = PatientRoutes.Prescription.routeWithArgs,
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
                route = "${PatientRoutes.BookAppointment.route}/{doctorId}",
                arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
                BookAppointmentScreen(
                    onNextClicked = { slotId ->
                        navController.navigate("${PatientRoutes.PatientDetails.route}/$slotId")
                    },
                    doctorId = doctorId,
                    patientId = "defaultPatientId",
                    appointmentViewModel = BookAppointmentViewModel()
                )
            }

            composable(
                route = "${PatientRoutes.PatientDetails.route}/{slotId}",
                arguments = listOf(navArgument("slotId") { type = NavType.StringType })
            ) { backStackEntry ->
                val slotId = backStackEntry.arguments?.getString("slotId") ?: ""
                PatientDetailsScreen(
                    slotId = slotId,
                    onBackClicked = { navController.popBackStack() },  // Navigate back to the previous screen
                    onNextClicked = { appointmentId ->
                        navController.navigate("${PatientRoutes.PatientSummary.route}/$appointmentId")
                    },
                )
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
                    viewModel = appointmentViewModel
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
                    patientId = patientId
                )
            }

            composable(
                route = "${PatientRoutes.CancelReason.route}/{appointmentid}",
                arguments = listOf(navArgument("appointmentid") { type = NavType.StringType })
            ) { backStackEntry ->
                val appointmentid = backStackEntry.arguments?.getString("appointmentid") ?: ""
                val reasonViewModel: CancelReasonViewModel = viewModel()
                CancelReasonScreen(
                    viewModel = reasonViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNext = { navController.navigate(PatientRoutes.SuccessCancel.route) },
                    appointmentId = appointmentid
                )
            }

            composable(
                route = "${PatientRoutes.CancelDialog.route}/{appointmentid}",
                arguments = listOf(navArgument("appointmentid") { type = NavType.StringType })
            ) { backStackEntry ->
                val appointmentid = backStackEntry.arguments?.getString("appointmentid") ?: ""
                CancelDialog(
                    onBackClick = { navController.popBackStack() },
                    onConfirmClick = { appointmentId ->
                        navController.navigate("${PatientRoutes.CancelReason.route}/$appointmentId")
                    },
                    onDismiss = { navController.popBackStack() },
                    appointmentid = appointmentid
                )
            }

            composable(
                route = "${PatientRoutes.AppointmentQR.route}/{appointmentid}",
                arguments = listOf(navArgument("appointmentid") { type = NavType.StringType })
            ) { backStackEntry ->
                val appointmentid = backStackEntry.arguments?.getString("appointmentid") ?: ""
                AppointmentQRScreen(
                    viewModel = appointmentViewModel,
                    appointmentId = appointmentid,
                    onDismiss = { navController.popBackStack() }
                )
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
        }
    }
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = PatientRoutes.HomeScreen.route
    ),
    NavigationItem(
        title = "Appointment",
        icon = Icons.Default.Person,
        route = PatientRoutes.Appointment.route
    ),
    NavigationItem(
        title = "Top Doctors",
        icon = Icons.Default.Star,
        route = PatientRoutes.topDoctors.route
    ),
    NavigationItem(
        title = "Notifications",
        icon = Icons.Default.Notifications,
        route = PatientRoutes.NotificationScreen.route
    ),
    NavigationItem(
        title = "Prescriptions",
        icon = Icons.Default.MedicalServices,
        route = PatientRoutes.topDoctors.route
    ),
)