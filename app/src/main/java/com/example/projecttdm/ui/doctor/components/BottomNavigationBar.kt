package com.example.projecttdm.ui.doctor.components
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projecttdm.R
import com.example.projecttdm.ui.doctor.DoctorRoutes
// Your existing BottomNavItem class
sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem(DoctorRoutes.HomeScreen.route, Icons.Outlined.Home, "Home")
    object Notifications : BottomNavItem(DoctorRoutes.AppointmentValidationScreen.route, Icons.Outlined.Notifications, "Notifications")
    object Dashboard : BottomNavItem(DoctorRoutes.QrScanner.route, Icons.Outlined.Apps, "Dashboard")
    object Profile : BottomNavItem(DoctorRoutes.QrScanner.route, Icons.Outlined.Person, "Profile")
    object Settings : BottomNavItem(DoctorRoutes.AppointmentOfWeek.route, Icons.Outlined.Settings, "Settings")
}

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

@Composable
fun AnimatedBottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        DoctorRoutes.HomeScreen to "Home",
        DoctorRoutes.AppointmentValidationScreen to "Appointments",
        DoctorRoutes.QrScanner to "Camera" ,
        DoctorRoutes.AppointmentOfWeek to "Week"
    )

    val currentRoute = currentRoute(navController)

    val selectedItem by remember(currentRoute) {
        derivedStateOf {
            currentRoute?.let { route ->
                when {
                    route == DoctorRoutes.HomeScreen.route || route.startsWith(DoctorRoutes.HomeScreen.route) -> 0
                    route == DoctorRoutes.AppointmentValidationScreen.route || route.startsWith(DoctorRoutes.AppointmentValidationScreen.route) -> 1
                    route == DoctorRoutes.QrScanner.route || route.startsWith(DoctorRoutes.QrScanner.route) -> 2
                    route == DoctorRoutes.AppointmentOfWeek.route || route.startsWith(DoctorRoutes.AppointmentOfWeek.route) -> 3
                   /* route == Screen.DeviceDetails.route || route.startsWith(Screen.DeviceDetails.route) -> 0
                    route == Screen.Main_account.route || route.startsWith(Screen.Main_account.route) -> 2
                    route == Screen.TaskDetails.route || route.startsWith(Screen.TaskDetails.route) -> 1
                    route == Screen.Login.route || route.startsWith(Screen.Login.route) -> 0
                    route == Screen.InterventionHistory.route || route.startsWith(Screen.InterventionHistory.route) -> 0 */
                    else -> 1
                }
            } ?: 2 // Default to 2 if currentRoute is null
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
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
                items.forEachIndexed { index, (screen, label) ->
                    NavItem(
                        icon = painterResource(id = when (screen) {
                            DoctorRoutes.HomeScreen -> R.drawable.d_home
                            DoctorRoutes.AppointmentValidationScreen -> R.drawable.d_tasks
                            DoctorRoutes.AppointmentOfWeek -> R.drawable.d_account
                            DoctorRoutes.QrScanner -> R.drawable.d_camera
                            else -> R.drawable.d_home
                        }),
                        label = label,
                        isSelected = selectedItem == index,
                        cutoutRadius = cutoutRadius,
                        onClick = {
                            navController.navigate(screen.route)
//                            {
//                                popUpTo(navController.graph.startDestinationId) {
//                                    saveState = true
//                                }
//                                launchSingleTop = true
//                                restoreState = true
//                            }
                            {
                                // Ne pas réutiliser l'état précédent - forcer la création d'un nouveau composable
                                restoreState = false
                                // Éviter d'empiler les écrans multiples de détails
                                launchSingleTop = true
                                // Option pour nettoyer la pile de navigation
                                popUpTo(DoctorRoutes.HomeScreen.route) {
                                    saveState = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.NavItem(
    icon: Painter,
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
                painter = icon,
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



