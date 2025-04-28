package com.example.projecttdm.ui.patient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Doctor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveFavoriteDialog(
    doctor: Doctor,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 24.dp)
        ) {
            // Title with center indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Remove from Favorites?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            )

            // Doctor Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Doctor Image
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    if (doctor.imageResId != null) {
                        Image(
                            painter = painterResource(id = doctor.imageResId),
                            contentDescription = "Doctor image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Placeholder if no image
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.Center),
                            tint = Color.Gray
                        )
                    }
                }

                // Doctor Info
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = doctor.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${doctor.specialty.name} | ${doctor.hospital}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                // Favorite Icon
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            )

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel Button
                Button(
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.hide()
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Yes, Remove Button
                Button(
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.hide()
                            onConfirm()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Yes, Remove",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Add some padding at the bottom to account for system bars
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}