package com.example.projecttdm.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Notification
import com.example.projecttdm.theme.Baloo
import com.example.projecttdm.theme.canceledNotification
import com.example.projecttdm.theme.changedNotification
import com.example.projecttdm.theme.succesNotification
import com.example.projecttdm.theme.textNotification
import com.example.projecttdm.theme.yellowNotification


@Composable
fun NotificationItem(notification: Notification) {

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp , vertical = 12.dp)
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ){
            CustomIcon(notification.title)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = notification.title,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = Baloo,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                //               Spacer(modifier = Modifier.height(0.dp)) // Utilisez height au lieu de width

                Row (
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.date + " | " + notification.heure ,
                        color = textNotification,
                        fontFamily = Baloo,
                        fontWeight = FontWeight.Normal
                    )
                    if (!notification.is_read){
                        Box (
                            modifier = Modifier.background(colorResource(id = R.color.bleu), shape = RoundedCornerShape(12.dp)).padding(horizontal = 4.dp, vertical = 2.dp)
                        ){
                            Text("New", color = colorResource(id = R.color.white), fontFamily = Baloo , fontSize = 12.sp ,fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

            }



        }

        Text(
            notification.message,
            color = textNotification,
            fontFamily = Baloo,
            fontWeight = FontWeight.Normal
        )

    }
}



@Composable
fun CustomIcon(title : String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(62.dp)
            .background(color = when (title) {
                "Appointment Cancelled" -> canceledNotification
                "Schedule Modified" -> changedNotification
                "Appointment Confirmed" -> succesNotification
                else ->  yellowNotification // Icône par défaut
            }, shape = CircleShape)
    ) {
        Image(
            painter = painterResource(id = when (title){
                "Appointment Cancelled" -> R.drawable.cancel_icon
                "Schedule Modified" -> R.drawable.schedule_green
                "Appointment Confirmed" -> R.drawable.schedule_blue
                else ->  R.drawable.bell
            }),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(32.dp)

        )

    }
}