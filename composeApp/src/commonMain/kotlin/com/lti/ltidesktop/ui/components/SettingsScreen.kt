package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.presentation.Screen
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.ThemeManager
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    state: AppState,
    onEvent: (AppEvent) -> Unit,
    themeManager: ThemeManager = koinInject()
) {
    var selectedTab by remember { mutableStateOf(SettingsTab.GENERAL) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(LtiTheme.colors.background)
    ) {
        // Left Rail Navigation
        Column(
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
                .padding(vertical = 32.dp, horizontal = 16.dp)
        ) {
            Text(
                "Preferences",
                style = LtiTheme.typography.displayLarge,
                color = LtiTheme.colors.textPrimary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            SettingsTab.values().forEach { tab ->
                val isSelected = selectedTab == tab
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(LtiTheme.shapes.medium)
                        .background(if (isSelected) LtiTheme.colors.surfaceContainer else Color.Transparent)
                        .clickable { selectedTab = tab }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = null,
                        tint = if (isSelected) LtiTheme.colors.primary else LtiTheme.colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        tab.label,
                        style = LtiTheme.typography.bodyMedium,
                        color = if (isSelected) LtiTheme.colors.textPrimary else LtiTheme.colors.textSecondary,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom Action
            DesktopButton(
                text = "Back to Home",
                onClick = { onEvent(AppEvent.NavigateTo(Screen.HOME)) },
                icon = Icons.Default.ArrowBack,
                modifier = Modifier.fillMaxWidth()
            )
        }

        VerticalDivider(color = LtiTheme.colors.border)

        // Main Content Area
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 64.dp, vertical = 48.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        selectedTab.label,
                        style = LtiTheme.typography.displayLarge,
                        color = LtiTheme.colors.textPrimary
                    )
                    Text(
                        selectedTab.description,
                        style = LtiTheme.typography.bodySmall,
                        color = LtiTheme.colors.textSecondary
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = { /* Reset */ }) {
                        Text("Reset to Default", color = LtiTheme.colors.textSecondary, style = LtiTheme.typography.labelSmall)
                    }
                    DesktopButton(
                        text = "Apply Changes",
                        onClick = { onEvent(AppEvent.SaveSettings) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(color = LtiTheme.colors.border)
            Spacer(modifier = Modifier.height(32.dp))

            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    SettingsTab.GENERAL -> GeneralSettings(state, onEvent)
                    SettingsTab.TERMINAL -> TerminalSettings(state, onEvent)
                    SettingsTab.CONNECTION -> ConnectionSettings(state, onEvent)
                    SettingsTab.APPEARANCE -> AppearanceSettings(state, onEvent, themeManager)
                    SettingsTab.ABOUT -> AboutSection()
                }
            }
        }
    }
}

@Composable
private fun SettingsGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp)) {
        Text(
            title.uppercase(),
            style = LtiTheme.typography.labelSmall,
            color = LtiTheme.colors.textSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LtiTheme.colors.surfaceContainerLowest, LtiTheme.shapes.medium)
                .border(1.dp, LtiTheme.colors.border, LtiTheme.shapes.medium)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsRow(
    title: String,
    description: String,
    control: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 32.dp)) {
            Text(title, style = LtiTheme.typography.bodyMedium, color = LtiTheme.colors.textPrimary, fontWeight = FontWeight.Medium)
            Text(description, style = LtiTheme.typography.bodySmall, color = LtiTheme.colors.textSecondary)
        }
        control()
    }
}

@Composable
private fun SettingsScrollablePanel(content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        val state = androidx.compose.foundation.lazy.rememberLazyListState()
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize().padding(end = 16.dp),
            content = content
        )
        
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
            adapter = androidx.compose.foundation.rememberScrollbarAdapter(scrollState = state),
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

