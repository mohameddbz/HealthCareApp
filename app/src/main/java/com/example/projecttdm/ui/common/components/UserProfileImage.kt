package com.example.projecttdm.ui.common.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projecttdm.R
import com.example.projecttdm.data.model.ImageBlob
import com.example.projecttdm.ui.patient.screens.WindowType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun UserProfileImage(imageBlob: ImageBlob?) {
    // État pour stocker le bitmap une fois chargé
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Effet pour charger l'image de manière asynchrone
    LaunchedEffect(imageBlob) {
        if (imageBlob != null) {
            bitmap = withContext(Dispatchers.IO) {
                try {
                    // Conversion et décodage dans un thread d'I/O
                    val byteArray = imageBlob.data.map { it.toByte() }.toByteArray()
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                } catch (e: Exception) {
                    // Gérer les erreurs de décodage
                    e.printStackTrace()
                    null
                }
            }
        }
    }

    // Affichage de l'image
    if (bitmap != null) {
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clip(CircleShape),
                 )
    } else {
        // Image par défaut pendant le chargement ou en cas d'erreur
        Image(
            painter = painterResource(id = R.drawable.default_profil),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clip(CircleShape),
        )

    }
}