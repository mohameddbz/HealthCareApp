package com.example.projecttdm.ui.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projecttdm.ui.notifications.NotificationsScreen
import com.example.projecttdm.ui.patient.components.BookAppointment.FailurePopup
import com.example.projecttdm.ui.patient.components.BookAppointment.SuccessPopup
import com.example.projecttdm.ui.patient.screens.AppointmentScreen
import com.example.projecttdm.ui.patient.screens.BookAppointmentScreen
import com.example.projecttdm.ui.patient.screens.DoctorProfileScreen
import com.example.projecttdm.ui.patient.screens.HomeScreen
import com.example.projecttdm.ui.patient.screens.PatientDetailsScreen
import com.example.projecttdm.ui.patient.screens.PinVerificationScreen
import com.example.projecttdm.ui.patient.screens.SearchScreen
import com.example.projecttdm.ui.patient.screens.TopDoctorScreen
import com.example.projecttdm.ui.patient.components.Qr.AppointmentQRDialog
import com.example.projecttdm.ui.screens.AppointmentQRScreen
import com.example.projecttdm.viewmodel.AppointmentViewModel
import com.example.projecttdm.viewmodel.DoctorListViewModel
import com.example.projecttdm.viewmodel.DoctorSearchViewModel
import com.example.projecttdm.viewmodel.BookAppointmentViewModel
import com.example.projecttdm.viewmodel.DoctorProfileViewModel
import com.example.projecttdm.viewmodel.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientNavigation(navController: NavHostController = rememberNavController(),  modifier: Modifier = Modifier) {
    val doctorSearchViewModel: DoctorSearchViewModel = viewModel()
    val appointmentViewModel: AppointmentViewModel = viewModel()
    val doctorListViewModel: DoctorListViewModel = viewModel(factory = viewModelFactory {
        initializer { DoctorListViewModel(doctorSearchViewModel) }
    })
    NavHost(
        navController = navController,
        startDestination = PatientRoutes.HomeScreen.route,
        modifier = modifier
    ) {
        composable(PatientRoutes.BookAppointment.route) {
            BookAppointmentScreen(
                onNextClicked = {
                    navController.navigate(PatientRoutes.PinVerification.route)
                },
                doctorId = "defaultDoctorId",
                patientId = "defaultPatientId",
                appointmentViewModel = BookAppointmentViewModel()
            )
        }

        composable(PatientRoutes.HomeScreen.route) {
            HomeScreen(doctorSearchViewModel,doctorListViewModel,navController)
                }

        composable(PatientRoutes.PatientDetails.route) {
            PatientDetailsScreen(
                onBackClicked = { navController.popBackStack() },  // Navigate back to the previous screen
                onNextClicked = { navController.navigate(PatientRoutes.topDoctors.route) }
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



        composable(PatientRoutes.Success.route) {
            SuccessPopup (
                onViewAppointment = {
                    navController.navigate(PatientRoutes.PatientDetails.route) {
                        popUpTo(0)
                    }
                },
                onCancel = {
                    navController.navigate(PatientRoutes.BookAppointment.route) {
                        popUpTo(0)
                    }
                }
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
            notificationViewModel.getNotifications()
            NotificationsScreen(notificationViewModel,navController)
        }

        composable(PatientRoutes.topDoctors.route) {
            TopDoctorScreen(
                onBackClick = {  },
                onDoctorClick = {  },
                onSearchClick = { navController.navigate(PatientRoutes.searchDoctor.route) }
            )
        }

        composable(PatientRoutes.searchDoctor.route) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                doctorSearchViewModel
            )
        }

        composable(PatientRoutes.Appointment.route) {
            AppointmentScreen(navController,appointmentViewModel,doctorListViewModel)
        }


        // For the AppointmentQR route, update the composable:
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

        composable(PatientRoutes.doctorProfile.route){
            val doctorProfileViewModel: DoctorProfileViewModel = viewModel()
            DoctorProfileScreen(
                viewModel = doctorProfileViewModel ,
                onBackClick = {navController.popBackStack()},
                navigateToAllReviews = {}
            )
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
        route = PatientRoutes.BookAppointment.route
    ),
    NavigationItem(
        title = "History",
        icon = Icons.Default.ShoppingCart,
        route = PatientRoutes.doctorProfile.route
    ),
    NavigationItem(
        title = "Articles",
        icon = Icons.Default.ShoppingCart,
        route = PatientRoutes.Appointment.route
    ),
    NavigationItem(
        title = "Profile",
        icon = Icons.Default.ShoppingCart,
        route = PatientRoutes.AppointmentQR.route
    ),
)

