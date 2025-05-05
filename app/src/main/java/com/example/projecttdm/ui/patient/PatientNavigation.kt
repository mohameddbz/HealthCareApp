package com.example.projecttdm.ui.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.ReviewRepository
import com.example.projecttdm.data.repository.SpecialtyRepository
import com.example.projecttdm.ui.notifications.NotificationsScreen
import com.example.projecttdm.ui.patient.components.Appointment.AppointmentDetailsScreen
import com.example.projecttdm.ui.patient.components.BookAppointment.FailurePopup
import com.example.projecttdm.ui.patient.components.BookAppointment.SuccessPopup
import com.example.projecttdm.ui.patient.screens.*
import com.example.projecttdm.ui.screens.AppointmentQRScreen
import com.example.projecttdm.viewmodel.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientNavigation(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val appointmentViewModel: AppointmentViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val prescriptionViewModel: PrescriptionViewModel = viewModel()

    val doctorRepository = DoctorRepository()
    val reviewRepository = ReviewRepository()
    val specialtyRepository = SpecialtyRepository()

    val doctorListViewModel: DoctorListViewModel = viewModel(factory = viewModelFactory {
        initializer {
            DoctorListViewModel(
                doctorRepository = doctorRepository,
                reviewRepository = reviewRepository,
                specialtyRepository = specialtyRepository
            )
        }
    })

    NavHost(
        navController = navController,
        startDestination = PatientRoutes.HomeScreen.route,
        modifier = modifier
    ) {
        composable(PatientRoutes.HomeScreen.route) {
            HomeScreen(doctorListViewModel, navController,homeViewModel,
                onDoctorClick = { doctorId ->
                navController.navigate("${PatientRoutes.doctorProfile.route}/$doctorId")
            }, appintementViewModel = appointmentViewModel
            )
        }

        composable(PatientRoutes.BookAppointment.route) {
            val bookAppointmentViewModel: BookAppointmentViewModel = viewModel()
            BookAppointmentScreen(
                onNextClicked = { navController.navigate(PatientRoutes.PinVerification.route) },
                doctorId = "defaultDoctorId", // TODO: Replace with dynamic doctorId
                patientId = "defaultPatientId", // TODO: Replace with dynamic patientId
                appointmentViewModel = bookAppointmentViewModel
            )
        }

        composable(PatientRoutes.PatientDetails.route) {
            PatientDetailsScreen(
                onBackClicked = { navController.popBackStack() },
                onNextClicked = { navController.navigate(PatientRoutes.PatientSummary.route) }
            )
        }

        composable(PatientRoutes.PatientSummary.route) {
            AppointmentReviewScreen(
                navController = navController,
                onBackPressed = { navController.popBackStack() },
                onNextPressed = { navController.navigate(PatientRoutes.PinVerification.route) }
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
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(PatientRoutes.Success.route) {
            SuccessPopup(
                titleText = "Congratulations!",
                messageText = "Appointment successfully booked.\nYou will receive a notification.",
                buttonText = "View Appointment",
                onPrimaryAction = {
                    navController.navigate(PatientRoutes.Appointment.route) {
                        popUpTo(0)
                    }
                },
                onDismiss = {
                    navController.navigate(PatientRoutes.HomeScreen.route) {
                        popUpTo(0)
                    }
                },
                showSecondaryButton = true,
                secondaryButtonText = "Cancel"
            )
        }

        composable(
            route = "appointmentDetails/{appointmentId}",
            arguments = listOf(
                navArgument("appointmentId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
            AppointmentDetailsScreen(
                viewModel = appointmentViewModel,
                appointmentId = appointmentId,
                navController = navController
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
                    navController.navigate(PatientRoutes.HomeScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(PatientRoutes.NotificationScreen.route) {
            val notificationViewModel: NotificationViewModel = viewModel()
            notificationViewModel.getNotifications()
            NotificationsScreen(notificationViewModel, navController)
        }

        composable(PatientRoutes.topDoctors.route) {
            TopDoctorScreen(
                onBackClick = { navController.popBackStack() },
                onDoctorClick = { doctorId ->
                    navController.navigate("${PatientRoutes.doctorProfile.route}/$doctorId")
                },
                onSearchClick = { navController.navigate(PatientRoutes.searchDoctor.route) },
            )
        }

        composable(PatientRoutes.searchDoctor.route) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(PatientRoutes.Appointment.route) {
            AppointmentScreen(
                navController = navController,
                viewModel = appointmentViewModel,
                doctorViewModel = doctorListViewModel,
                homeViewModel = homeViewModel
            )
        }

        composable(
            route = PatientRoutes.AppQR.routeWithArgs,
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId")
            AppointmentQRScreen(
                viewModel = appointmentViewModel,
                appointmentId = appointmentId,
                onDismiss = { navController.popBackStack() }
            )
        }

        composable(
            route = "${PatientRoutes.doctorProfile.route}/{doctorId}",
            arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            DoctorProfileScreen(
                viewModel = doctorListViewModel,
                doctorId = doctorId,
                onBackClick = { navController.popBackStack() },
                navigateToAllReviews = { }
            )
        }

        composable(PatientRoutes.Prescription.route) {
            PrescriptionScreenContent(viewModel = prescriptionViewModel)
        }

        // TODO: Add more routes like rescheduling, canceling appointments etc.
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
        route = PatientRoutes.Prescription.route
    ),
)