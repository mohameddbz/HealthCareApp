package com.example.projecttdm.ui.patient

import AppointmentReviewScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projecttdm.ui.patient.components.FailureDialog
import com.example.projecttdm.ui.patient.components.SuccessDialog
import com.example.projecttdm.ui.patient.screens.BookAppointmentScreen



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "bookAppointment"
    ) {
        composable("bookAppointment") {
            BookAppointmentScreen(
                onNextClicked = {
                    navController.navigate("patientDetails")
                }
            )
        }

        composable("patientDetails") {
            PatientDetailsScreen(
                onNextClicked = {
                    navController.navigate("reviewSummary")
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable("reviewSummary") {
            AppointmentReviewScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onNextPressed = {
                    navController.navigate("pinVerification")
                }
            )
        }

        composable("pinVerification") {
            PinVerificationScreen(
                onBackClicked = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate("success") {
                        popUpTo("pinVerification") { inclusive = true }
                    }
                },
                onFailure = {
                    navController.navigate("failure") {
                        popUpTo("pinVerification") { inclusive = true }
                    }
                }
            )
        }

        composable("success") {
            SuccessDialog(
                onViewAppointment = {
                    // Change this to navigate back to the appointment review screen
                    // without creating another success dialog
                    navController.navigate("reviewSummary") {
                        // Clear backstack all the way to the beginning
                        popUpTo("bookAppointment")
                    }
                },
                onCancel = {
                    navController.navigate("bookAppointment") {
                        popUpTo(0)
                    }
                }
            )
        }

        composable("failure") {
            FailureDialog(
                onTryAgain = {
                    navController.navigate("pinVerification") {
                        popUpTo("failure") { inclusive = true }
                    }
                },
                onCancel = {
                    navController.navigate("bookAppointment") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}
