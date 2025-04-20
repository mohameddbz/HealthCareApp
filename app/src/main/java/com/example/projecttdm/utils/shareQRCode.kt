package com.example.projecttdm.utils

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.projecttdm.data.model.Appointment
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun shareQRCode(context: android.content.Context, bitmap: Bitmap, appointment: Appointment) {
    try {
        // Save bitmap to cache directory
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "appointment_qr_${appointment.id}.png")

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        // Get content URI using FileProvider
        val fileUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        // Create share intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)

            // Create a formatted message with appointment details
            val doctorName = appointment.doctorId // Would use viewModel.getDoctorName but can't access it here
            val dateString = appointment.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val timeString = appointment.time.format(DateTimeFormatter.ofPattern("HH:mm"))

            val message = "My appointment details:\n" +
                    "Doctor: $doctorName\n" +
                    "Date: $dateString\n" +
                    "Time: $timeString\n" +
                    "Please scan the attached QR code at reception."

            putExtra(Intent.EXTRA_TEXT, message)
            type = "image/png"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // Start the sharing activity
        context.startActivity(Intent.createChooser(shareIntent, "Share Appointment QR Code"))

    } catch (e: Exception) {
        e.printStackTrace()
        // Show error toast or handle exception
    }
}