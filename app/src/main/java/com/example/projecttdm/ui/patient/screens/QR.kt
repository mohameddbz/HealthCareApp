package com.example.projecttdm.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.projecttdm.data.model.QRCodeData
import com.example.projecttdm.ui.patient.components.Qr.AppointmentQRDialog
import com.example.projecttdm.viewmodel.AppointmentViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentQRScreen(
    viewModel: AppointmentViewModel,
    appointmentId: String? = null,
    onDismiss: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var qrCodeData by remember { mutableStateOf<QRCodeData?>(null) }
    var showError by remember { mutableStateOf(false) }

    // Observe LiveData from ViewModel
    val qrCodeResult by viewModel.qrCodeData.observeAsState()

    // Load the QR code when the screen appears
    LaunchedEffect(appointmentId) {
        if (appointmentId != null) {
            viewModel.fetchQRCode(appointmentId)
        }
    }

    // Process result
    LaunchedEffect(qrCodeResult) {
        qrCodeResult?.let { result ->
            isLoading = false
            result.onSuccess {
                qrCodeData = it
            }.onFailure {
                showError = true
                Log.e("AppointmentQRScreen", "Error fetching QR code", it)
            }
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        qrCodeData != null -> {
            AppointmentQRDialog(
                qrCodeData = qrCodeData!!,
                onDismiss = onDismiss,
                isLoading = isLoading
            )
        }

        showError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erreur lors du chargement du QR Code.")
            }
        }
    }
}
