package com.example.projecttdm.ui.patient

import NotificationsScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.ui.patient.screens.SearchScreen
import com.example.projecttdm.ui.patient.screens.TopDoctorScreen
import com.example.projecttdm.viewmodel.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = PatientRoutes.BookAppointment.route
    ) {
        composable(PatientRoutes.BookAppointment.route) {
            BookAppointmentScreen(
                onNextClicked = {
                    navController.navigate(PatientRoutes.PinVerification.route)
                }
            )
        }

        composable(PatientRoutes.PatientDetails.route) {
            PatientDetailsScreen(
                onNextClicked = { navController.navigate(PatientRoutes.topDoctors.route)  }
            )
        }

        composable(PatientRoutes.PinVerification.route) {
            PinVerificationFlow { isSuccess ->
                if (isSuccess) {
                    navController.navigate(PatientRoutes.Success.route) {
                        popUpTo(PatientRoutes.PinVerification.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(PatientRoutes.Failure.route) {
                        popUpTo(PatientRoutes.PinVerification.route) { inclusive = true }
                    }
                }
            }
        }

        composable(PatientRoutes.Success.route) {
            SuccessPopup(
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
            NotificationsScreen(notificationViewModel)
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
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
