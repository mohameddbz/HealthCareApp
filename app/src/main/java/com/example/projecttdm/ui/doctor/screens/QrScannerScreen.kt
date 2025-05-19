//package com.example.projecttdm.ui.doctor.screens
//
//import android.content.pm.PackageManager
//import android.util.Log
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.OptIn
//import androidx.camera.core.*
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Button
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.*
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import androidx.navigation.NavController
//import com.example.projecttdm.ui.patient.PatientRoutes
//import com.example.projecttdm.viewmodel.QrViewModel
//import com.google.mlkit.vision.barcode.BarcodeScanning
//import com.google.mlkit.vision.common.InputImage
//
//
//@OptIn(ExperimentalGetImage::class)
//@Composable
//fun QrScannerScreen(
//    viewModel: QrViewModel,
//    navController: NavController,
//) {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val qrData by viewModel.qrData.observeAsState()
//
//    var hasPermission by remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(
//                context, android.Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//        )
//    }
//
//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { granted -> hasPermission = granted }
//
//    LaunchedEffect(Unit) {
//        if (!hasPermission) {
//            launcher.launch(android.Manifest.permission.CAMERA)
//        }
//    }
//
//    LaunchedEffect(qrData) {
//        println("---------------------${qrData}")
//        qrData?.id?.let { id ->
//            navController.navigate("${PatientRoutes.PatientSummary.route}/$id")
//            viewModel.clearQrData()
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        if (hasPermission && qrData == null) {
//            AndroidView(
//                factory = { ctx ->
//                    val previewView = PreviewView(ctx)
//                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
//                    val scanner = BarcodeScanning.getClient()
//
//                    cameraProviderFuture.addListener({
//                        val cameraProvider = cameraProviderFuture.get()
//
//                        val preview = Preview.Builder().build().apply {
//                            setSurfaceProvider(previewView.surfaceProvider)
//                        }
//
//                        val analysis = ImageAnalysis.Builder()
//                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                            .build()
//
//                        analysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
//                            val mediaImage = imageProxy.image
//                            if (mediaImage != null) {
//                                val inputImage = InputImage.fromMediaImage(
//                                    mediaImage, imageProxy.imageInfo.rotationDegrees
//                                )
//
//                                scanner.process(inputImage)
//                                    .addOnSuccessListener { barcodes ->
//                                        barcodes.firstOrNull()?.rawValue?.let { rawJson ->
//                                            viewModel.updateQrCode(rawJson)
//                                        }
//                                    }
//                                    .addOnCompleteListener {
//                                        imageProxy.close()
//                                    }
//                            } else {
//                                imageProxy.close()
//                            }
//                        }
//
//                        cameraProvider.unbindAll()
//                        cameraProvider.bindToLifecycle(
//                            lifecycleOwner,
//                            CameraSelector.DEFAULT_BACK_CAMERA,
//                            preview,
//                            analysis
//                        )
//                    }, ContextCompat.getMainExecutor(ctx))
//
//                    previewView
//                },
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//
//        // Overlay for scanned data
//        qrData?.let { data ->
//            Column(
//                modifier = Modifier
//                    .align(Alignment.TopStart)
//                    .padding(16.dp)
//                    .background(Color.Black.copy(alpha = 0.7f))
//                    .padding(12.dp)
//            ) {
//                Text("ID: ${data}", color = Color.White)
//                Text("Content: ${data.content}", color = Color.White)
//                Text("Timestamp: ${data.timestamp}", color = Color.White)
//            }
//
//            // 🩺 "Add Prescription" Button
//            Button (
//                onClick = {
//
//                },
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(16.dp)
//            ) {
//                Text("Add Prescription")
//            }
//        }
//    }
//}
//
//
//
package com.example.projecttdm.ui.doctor.screens

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.projecttdm.ui.patient.PatientRoutes
import com.example.projecttdm.viewmodel.QrViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


@OptIn(ExperimentalGetImage::class)
@Composable
fun QrScannerScreen(
    viewModel: QrViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val qrData by viewModel.qrData.observeAsState()

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(android.Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(qrData) {
        Log.d("QR_DEBUG", "QR Data received: $qrData")
        qrData?.let { data ->
            Log.d("QR_DEBUG", "ID: ${data.id}, Content: ${data.content}, Timestamp: ${data.timestamp}")
            data.id?.let { id ->
                Log.d("QR_DEBUG", "Navigating to patient summary with ID: $id")
                navController.navigate("${PatientRoutes.PatientSummary.route}/$id")
                viewModel.clearQrData()
            } ?: run {
                Log.e("QR_DEBUG", "ID is null in QR data")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasPermission && qrData == null) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    val scanner = BarcodeScanning.getClient()

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder().build().apply {
                            setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val analysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        analysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val inputImage = InputImage.fromMediaImage(
                                    mediaImage, imageProxy.imageInfo.rotationDegrees
                                )

                                scanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        barcodes.firstOrNull()?.rawValue?.let { rawJson ->
                                            Log.d("QR_SCAN", "Raw QR content: $rawJson")
                                            viewModel.updateQrCode(rawJson)
                                        }
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        }

                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            analysis
                        )
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Overlay for scanned data
        qrData?.let { data ->
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(12.dp)
            ) {
                Text("ID: ${data.id ?: "null"}", color = Color.White)
                Text("Content: ${data.content ?: "null"}", color = Color.White)
                Text("Timestamp: ${data.timestamp ?: "null"}", color = Color.White)
            }

            // 🩺 "Add Prescription" Button
            Button (
                onClick = {
                    // Button action
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("Add Prescription")
            }
        }
    }
}