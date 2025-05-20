package com.example.projecttdm.ui.doctor.components

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.ImageBlob
import com.example.projecttdm.ui.doctor.screens.CardBackgroundColor
import com.example.projecttdm.ui.doctor.screens.GrayTextColor
import com.example.projecttdm.ui.doctor.screens.LightBackgroundColor
import com.example.projecttdm.ui.doctor.screens.PrimaryColor




@Composable
fun AppointmentItem(name: String, time: String, type: String, imageRes: ImageBlob?) {
    var expanded by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (expanded) 8.dp else 2.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "elevation"
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(elevation, RoundedCornerShape(12.dp)),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        imageRes?.let { imageBlob ->
                            val byteArray = imageBlob.data.map { it.toByte() }.toByteArray()

                            val imageBitmap = remember(byteArray) {
                                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)?.asImageBitmap()
                            }

                            imageBitmap?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = "Patient ${name}",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } ?: run {
                                // Optional: fallback image if imageBitmap is null
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Default Avatar",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } ?: run {
                            // Optional: fallback when imageBlob is null
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "No Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }


                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.Schedule,
                                contentDescription = "Time",
                                tint = GrayTextColor,
                                modifier = Modifier.size(14.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                time,
                                style = MaterialTheme.typography.bodySmall,
                                color = GrayTextColor
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Icon(
                                Icons.Rounded.LocalHospital,
                                contentDescription = "Type",
                                tint = GrayTextColor,
                                modifier = Modifier.size(14.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                type,
                                style = MaterialTheme.typography.bodySmall,
                                color = GrayTextColor
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(LightBackgroundColor)
                ) {
                    Icon(
                        if (expanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "Expand",
                        tint = PrimaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Divider(
                        color = Color(0xFFEEEEEE),
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { /* Start appointment */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryColor
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Rounded.PlayArrow,
                                contentDescription = "Start",
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text("Commencer")
                        }

                        Button(
                            onClick = { /* Reschedule */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = PrimaryColor
                            ),
                            border = ButtonDefaults.outlinedButtonBorder,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Rounded.Schedule,
                                contentDescription = "Reschedule",
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text("Reprogramer")
                        }
                    }
                }
            }
        }
    }
}




