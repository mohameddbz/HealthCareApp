package com.example.projecttdm.ui.common.navigationBar

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.compose.ui.graphics.vector.ImageVector

// Modèle d'élément de la barre de navigation
data class CbnMenuItem(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector, // Nous utiliserons un ImageVector pour simplifier
    val title: String
)