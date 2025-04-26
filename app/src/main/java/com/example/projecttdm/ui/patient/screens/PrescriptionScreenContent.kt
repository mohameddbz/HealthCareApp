package com.example.projecttdm.ui.patient.screens

import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projecttdm.theme.ProjectTDMTheme
import com.example.projecttdm.ui.patient.components.Prescription.PdfActionDialog
import com.example.projecttdm.ui.patient.components.Prescription.PrescriptionContent
import com.example.projecttdm.viewmodel.PrescriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionScreenContent(viewModel: PrescriptionViewModel = viewModel()) {
    val prescription by viewModel.prescription.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val downloadSuccess by viewModel.downloadSuccess.collectAsState()
    val context = LocalContext.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var prescriptionComposeView by remember { mutableStateOf<ComposeView?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var uriToHandle by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(downloadSuccess) {
        downloadSuccess?.let {
            uriToHandle = it
            showDialog = true
        }
    }

    if (showDialog && uriToHandle != null) {
        PdfActionDialog(
            showDialog = showDialog,
            uri = uriToHandle,
            onDismiss = { showDialog = false },
            context = context
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Prescription",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge // Apply theme typography
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    backDispatcher?.onBackPressed()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Retour"
                    )
                }
            }
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            prescription?.let { prescriptionData ->
                AndroidView(
                    factory = { ctx ->
                        ComposeView(ctx).apply {
                            prescriptionComposeView = this
                            setContent {
                                ProjectTDMTheme {
                                    PrescriptionContent(prescription = prescriptionData)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            } ?: CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = {
                prescriptionComposeView?.let { view ->
                    viewModel.downloadPrescription(context, view)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = !isLoading && prescription != null,
            shape = MaterialTheme.shapes.large
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    "Télécharger l'ordonnance",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}