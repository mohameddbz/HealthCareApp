package com.example.projecttdm.ui.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions

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
                    navController.navigate("pinVerification")
                }
            )
        }

        composable("patientDetails") {
            PatientDetailsScreen(
                onNextClicked = { /* Handle next action */ }
            )
        }

        composable("pinVerification") {
            PinVerificationFlow { isSuccess ->
                if (isSuccess) {
                    navController.navigate("success") {
                        popUpTo("pinVerification") { inclusive = true }
                    }
                } else {
                    navController.navigate("failure") {
                        popUpTo("pinVerification") { inclusive = true }
                    }
                }
            }
        }

        composable("success") {
            SuccessPopup (
                onViewAppointment = {
                    navController.navigate("patientDetails") {
                        // Clear entire back stack
                        popUpTo(0)
                    }
                },
                onCancel = {
                    navController.navigate("bookAppointment") {
                        // Clear entire back stack
                        popUpTo(0)
                    }
                }
            )
        }

        composable("failure") {
            FailurePopup(
                onTryAgain = {
                    navController.navigate("pinVerification") {
                        // Clear back stack to avoid multiple failure screens
                        popUpTo("failure") { inclusive = true }
                    }
                },
                onCancel = {
                    navController.navigate("bookAppointment") {
                        // Clear back stack
                        popUpTo(0)
                    }
                }
            )
        }
    }
}