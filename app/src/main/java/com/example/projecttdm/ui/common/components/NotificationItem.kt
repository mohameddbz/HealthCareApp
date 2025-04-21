package com.example.projecttdm.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Notification
import com.example.projecttdm.theme.Baloo
import com.example.projecttdm.theme.NotificationColors

@Composable
fun NotificationItem(notification: Notification) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                NotificationIcon(notification.title)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = notification.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontFamily = Baloo,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${notification.date} | ${notification.heure}",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontFamily = Baloo,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )

                        if (!notification.is_read) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text(
                                    "New",
                                    fontFamily = Baloo,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notification.message,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontFamily = Baloo,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun NotificationIcon(title: String) {
    val (backgroundColor, iconRes) = when (title) {
        "Appointment Cancelled" -> NotificationColors.canceled to R.drawable.cancel_icon
        "Schedule Modified" -> NotificationColors.changed to R.drawable.schedule_green
        "Appointment Confirmed" -> NotificationColors.success to R.drawable.schedule_blue
        else -> NotificationColors.yellow to R.drawable.bell
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Notification Icon",
            modifier = Modifier.size(24.dp)
        )
    }
}