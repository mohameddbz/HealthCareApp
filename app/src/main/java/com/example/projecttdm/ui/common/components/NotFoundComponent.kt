package com.example.projecttdm.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projecttdm.R

@Composable
fun NotFoundComponent(
    keyword: String = "",
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // The not found illustration image
        Image(
            painter = painterResource(id = R.drawable.not_found_illustration),
            contentDescription = "Not Found",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // "Not Found" title
        Text(
            text = "Not Found",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Message about the keyword not being found
        Text(
            text = if (keyword.isNotEmpty()) {
                "Désolé, le mot-clé \"$keyword\" que vous avez saisi est introuvable. Veuillez vérifier ou essayer un autre mot-clé."
            } else {
                "Désolé, le mot-clé que vous avez saisi est introuvable. Veuillez vérifier ou essayer un autre mot-clé."
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

    }
}
