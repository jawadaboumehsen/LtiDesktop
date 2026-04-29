package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.network.ConnectionState
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.TerminalTypography

@Composable
fun HomeScreen(
    state: AppState,
    onEvent: (AppEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LtiTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "System Dashboard",
                    color = LtiTheme.colors.textPrimary,
                    style = LtiTheme.typography.displayLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Overview of network health and activity.",
                    color = LtiTheme.colors.textSecondary,
                    style = LtiTheme.typography.bodyMedium
                )
            }
            
            DesktopButton(
                text = "Refresh Data",
                onClick = { /* Refresh */ },
                icon = Icons.Default.Refresh
            )
        }

        // Bento Box Grid
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column (Cards + Analytics)
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Status Cards Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val isConnected = state.connectionState == ConnectionState.CONNECTED
                    StatusCard(
                        title = "Connection Status",
                        value = if (isConnected) "Active" else "Offline",
                        icon = Icons.Default.Dns,
                        iconColor = LtiTheme.colors.success,
                        iconBg = LtiTheme.colors.success.copy(alpha = 0.1f),
                        subtitle = if (isConnected) "Stable" else "No connection",
                        subtitleColor = if (isConnected) LtiTheme.colors.success else LtiTheme.colors.error,
                        showPulse = isConnected,
                        modifier = Modifier.weight(1f)
                    )
                    StatusCard(
                        title = "Total Commands",
                        value = "1,240",
                        icon = Icons.Default.Terminal,
                        iconColor = LtiTheme.colors.primary,
                        iconBg = LtiTheme.colors.primary.copy(alpha = 0.1f),
                        subtitle = "Last 24h",
                        subtitleColor = LtiTheme.colors.textSecondary,
                        showPulse = false,
                        modifier = Modifier.weight(1f)
                    )
                    StatusCard(
                        title = "System Uptime",
                        value = "4h 12m",
                        icon = Icons.Default.Timer,
                        iconColor = LtiTheme.colors.info,
                        iconBg = LtiTheme.colors.info.copy(alpha = 0.1f),
                        subtitle = "Since last reboot",
                        subtitleColor = LtiTheme.colors.textSecondary,
                        showPulse = false,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Analytics Section
                AnalyticsSection()
            }

            // Right Column (Activity Logs)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(LtiTheme.colors.surfaceContainerLowest, LtiTheme.shapes.medium)
                    .border(1.dp, LtiTheme.colors.border, LtiTheme.shapes.medium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LtiTheme.colors.surface, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recent Activity Logs",
                        color = LtiTheme.colors.textPrimary,
                        style = LtiTheme.typography.titleSmall
                    )
                    Text(
                        text = "View All",
                        color = LtiTheme.colors.primary,
                        style = LtiTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.clickable { /* View All */ }
                    )
                }

                HorizontalDivider(color = LtiTheme.colors.border)

                val mockLogs = listOf(
                    LogData("Login", "login", "Admin User", " authenticated successfully via SSH.", "IP: 192.168.1.104 • Node: Alpha-01", "2 mins ago"),
                    LogData("Sync", "cloud_sync", "", "System backup initiated automatically.", "Target: /mnt/backup/daily • Size: 4.2GB", "45 mins ago"),
                    LogData("Warning", "warning", "", "High memory usage detected on worker node.", "Node: Beta-03 • Usage: 94%", "2 hours ago"),
                    LogData("Build", "build", "System", "Configuration updated by System.", "File: /etc/nginx/nginx.conf • Action: Reload", "4 hours ago")
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().padding(end = 8.dp)
                    ) {
                        items(mockLogs) { log ->
                            ActivityLogItem(log)
                            HorizontalDivider(color = LtiTheme.colors.border.copy(alpha=0.5f))
                        }
                    }
                    
                    var isScrollbarHovered by remember { mutableStateOf(false) }
                    val thickness by androidx.compose.animation.core.animateDpAsState(targetValue = if (isScrollbarHovered) 8.dp else 4.dp)
                    
                    androidx.compose.foundation.VerticalScrollbar(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        when (event.type) {
                                            PointerEventType.Enter -> isScrollbarHovered = true
                                            PointerEventType.Exit -> isScrollbarHovered = false
                                        }
                                    }
                                }
                            },
                        adapter = androidx.compose.foundation.rememberScrollbarAdapter(scrollState = listState),
                        style = androidx.compose.foundation.ScrollbarStyle(
                            minimalHeight = 16.dp,
                            thickness = thickness,
                            shape = LtiTheme.shapes.small,
                            hoverDurationMillis = 300,
                            unhoverColor = LtiTheme.colors.textSecondary.copy(alpha = 0.15f),
                            hoverColor = LtiTheme.colors.textSecondary.copy(alpha = 0.4f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    subtitle: String,
    subtitleColor: Color,
    showPulse: Boolean,
    modifier: Modifier = Modifier
) {
    var isHovered by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(if (isHovered) LtiTheme.colors.surfaceContainer else LtiTheme.colors.surfaceContainerLowest, LtiTheme.shapes.medium)
            .border(1.dp, if (isHovered) LtiTheme.colors.border.copy(alpha=1f) else LtiTheme.colors.border.copy(alpha=0.5f), LtiTheme.shapes.medium)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Enter -> isHovered = true
                            PointerEventType.Exit -> isHovered = false
                        }
                    }
                }
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, LtiTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Text(
                text = title.uppercase(),
                color = LtiTheme.colors.textSecondary,
                style = LtiTheme.typography.labelSmall
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value,
                color = LtiTheme.colors.textPrimary,
                style = LtiTheme.typography.displayLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (showPulse) {
                    Box(modifier = Modifier.size(8.dp).background(LtiTheme.colors.success, LtiTheme.shapes.medium))
                }
                Text(
                    text = subtitle,
                    color = subtitleColor,
                    style = LtiTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

data class LogData(
    val type: String,
    val iconName: String,
    val subject: String,
    val action: String,
    val details: String,
    val time: String
)

@Composable
private fun ActivityLogItem(log: LogData) {
    val (icon, tint, bg) = when (log.iconName) {
        "login" -> Triple(Icons.Default.Login, LtiTheme.colors.success, LtiTheme.colors.success.copy(alpha = 0.1f))
        "cloud_sync" -> Triple(Icons.Default.CloudSync, LtiTheme.colors.primary, LtiTheme.colors.primary.copy(alpha = 0.1f))
        "warning" -> Triple(Icons.Default.Warning, LtiTheme.colors.warning, LtiTheme.colors.warning.copy(alpha = 0.1f))
        "build" -> Triple(Icons.Default.Build, LtiTheme.colors.textSecondary, LtiTheme.colors.surfaceContainer)
        else -> Triple(Icons.Default.Info, Color.Gray, Color.LightGray)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(bg, LtiTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = tint, modifier = Modifier.size(12.dp))
        }
        
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            val text = buildAnnotatedString {
                if (log.subject.isNotEmpty()) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = LtiTheme.colors.textPrimary)) {
                        append(log.subject)
                    }
                    append(" ")
                }
                append(log.action)
                append(" ")
                withStyle(style = SpanStyle(color = LtiTheme.colors.textSecondary)) {
                    append(log.details)
                }
            }
            Text(text, style = LtiTheme.typography.bodySmall, color = LtiTheme.colors.textPrimary, maxLines = 1)
        }
        
        Text(log.time, style = LtiTheme.typography.bodySmall, color = LtiTheme.colors.textSecondary)
    }
}
