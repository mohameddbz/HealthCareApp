package com.example.projecttdm.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Notification
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.projecttdm.ui.common.components.NotificationItem
import com.example.projecttdm.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(viewModel:NotificationViewModel,navController :NavHostController) {
    val notificationData by viewModel.notificationData.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications" , color = Black , fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = {  navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* More options */ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier

                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(notificationData) { notification ->
                NotificationItem(notification)
            }
        }
    }
}


