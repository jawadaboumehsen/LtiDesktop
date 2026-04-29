package com.lti.ltidesktop.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Power
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.network.ConnectionState
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.ui.theme.LtiTheme

import androidx.compose.ui.draw.shadow

@Composable
fun ConnectScreen(
    state: AppState,
    onEvent: (AppEvent) -> Unit
) {
    val isConnecting = state.connectionState == ConnectionState.CONNECTING

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LtiTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(440.dp)
                .shadow(elevation = 24.dp, shape = LtiTheme.shapes.medium, spotColor = Color.Black.copy(alpha = 0.5f))
                .background(LtiTheme.colors.surface, LtiTheme.shapes.medium)
                .border(1.dp, LtiTheme.colors.border, LtiTheme.shapes.medium)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(LtiTheme.colors.surfaceVariant, LtiTheme.shapes.medium)
                        .border(1.dp, LtiTheme.colors.border, LtiTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Dns,
                        contentDescription = null,
                        tint = LtiTheme.colors.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "System Console",
                        color = LtiTheme.colors.textPrimary,
                        style = LtiTheme.typography.displayLarge
                    )
                    Text(
                        text = "Establish a secure connection to the host server to continue.",
                        color = LtiTheme.colors.textSecondary,
                        style = LtiTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            // Connection Form
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ConnectTextField(
                    value = state.host,
                    onValueChange = { onEvent(AppEvent.UpdateHost(it)) },
                    label = "Server IP Address",
                    placeholder = "e.g., 192.168.1.100",
                    icon = Icons.Default.Computer,
                    enabled = !isConnecting
                )
                ConnectTextField(
                    value = state.port,
                    onValueChange = { onEvent(AppEvent.UpdatePort(it)) },
                    label = "Port Number",
                    placeholder = "e.g., 8080",
                    icon = Icons.Default.Power,
                    enabled = !isConnecting
                )

                // Error Message
                AnimatedVisibility(visible = state.errorMessage != null) {
                    Text(
                        text = state.errorMessage ?: "",
                        color = LtiTheme.colors.error,
                        style = LtiTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (isConnecting) {
                    Box(modifier = Modifier.fillMaxWidth().height(28.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = LtiTheme.colors.primary,
                            strokeWidth = 2.dp
                        )
                    }
                } else {
                    DesktopButton(
                        text = "Connect",
                        onClick = { onEvent(AppEvent.Connect) },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.AutoMirrored.Filled.ArrowForward
                    )
                }
            }

            // Status Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(LtiTheme.colors.border, LtiTheme.shapes.small)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Ready to connect",
                    color = LtiTheme.colors.textSecondary,
                    style = LtiTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ConnectTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label.uppercase(),
            style = LtiTheme.typography.labelSmall,
            color = LtiTheme.colors.textSecondary,
            letterSpacing = 0.5.sp
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = LtiTheme.colors.textSecondary.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            shape = LtiTheme.shapes.small,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LtiTheme.colors.textSecondary,
                    modifier = Modifier.size(18.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LtiTheme.colors.primary,
                unfocusedBorderColor = LtiTheme.colors.border,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = LtiTheme.colors.primary,
                focusedTextColor = LtiTheme.colors.textPrimary,
                unfocusedTextColor = LtiTheme.colors.textPrimary
            ),
            textStyle = LtiTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            singleLine = true
        )
    }
}
