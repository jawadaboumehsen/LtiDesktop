package com.lti.ltidesktop.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.presentation.Screen
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.imagevector.LtiPatcherIcon
import com.lti.ltidesktop.ui.imagevector.WordmarkLtipatcher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(
    currentScreen: Screen,
    isExpanded: Boolean,
    onEvent: (AppEvent) -> Unit,
    content: @Composable () -> Unit
) {
    var expandedWidth by remember { mutableStateOf(260.dp) }
    val sidebarWidth by animateDpAsState(targetValue = if (isExpanded) expandedWidth else 80.dp)
    val density = LocalDensity.current

    Row(modifier = Modifier.fillMaxSize()) {
        // SideNavBar
        Column(
            modifier = Modifier
                .width(sidebarWidth)
                .fillMaxHeight()
                .background(LtiTheme.colors.sidebarBackground)
                .border(width = 1.dp, color = LtiTheme.colors.sidebarBorder, shape = RoundedCornerShape(0.dp)),
            horizontalAlignment = Alignment.Start
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isExpanded) 16.dp else 0.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
            ) {
                if (isExpanded) {
                    Icon(
                        imageVector = WordmarkLtipatcher,
                        contentDescription = "LtiPatcher Wordmark",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .height(32.dp)
                            .clickable { onEvent(AppEvent.ToggleSidebar) }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(LtiTheme.shapes.medium)
                            .background(LtiTheme.colors.primary.copy(alpha = 0.1f))
                            .clickable { onEvent(AppEvent.ToggleSidebar) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = LtiPatcherIcon,
                            contentDescription = "LtiPatcher Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = if (isExpanded) 20.dp else 12.dp),
                thickness = 1.dp,
                color = LtiTheme.colors.sidebarBorder.copy(alpha = 0.3f)
            )

            // Navigation Items
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                NavigationItem(
                    label = "Dashboard",
                    icon = Icons.Default.GridView,
                    selected = currentScreen == Screen.HOME,
                    isExpanded = isExpanded,
                    onClick = { onEvent(AppEvent.NavigateTo(Screen.HOME)) }
                )
                NavigationItem(
                    label = "Remote Console",
                    icon = Icons.Default.Terminal,
                    selected = currentScreen == Screen.CLI,
                    isExpanded = isExpanded,
                    onClick = { onEvent(AppEvent.NavigateTo(Screen.CLI)) }
                )
                NavigationItem(
                    label = "System Settings",
                    icon = Icons.Default.Settings,
                    selected = currentScreen == Screen.SETTINGS,
                    isExpanded = isExpanded,
                    onClick = { onEvent(AppEvent.NavigateTo(Screen.SETTINGS)) }
                )
            }

            Spacer(Modifier.weight(1f))

            // Logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable { /* Sign Out */ }
                    .padding(horizontal = if (isExpanded) 24.dp else 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isExpanded) Arrangement.spacedBy(12.dp) else Arrangement.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = LtiTheme.colors.textSecondary, modifier = Modifier.size(20.dp))
                if (isExpanded) {
                    Text("Sign Out", color = LtiTheme.colors.textSecondary, style = LtiTheme.typography.bodySmall)
                }
            }
        }

        // Drag Handle
        if (isExpanded) {
            var isHandleHovered by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(if (isHandleHovered) LtiTheme.colors.primary.copy(alpha = 0.5f) else Color.Transparent)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                when (event.type) {
                                    androidx.compose.ui.input.pointer.PointerEventType.Enter -> isHandleHovered = true
                                    androidx.compose.ui.input.pointer.PointerEventType.Exit -> isHandleHovered = false
                                }
                            }
                        }
                    }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            change.consume()
                            val newWidth = with(density) {
                                (expandedWidth.toPx() + dragAmount).toDp()
                            }
                            expandedWidth = newWidth.coerceIn(200.dp, 600.dp)
                        }
                    }
            )
        }

        // Main Content Area
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            // TopAppBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(LtiTheme.colors.surface)
                    .border(width = 1.dp, color = LtiTheme.colors.border, shape = RoundedCornerShape(0.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when(currentScreen) {
                        Screen.HOME -> "System Overview"
                        Screen.CLI -> "Remote Terminal Session"
                        Screen.SETTINGS -> "Console Configuration"
                    },
                    color = LtiTheme.colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .background(LtiTheme.colors.success.copy(alpha = 0.1f), LtiTheme.shapes.medium)
                            .border(1.dp, LtiTheme.colors.success.copy(alpha = 0.2f), LtiTheme.shapes.medium)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(LtiTheme.colors.success, LtiTheme.shapes.small))
                        Text("Connected", color = LtiTheme.colors.success, style = LtiTheme.typography.labelSmall)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Transparent)
                            .padding(start = 16.dp)
                    ) {
                        // Left border line using a Box to simulate border-l
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(LtiTheme.colors.border.copy(alpha=0.5f)))
                        Spacer(modifier = Modifier.width(8.dp))

                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text("Network Configuration") } },
                            state = rememberTooltipState()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(LtiTheme.shapes.medium)
                                    .clickable { /* WiFi tethering action */ }
                                    .pointerHoverIcon(PointerIcon.Hand),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.WifiTethering, contentDescription = "Connection", tint = LtiTheme.colors.textSecondary, modifier = Modifier.size(20.dp))
                            }
                        }

                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text("Alerts & Notifications") } },
                            state = rememberTooltipState()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(LtiTheme.shapes.medium)
                                    .clickable { /* Notifications */ }
                                    .pointerHoverIcon(PointerIcon.Hand),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = LtiTheme.colors.textSecondary, modifier = Modifier.size(20.dp))
                                // Notification dot
                                Box(modifier = Modifier.size(6.dp).background(LtiTheme.colors.error, LtiTheme.shapes.small).align(Alignment.TopEnd).offset(x=(-4).dp, y=4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(LtiTheme.colors.surfaceVariant, androidx.compose.foundation.shape.CircleShape)
                                .border(1.dp, LtiTheme.colors.border.copy(alpha=0.5f), androidx.compose.foundation.shape.CircleShape)
                                .clickable { /* User Profile */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", color = LtiTheme.colors.textPrimary, style = LtiTheme.typography.titleSmall.copy(fontSize = 14.sp))
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize().background(LtiTheme.colors.background)) {
                content()
            }
        }
    }
}

