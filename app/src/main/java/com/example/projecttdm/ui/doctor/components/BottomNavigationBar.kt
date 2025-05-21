package com.example.projecttdm.ui.components

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

// Data class for navigation items
data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector? = null,
    val painter: Painter? = null
)

// Custom shape for the bottom bar with animated cutout
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

        // Create the path for the bottom bar with cutout
        path.moveTo(0f, cornerRadiusPx)
        path.quadraticBezierTo(0f, 0f, cornerRadiusPx, 0f)
        path.lineTo(startX.coerceIn(cornerRadiusPx, size.width - cornerRadiusPx), 0f)

        // Left curve of cutout
        path.cubicTo(
            x1 = startX + cutoutWidth * 0.2f, y1 = 0f,
            x2 = cutoutHorizontalOffset - cutoutRadiusPx * 0.6f, y2 = centerCutoutY,
            x3 = cutoutHorizontalOffset, y3 = centerCutoutY
        )

        // Right curve of cutout
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
fun AnimatedBottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
    selectedItemProvider: (String?) -> Int = { _ -> 0 },
    modifier: Modifier = Modifier,
    cutoutRadius: Dp = 48.dp,
    barHeight: Dp = 65.dp,
    totalHeight: Dp = 85.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = Color.Gray
) {
    val currentRoute = currentRoute(navController)

    val selectedItem by remember(currentRoute) {
        derivedStateOf {
            selectedItemProvider(currentRoute)
        }
    }

    val density = LocalDensity.current
    val cornerRadius = 0.dp
    val bottomPadding = 24.dp

    val targetOffset = remember(selectedItem, items.size) {
        derivedStateOf {
            if (items.size > 0) {
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
        // Background shape with animated cutout
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
                    .background(backgroundColor)
            )
        }

        // Navigation items row
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
                    item = item,
                    isSelected = selectedItem == index,
                    cutoutRadius = cutoutRadius,
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = false
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = false
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
    item: BottomNavItem,
    isSelected: Boolean,
    cutoutRadius: Dp,
    selectedColor: Color,
    unselectedColor: Color,
    onClick: () -> Unit
) {
    val targetOffsetValue = if (isSelected) -(cutoutRadius.value * 0.7f) else 0f
    val animatedOffset by animateFloatAsState(
        targetValue = targetOffsetValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = tween(200, easing = FastOutSlowInEasing)
    )

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
            modifier = Modifier.padding(top = 0.dp)
        ) {
            // Background circle for selected item
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(selectedColor, CircleShape)
                        .scale(scale)
                )
            }

            // Icon - can be either ImageVector or Painter
            when {
                item.icon != null -> {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) Color.White else unselectedColor,
                        modifier = Modifier.size(if (isSelected) 28.dp else 24.dp)
                    )
                }
                item.painter != null -> {
                    Icon(
                        painter = item.painter,
                        contentDescription = item.title,
                        tint = if (isSelected) Color.White else unselectedColor,
                        modifier = Modifier.size(if (isSelected) 28.dp else 24.dp)
                    )
                }
            }
        }

        // Animated label visibility
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn(animationSpec = tween(150, delayMillis = 100)) +
                    expandVertically(animationSpec = tween(200, delayMillis = 100)),
            exit = fadeOut(animationSpec = tween(100)) +
                    shrinkVertically(animationSpec = tween(150))
        ) {
            Text(
                text = item.title,
                color = selectedColor,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

// Convenience composable for wrapping content with bottom navigation
@Composable
fun ScreenWithBottomNavigation(
    navController: NavHostController,
    items: List<BottomNavItem>,
    selectedItemProvider: (String?) -> Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Main content with bottom padding to avoid overlap
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 125.dp) // Reserve space for bottom navigation
        ) {
            content()
        }

        // Bottom Navigation positioned at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-40).dp) // Move up slightly from bottom edge
        ) {
            AnimatedBottomNavigationBar(
                navController = navController,
                items = items,
                selectedItemProvider = selectedItemProvider
            )
        }
    }
}