package com.example.projecttdm.utils

import android.content.Context
import android.net.Uri


fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        bytes
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}