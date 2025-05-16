package com.example.projecttdm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.model.QRCodeData
import com.google.gson.Gson


class QrViewModel : ViewModel() {
    private val _qrData = MutableLiveData<QRCodeData?>()
    val qrData: LiveData<QRCodeData?> = _qrData

    fun updateQrCode(json: String) {
        try {
            val data = Gson().fromJson(json, QRCodeData::class.java)
            _qrData.value = data
        } catch (e: Exception) {
            Log.e("QR_PARSE", "Failed to parse QR JSON", e)
        }
    }

    fun clearQrData() {
        _qrData.value = null
    }

}
