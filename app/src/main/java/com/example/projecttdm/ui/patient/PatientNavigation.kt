package com.example.projecttdm.ui.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.ui.notifications.NotificationsScreen
import com.example.projecttdm.ui.patient.components.Appointment.FailurePopup
import com.example.projecttdm.ui.patient.components.Appointment.SuccessPopup
import com.example.projecttdm.ui.patient.screens.AppointmentReview
import com.example.projecttdm.ui.patient.screens.AppointmentReviewScreen
import com.example.projecttdm.ui.patient.screens.BookAppointmentScreen
import com.example.projecttdm.ui.patient.screens.PatientDetailsScreen
import com.example.projecttdm.ui.patient.screens.PinVerificationScreen
import com.example.projecttdm.ui.patient.screens.SearchScreen
import com.example.projecttdm.ui.patient.screens.TopDoctorScreen
import com.example.projecttdm.viewmodel.BookAppointmentViewModel
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
                    navController.navigate(PatientRoutes.PatientDetails.route)
                },
                doctorId = "defaultDoctorId",
                patientId = "defaultPatientId",
                appointmentViewModel = BookAppointmentViewModel()
            )
        }

        composable(PatientRoutes.PatientDetails.route) {
            PatientDetailsScreen(
                onBackClicked = { navController.popBackStack() },  // Navigate back to the previous screen
                onNextClicked = { navController.navigate(PatientRoutes.PatientSummary.route) }
            )
        }
        composable(PatientRoutes.PatientSummary.route) {
            AppointmentReviewScreen(
                navController = navController,
                onBackPressed = { navController.popBackStack() },
                onNextPressed = {
                    navController.navigate(PatientRoutes.PinVerification.route)
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
            NotificationsScreen(
                viewModel = notificationViewModel,
                onBackPressed = TODO(),
                onMoreClick = TODO()
            )
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
