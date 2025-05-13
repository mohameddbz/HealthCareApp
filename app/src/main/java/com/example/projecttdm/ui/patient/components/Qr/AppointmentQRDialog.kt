package com.example.projecttdm.ui.patient.components.Qr

import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.QRCodeData
import com.example.projecttdm.data.model.appointment2
import com.example.projecttdm.viewmodel.AppointmentViewModel
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentQRDialog(
    qrCodeData: QRCodeData,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {
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
                else -> {
                    val imageData = qrCodeData.image
                    if (imageData != null) {
                        val imageBytes = Base64.decode(imageData.split(",")[1], Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        val imageBitmap = bitmap.asImageBitmap()

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Votre QR Code",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = "QR Code",
                                modifier = Modifier
                                    .size(300.dp)
                                    .padding(bottom = 16.dp)
                            )
                            Button(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Fermer")
                            }
                        }
                    }
                }

            }
        }
    }
}
