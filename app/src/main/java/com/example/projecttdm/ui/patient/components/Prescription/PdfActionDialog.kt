package com.example.projecttdm.ui.patient.components.Prescription

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.utils.savePdfToDownloads


@Composable
fun PdfActionDialog(
    showDialog: Boolean,
    uri: Uri?,
    onDismiss: () -> Unit,
    context: Context
) {
    if (!showDialog || uri == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = "PDF Icon",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Que voulez-vous faire ?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = "Souhaitez-vous ouvrir ou enregistrer le fichier PDF ?",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val buttonModifier = Modifier
                    .weight(1f)
                    .height(48.dp)

                OutlinedButton(
                    onClick = {
                        savePdfToDownloads(context, uri, "ordonnance.pdf")
                        onDismiss()
                    },
                    modifier = buttonModifier.then(Modifier.padding(end = 4.dp)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Enregistrer")
                }

                Button(
                    onClick = {
                        val openIntent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "application/pdf")
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
                        }
                        context.startActivity(Intent.createChooser(openIntent, "Ouvrir l'ordonnance"))
                        onDismiss()
                    },
                    modifier = buttonModifier.then(Modifier.padding(start = 4.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Ouvrir", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    )
}

