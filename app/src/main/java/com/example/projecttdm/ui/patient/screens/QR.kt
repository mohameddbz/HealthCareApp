package com.example.projecttdm.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.projecttdm.viewmodel.AppointmentViewModel
import com.example.projecttdm.ui.patient.components.Qr.AppointmentQRDialog


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentQRScreen(
    viewModel: AppointmentViewModel,
    appointmentId: String? = null,
    onDismiss: () -> Unit
) {
    // Load appointment data when the screen is launched
    LaunchedEffect(Unit) {
        if (appointmentId != null) {
            viewModel.showAppointmentQRCode(appointmentId)
        } else {
            viewModel.loadLatestAppointment()
        }
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val showDialog by viewModel.showQRCodeDialog.collectAsState()

    if (showDialog) {
        AppointmentQRDialog(viewModel = viewModel, onDismiss = onDismiss)
    } else if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}