@Composable
private fun GeneralSettings(state: AppState, onEvent: (AppEvent) -> Unit) {
    SettingsScrollablePanel {
        item {
            SettingsGroup("Application") {
                SettingsRow(
                    "Auto-connect on Startup",
                    "Automatically attempt to connect to the last known host when the app starts."
                ) {
                    Switch(
                        checked = state.settings.autoConnect,
                        onCheckedChange = { onEvent(AppEvent.UpdateAutoConnect(it)) },
                        colors = ltiSwitchColors()
                    )
                }
                
                SettingsRow(
                    "Language",
                    "Select your preferred interface language."
                ) {
                    // Mock Dropdown
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .background(LtiTheme.colors.surface, LtiTheme.shapes.small)
                            .border(1.dp, LtiTheme.colors.border, LtiTheme.shapes.small)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(state.settings.language, style = LtiTheme.typography.bodyMedium, color = LtiTheme.colors.textPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun TerminalSettings(state: AppState, onEvent: (AppEvent) -> Unit) {
    SettingsScrollablePanel {
        item {
            SettingsGroup("Behavior") {
                SettingsRow(
                    "Scrollback Limit",
                    "Number of lines to keep in the terminal history."
                ) {
                    OutlinedTextField(
                        value = state.settings.historyLimit.toString(),
                        onValueChange = { val limit = it.toIntOrNull() ?: 0; onEvent(AppEvent.UpdateHistoryLimit(limit)) },
                        modifier = Modifier.width(120.dp),
                        textStyle = LtiTheme.typography.bodyMedium,
                        singleLine = true,
                        shape = LtiTheme.shapes.small,
                        colors = ltiTextFieldColors()
                    )
                }
            }
            
            SettingsGroup("Text") {
                SettingsRow(
                    "Font Size",
                    "The size of the terminal text (10px - 24px)."
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Slider(
                            value = state.settings.fontSize.toFloat(),
                            onValueChange = { onEvent(AppEvent.UpdateFontSize(it.toInt())) },
                            valueRange = 10f..24f,
                            modifier = Modifier.width(150.dp),
                            colors = SliderDefaults.colors(thumbColor = LtiTheme.colors.primary, activeTrackColor = LtiTheme.colors.primary)
                        )
                        Text("${state.settings.fontSize}px", style = LtiTheme.typography.labelSmall, color = LtiTheme.colors.textPrimary, modifier = Modifier.width(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectionSettings(state: AppState, onEvent: (AppEvent) -> Unit) {
    SettingsScrollablePanel {
        item {
            SettingsGroup("Network") {
                SettingsRow(
                    "Connection Timeout",
                    "Time in milliseconds to wait before aborting a connection attempt."
                ) {
                    OutlinedTextField(
                        value = state.settings.connectionTimeout.toString(),
                        onValueChange = { val t = it.toIntOrNull() ?: 0; onEvent(AppEvent.UpdateConnectionTimeout(t)) },
                        modifier = Modifier.width(120.dp),
                        textStyle = LtiTheme.typography.bodyMedium,
                        singleLine = true,
                        shape = LtiTheme.shapes.small,
                        colors = ltiTextFieldColors()
                    )
                }
            }
        }
    }
}

@Composable
private fun AppearanceSettings(state: AppState, onEvent: (AppEvent) -> Unit, themeManager: ThemeManager) {
    SettingsScrollablePanel {
        item {
            SettingsGroup("Theme") {
                SettingsRow(
                    "Dark Mode",
                    "Toggle between light and dark professional zinc themes."
                ) {
                    Switch(
                        checked = themeManager.isDark,
                        onCheckedChange = { themeManager.toggleTheme() },
                        colors = ltiSwitchColors()
                    )
                }
            }
            
            SettingsGroup("Window") {
                SettingsRow(
                    "Background Opacity",
                    "Adjust the transparency of the application panels."
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Slider(
                            value = state.settings.opacity,
                            onValueChange = { onEvent(AppEvent.UpdateOpacity(it)) },
                            valueRange = 0.5f..1.0f,
                            modifier = Modifier.width(150.dp),
                            colors = SliderDefaults.colors(thumbColor = LtiTheme.colors.primary, activeTrackColor = LtiTheme.colors.primary)
                        )
                        Text("${(state.settings.opacity * 100).toInt()}%", style = LtiTheme.typography.labelSmall, color = LtiTheme.colors.textPrimary, modifier = Modifier.width(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutSection() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(LtiTheme.colors.primary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Terminal, null, tint = LtiTheme.colors.primary, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("LtiDesktop CLI", style = LtiTheme.typography.displayLarge, color = LtiTheme.colors.textPrimary)
        Text("Version 2.4.0-kinetic", style = LtiTheme.typography.bodySmall, color = LtiTheme.colors.textSecondary)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "© 2026 Lti Solutions. All rights reserved.\nA professional cross-platform terminal interface.",
            style = LtiTheme.typography.bodySmall,
            color = LtiTheme.colors.textSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun ltiSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor = Color.White,
    checkedTrackColor = LtiTheme.colors.primary,
    uncheckedThumbColor = LtiTheme.colors.textSecondary,
    uncheckedTrackColor = LtiTheme.colors.border
)

@Composable
private fun ltiTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = LtiTheme.colors.primary,
    unfocusedBorderColor = LtiTheme.colors.border,
    focusedTextColor = LtiTheme.colors.textPrimary,
    unfocusedTextColor = LtiTheme.colors.textPrimary
)

private enum class SettingsTab(val label: String, val icon: ImageVector, val description: String) {
    GENERAL("General", Icons.Default.Settings, "Configure general application behavior and localization."),
    TERMINAL("Terminal", Icons.Default.Terminal, "Customize the command-line interface and history behavior."),
    CONNECTION("Connection", Icons.Default.Cloud, "Manage network settings, timeouts, and connectivity."),
    APPEARANCE("Appearance", Icons.Default.Palette, "Personalize the visual style, theme, and transparency."),
    ABOUT("About", Icons.Default.Info, "Technical specifications and version information.")
}
