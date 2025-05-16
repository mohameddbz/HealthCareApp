package com.example.projecttdm.utils

import com.example.projecttdm.data.model.QRCodeData
import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.gson.Gson

@OptIn(ExperimentalGetImage::class)
class QRCodeAnalyzer(
    private val onDataParsed: (QRCodeData) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()
    private val gson = Gson()

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage: Image = imageProxy.image ?: return

        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val rawJson = barcode.rawValue ?: continue
                    try {
                        val data = gson.fromJson(rawJson, QRCodeData::class.java)
                        onDataParsed(data)
                        break // Stop after first valid QR
                    } catch (e: Exception) {
                        Log.e("QRCodeAnalyzer", "Invalid JSON format", e)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("QRCodeAnalyzer", "QR scan failed", it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
