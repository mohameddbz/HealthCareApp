//package com.example.projecttdm.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.projecttdm.data.model.QRCodeData
//import com.google.gson.Gson
//
//
//class QrViewModel : ViewModel() {
//    private val _qrData = MutableLiveData<QRCodeData?>()
//    val qrData: LiveData<QRCodeData?> = _qrData
//
//    fun updateQrCode(json: String) {
//        try {
//            val data = Gson().fromJson(json, QRCodeData::class.java)
//            _qrData.value = data
//        } catch (e: Exception) {
//            Log.e("QR_PARSE", "Failed to parse QR JSON", e)
//        }
//    }
//
//    fun clearQrData() {
//        _qrData.value = null
//    }
//
//}

package com.example.projecttdm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projecttdm.data.model.QRCodeData
import org.json.JSONObject


class QrViewModel : ViewModel() {
    private val _qrData = MutableLiveData<QRCodeData?>()
    val qrData: LiveData<QRCodeData?> = _qrData

    fun updateQrCode(json: String) {
        try {
            Log.d("QR_PARSE", "Attempting to parse JSON: $json")

            val jsonObject = JSONObject(json)

            val id = jsonObject.optString("appointmentId", "") // fallback to "" if missing
            val content = jsonObject.optString("content", "")
            val timestamp = jsonObject.optLong("timestamp", 0L)

            val data = QRCodeData(id, content, timestamp, null) // image = null

            Log.d("QR_PARSE", "Parsed QR data: id=${data.id}, content=${data.content}, timestamp=${data.timestamp}")
            _qrData.value = data
        } catch (e: Exception) {
            Log.e("QR_PARSE", "Failed to parse QR JSON: $json", e)
        }
    }

    fun clearQrData() {
        _qrData.value = null
    }
}