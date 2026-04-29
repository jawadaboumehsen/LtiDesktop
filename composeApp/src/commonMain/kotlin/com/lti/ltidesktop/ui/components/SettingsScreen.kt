package com.lti.ltidesktop.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.TerminalTypography
import com.lti.ltidesktop.ui.components.widgets.AntigravitySwitch
import com.lti.ltidesktop.ui.components.widgets.AntigravitySlider

@Composable
fun SettingsScreen(
    state: AppState,
    onEvent: (AppEvent) -> Unit
) {
    var activeTab by remember { mutableStateOf("network") }

    val tabs = listOf(
        TabItem("network", "Network", Icons.Default.WifiTethering),
        TabItem("appearance", "Appearance", Icons.Default.Palette),
        TabItem("system", "System", Icons.Default.Computer),
        TabItem("about", "About", Icons.Default.Info)
    )

    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar (220dp)
        Column(
            modifier = Modifier
                .width(220.dp)
                .fillMaxHeight()
                .background(Color(0xFF0C0C0C))
                .border(width = 0.dp, color = Color.Transparent)
                .drawBehind {
                    drawLine(
                        color = Color(0xFF1A1A1A),
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(horizontal = 12.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            tabs.forEach { tab ->
                val active = activeTab == tab.key
                val tabAccent = LtiTheme.colors.primary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (active) Color(0xFF161616) else Color.Transparent)
                        .drawBehind {
                            if (active) {
                                drawLine(
                                    color = tabAccent,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                        }
                        .clickable { activeTab = tab.key }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = null,
                        tint = if (active) LtiTheme.colors.primary else Color(0xFFA1A1AA),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = tab.label,
                        style = LtiTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = if (active) Color.White else Color(0xFFA1A1AA)
                    )
                }
            }
        }

        // Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(LtiTheme.colors.background)
                .padding(32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (activeTab) {
                "network" -> NetworkPane(state, onEvent)
                "appearance" -> AppearancePane(state, onEvent)
                "system" -> SystemPane(onEvent)
                "about" -> AboutPane()
            }
        }
    }
}

@Composable
private fun NetworkPane(state: AppState, onEvent: (AppEvent) -> Unit) {
    Section(title = "Connection", desc = "How LtiPatcher reaches your remote console") {
        SettingRow(
            label = "Auto-reconnect",
            hint = "Reconnect if the link drops",
            control = {
                AntigravitySwitch(
                    checked = state.settings.autoConnect,
                    onCheckedChange = { onEvent(AppEvent.UpdateAutoConnect(it)) }
                )
            }
        )
        SettingRow(
            label = "Keep-alive packets",
            hint = "Send periodic keepalive",
            control = {
                AntigravitySwitch(
                    checked = true, // Mocked for now
                    onCheckedChange = { }
                )
            }
        )
        SettingRow(
            label = "Timeout",
            hint = "${state.settings.connectionTimeout / 1000}s before reporting unreachable",
            control = {
                AntigravitySlider(
                    value = state.settings.connectionTimeout.toFloat() / 1000f,
                    onValueChange = { onEvent(AppEvent.UpdateConnectionTimeout((it * 1000).toInt())) },
                    valueRange = 5f..60f,
                    modifier = Modifier.width(120.dp)
                )
            }
        )
    }
}

@Composable
private fun AppearancePane(state: AppState, onEvent: (AppEvent) -> Unit) {
    var selectedTheme by remember { mutableStateOf("dark") }

    Section(title = "Appearance") {
        SettingRow(
            label = "Theme",
            hint = "Console color scheme",
            control = {
                Row(
                    modifier = Modifier
                        .background(Color(0xFF161616), RoundedCornerShape(4.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("dark", "midnight").forEach { t ->
                        val active = selectedTheme == t
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (active) LtiTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable { selectedTheme = t }
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = t.uppercase(),
                                style = LtiTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                                color = if (active) LtiTheme.colors.primary else Color(0xFFA1A1AA)
                            )
                        }
                    }
                }
            }
        )
        SettingRow(
            label = "Monospace UI",
            hint = "Use mono font for tabular data",
            control = {
                AntigravitySwitch(
                    checked = true, // Mocked
                    onCheckedChange = { }
                )
            }
        )
    }
}

@Composable
private fun SystemPane(onEvent: (AppEvent) -> Unit) {
    Section(title = "System") {
        SettingRow(
            label = "Restart Console",
            hint = "Disconnect and re-establish session",
            control = {
                DesktopButton(
                    text = "RESTART",
                    onClick = { onEvent(AppEvent.Disconnect); onEvent(AppEvent.Connect) },
                    icon = Icons.Default.Refresh,
                    isPrimary = false
                )
            }
        )
        SettingRow(
            label = "Force Reboot",
            hint = "Send SIGHUP to remote host",
            control = {
                DesktopButton(
                    text = "REBOOT",
                    onClick = { /* Reboot */ },
                    icon = Icons.Default.PowerOff,
                    isPrimary = false,
                    modifier = Modifier.border(1.dp, LtiTheme.colors.error.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                )
            }
        )
    }
}

@Composable
private fun AboutPane() {
    Section(title = "About LtiPatcher") {
        SettingRow(
            label = "Version",
            control = {
                Text("v0.1.0 · build 4291", style = TerminalTypography.copy(fontSize = 12.sp, color = Color(0xFFA1A1AA)))
            }
        )
        SettingRow(
            label = "Runtime",
            control = {
                Text("Compose Multiplatform · JVM 21", style = TerminalTypography.copy(fontSize = 12.sp, color = Color(0xFFA1A1AA)))
            }
        )
        SettingRow(
            label = "License",
            control = {
                Text("MIT", style = TerminalTypography.copy(fontSize = 12.sp, color = Color(0xFFA1A1AA)))
            }
        )
    }
}

@Composable
private fun Section(
    title: String,
    desc: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = title.uppercase(),
                style = LtiTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                color = Color.White
            )
            if (desc != null) {
                Text(
                    text = desc,
                    style = LtiTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color(0xFFA1A1AA),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A1A1A))
                .padding(top = 0.dp), // Divider-style layout
            verticalArrangement = Arrangement.spacedBy(1.dp) // Row spacing = divider
        ) {
            content()
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    hint: String? = null,
    control: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F0F0F))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = label,
                style = LtiTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = Color.White
            )
            if (hint != null) {
                Text(
                    text = hint,
                    style = LtiTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color(0xFF71717A),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        control()
    }
}

private data class TabItem(val key: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