@Composable
private fun NavigationItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    val backgroundColor = if (selected) LtiTheme.colors.sidebarItemActive
                          else if (isHovered) LtiTheme.colors.surfaceVariant.copy(alpha = 0.5f)
                          else Color.Transparent
    val contentColor = if (selected) LtiTheme.colors.textPrimary else LtiTheme.colors.textSecondary
    val indicatorColor = if (selected) LtiTheme.colors.primary else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp) // mx-2
            .clip(LtiTheme.shapes.medium)
            .background(backgroundColor)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            androidx.compose.ui.input.pointer.PointerEventType.Enter -> isHovered = true
                            androidx.compose.ui.input.pointer.PointerEventType.Exit -> isHovered = false
                        }
                    }
                }
            }
            .clickable { onClick() }
            .padding(horizontal = if(isExpanded) 12.dp else 0.dp, vertical = 8.dp), // px-3 py-2
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
    ) {
        if (isExpanded && selected) {
            // Left Indicator
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(indicatorColor)
                    .offset(x = (-16).dp) // Move border left edge
            )
        }

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) LtiTheme.colors.info else contentColor,
            modifier = Modifier.size(20.dp).offset(x = if (isExpanded && selected) (-16).dp else 0.dp)
        )

        if (isExpanded) {
            Spacer(Modifier.width(12.dp).offset(x = if (selected) (-16).dp else 0.dp))

            Text(
                text = label,
                color = contentColor,
                style = LtiTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal),
                modifier = Modifier.offset(x = if (selected) (-16).dp else 0.dp)
            )
        }
    }
}
