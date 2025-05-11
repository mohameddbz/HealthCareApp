package com.example.projecttdm.ui.patient.components.Qr

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.projecttdm.viewmodel.AppointmentViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentQRDialog(
    viewModel: AppointmentViewModel,
    onDismiss: () -> Unit
) {
    val appointment by viewModel.selectedAppointment.collectAsState()
    val qrCodeData by viewModel.qrCodeData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                appointment == null || qrCodeData == null -> {
                    Text(
                        text = "Error loading appointment details",
                        modifier = Modifier.padding(24.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    AppointmentQRContent(
                        appointment = appointment!!,
                        qrCodeContent = qrCodeData!!.content,
                        viewModel = viewModel,
                        onCancelClick = {
                            //viewModel.cancelAppointment(appointment!!.id)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}
