package com.example.projecttdm.ui.patient.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projecttdm.ui.patient.PatientRoutes

class BottomBarShape(
    private val cutoutRadius: Dp,
    private val cornerRadius: Dp,
    private val bottomPadding: Dp,
    private val cutoutHorizontalOffset: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val cutoutRadiusPx = with(density) { cutoutRadius.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val bottomPaddingPx = with(density) { bottomPadding.toPx() }
        val barHeight = size.height - bottomPaddingPx
        val cutoutDiameter = cutoutRadiusPx * 2
        val cutoutWidth = cutoutDiameter * 1.1f
        val startX = cutoutHorizontalOffset - cutoutWidth / 2f
        val endX = cutoutHorizontalOffset + cutoutWidth / 2f
        val centerCutoutY = cutoutRadiusPx
        path.moveTo(0f, cornerRadiusPx)
        path.quadraticBezierTo(0f, 0f, cornerRadiusPx, 0f)
        path.lineTo(startX.coerceIn(cornerRadiusPx, size.width - cornerRadiusPx), 0f)
        path.cubicTo(
            x1 = startX + cutoutWidth * 0.2f, y1 = 0f,
            x2 = cutoutHorizontalOffset - cutoutRadiusPx * 0.6f, y2 = centerCutoutY,
            x3 = cutoutHorizontalOffset, y3 = centerCutoutY
        )
        path.cubicTo(
            x1 = cutoutHorizontalOffset + cutoutRadiusPx * 0.6f, y1 = centerCutoutY,
            x2 = endX - cutoutWidth * 0.2f, y2 = 0f,
            x3 = endX.coerceIn(cornerRadiusPx, size.width - cornerRadiusPx), y3 = 0f
        )
        path.lineTo(size.width - cornerRadiusPx, 0f)
        path.quadraticBezierTo(size.width, 0f, size.width, cornerRadiusPx)
        path.lineTo(size.width, barHeight - cornerRadiusPx)
        path.quadraticBezierTo(size.width, barHeight, size.width - cornerRadiusPx, barHeight)
        path.lineTo(cornerRadiusPx, barHeight)
        path.quadraticBezierTo(0f, barHeight, 0f, barHeight - cornerRadiusPx)
        path.lineTo(0f, cornerRadiusPx)
        path.close()
        return Outline.Generic(path)
    }
}

data class PatientNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun AnimatedBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        PatientNavItem(
            title = "Home",
            icon = Icons.Default.Home,
            route = PatientRoutes.HomeScreen.route
        ),
        PatientNavItem(
            title = "Appointment",
            icon = Icons.Default.Person,
            route = PatientRoutes.Appointment.route
        ),
        PatientNavItem(
            title = "Doctors",
            icon = Icons.Default.Star,
            route = PatientRoutes.topDoctors.route
        ),
        PatientNavItem(
            title = "Notifications",
            icon = Icons.Default.Notifications,
            route = PatientRoutes.NotificationScreen.route
        ),
        PatientNavItem(
            title = "Profile",
            icon = Icons.Default.MedicalServices,
            route = PatientRoutes.Profile.route
        )
    )

    val currentRoute = currentRoute(navController)

    val selectedItem by remember(currentRoute) {
        derivedStateOf {
            when {
                currentRoute == PatientRoutes.HomeScreen.route ||
                        currentRoute?.startsWith(PatientRoutes.HomeScreen.route) == true -> 0

                currentRoute == PatientRoutes.Appointment.route ||
                        currentRoute?.startsWith(PatientRoutes.Appointment.route) == true ||
                        currentRoute?.contains("BookAppointment") == true ||
                        currentRoute?.contains("PatientDetails") == true ||
                        currentRoute?.contains("PatientSummary") == true ||
                        currentRoute?.contains("CancelReason") == true ||
                        currentRoute?.contains("CancelDialog") == true ||
                        currentRoute?.contains("RescheduleAppointment") == true ||
                        currentRoute?.contains("RescheduleReason") == true ||
                        currentRoute?.contains("AppointmentQR") == true -> 1

                currentRoute == PatientRoutes.topDoctors.route ||
                        currentRoute?.startsWith(PatientRoutes.topDoctors.route) == true ||
                        currentRoute?.contains("doctorProfile") == true ||
                        currentRoute?.contains("searchDoctor") == true ||
                        currentRoute?.contains("FavoriteDoctors") == true -> 2

                currentRoute == PatientRoutes.NotificationScreen.route ||
                        currentRoute?.startsWith(PatientRoutes.NotificationScreen.route) == true -> 3

                currentRoute?.contains("Prescription") == true -> 4

                else -> 0
            }
        }
    }

    val density = LocalDensity.current
    val cutoutRadius = 48.dp
    val cornerRadius = 0.dp
    val barHeight = 65.dp
    val totalHeight = 85.dp
    val bottomPadding = 24.dp
    val targetOffset = remember(selectedItem, items.size) {
        derivedStateOf {
            if (items.size > 0) {
                val itemWidthPx = density.density * (360.dp.value / items.size)
                val fraction = (selectedItem + 0.5f) / items.size.toFloat()
                fraction
            } else {
                0.5f
            }
        }
    }

    val animatedCutoutFraction by animateFloatAsState(
        targetValue = targetOffset.value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeight)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(barHeight + bottomPadding)
                .zIndex(0f)
        ) {
            val widthPx = with(LocalDensity.current) { maxWidth.toPx() }
            val animatedCutoutOffsetPx = animatedCutoutFraction * widthPx
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        BottomBarShape(
                            cutoutRadius = cutoutRadius,
                            cornerRadius = cornerRadius,
                            bottomPadding = 0.dp,
                            cutoutHorizontalOffset = animatedCutoutOffsetPx
                        )
                    )
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(barHeight)
                .padding(bottom = 0.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top
        ) {
            items.forEachIndexed { index, item ->
                NavItem(
                    icon = item.icon,
                    label = item.title,
                    isSelected = selectedItem == index,
                    cutoutRadius = cutoutRadius,
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RowScope.NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    cutoutRadius: Dp,
    onClick: () -> Unit
) {
    val targetOffsetValue = if (isSelected) - (cutoutRadius.value * 0.7f) else 0f
    val animatedOffset by animateFloatAsState(
        targetValue = targetOffsetValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = tween(200)
    )

    val itemColor = MaterialTheme.colorScheme.primary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .zIndex(if (isSelected) 1f else 0f)
            .offset(y = animatedOffset.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 0.dp)
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(itemColor, CircleShape)
                        .scale(scale)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier
                    .size(if (isSelected) 28.dp else 24.dp)
            )
        }

        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn(animationSpec = tween(150, delayMillis = 100)) +
                    expandVertically(animationSpec = tween(200, delayMillis = 100)),
            exit = fadeOut(animationSpec = tween(100)) +
                    shrinkVertically(animationSpec = tween(150)),
        ) {
            Text(
                text = label,
                color = itemColor,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
@Composable
fun MainScreenWithBottomNav(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Your main content screens - they will have bottom padding to avoid overlap
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 125.dp) // Reserve space for bottom navigation (85dp + 40dp)
        ) {
            // Your NavHost or screen content goes here
            // This content will not overlap with the bottom navigation
            NavHost(
                navController = navController,
                startDestination = PatientRoutes.HomeScreen.route
            ) {
                // Your screen destinations
            }
        }

        // Bottom Navigation - positioned at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-40).dp) // Move up by 40dp from bottom
        ) {
            AnimatedBottomNavigationBar(navController = navController)
        }
    }
}