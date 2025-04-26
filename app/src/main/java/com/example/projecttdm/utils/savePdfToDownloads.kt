package com.example.projecttdm.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

fun savePdfToDownloads(context: Context, sourceUri: Uri, fileName: String = "ordonnance.pdf") {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val destFile = File(downloadsDir, fileName)

    val inputStream = context.contentResolver.openInputStream(sourceUri)
    val outputStream = FileOutputStream(destFile)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    Toast.makeText(context, "Ordonnance enregistrée dans Téléchargements", Toast.LENGTH_SHORT).show()
}
