package com.example.projecttdm.ui.doctor.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.data.model.AppointmentWeekResponse

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentItemAffichage(
    appointment: AppointmentWeekResponse,
    onAppointmentClick: () -> Unit
) {
    val slot = appointment.slot_info
    val patient = appointment.patient_info

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppointmentClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Affichage amélioré de l'heure
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Blue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = slot?.start_time?.substring(0, 5) ?: "--:--",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = patient?.name ?: "Unknown",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${slot?.start_time?.substring(0, 5)} - ${slot?.end_time?.substring(0, 5)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            IconButton(onClick = { /* Handle more options */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
        }
    }
}