package com.example.projecttdm.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.model.QRCodeData
import org.json.JSONObject

class QrViewModel : ViewModel() {
    private val _qrData = mutableStateOf<QRCodeData?>(null)
    val qrData: State<QRCodeData?> = _qrData

    fun updateQrCode(rawValue: String) {
        try {
            val json = JSONObject(rawValue)
            val id = json.getString("id")
            val content = json.getString("content")
            val timestamp = json.getLong("timestamp")
            _qrData.value = QRCodeData(id, content, timestamp)
        } catch (e: Exception) {
            _qrData.value = null
        }
    }
}