package com.example.projecttdm.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.utils.QRCodeGenerator
import com.example.projecttdm.viewmodel.AppointmentViewModel
import java.time.format.DateTimeFormatter
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import com.example.projecttdm.ui.patient.components.Qr.AppointmentQRDialog
import java.io.File
import java.io.FileOutputStream

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


