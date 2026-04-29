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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.network.ConnectionState
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.ui.theme.LtiTheme

@Composable
fun ConnectScreen(
    state: AppState,
    onEvent: (AppEvent) -> Unit
) {
    val isConnecting = state.connectionState == ConnectionState.CONNECTING

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)), // Exact background from Connect.jsx
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(400.dp) // Match 400 width from Connect.jsx
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                // Logo Placeholder (would be logo-mark.svg)
                Icon(
                    imageVector = Icons.Default.Dns,
                    contentDescription = null,
                    tint = LtiTheme.colors.primary,
                    modifier = Modifier.size(40.dp)
                )
                
                Text(
                    text = "Connect to Console",
                    color = Color.White,
                    style = LtiTheme.typography.displayLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Enter your remote host details",
                    color = LtiTheme.colors.textSecondary,
                    style = LtiTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    textAlign = TextAlign.Center
                )
            }

            // Connection Form
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ConnectTextField(
                    value = state.host,
                    onValueChange = { onEvent(AppEvent.UpdateHost(it)) },
                    label = "Host",
                    placeholder = "hostname or IP",
                    icon = Icons.Default.Dns,
                    enabled = !isConnecting
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        ConnectTextField(
                            value = state.port,
                            onValueChange = { onEvent(AppEvent.UpdatePort(it)) },
                            label = "Port",
                            placeholder = "22",
                            icon = null,
                            enabled = !isConnecting
                        )
                    }
                    Box(modifier = Modifier.weight(2f)) {
                        ConnectTextField(
                            value = state.user,
                            onValueChange = { onEvent(AppEvent.UpdateUser(it)) },
                            label = "User",
                            placeholder = "root",
                            icon = null,
                            enabled = !isConnecting
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                DesktopButton(
                    text = if (isConnecting) "CONNECTING…" else "CONNECT",
                    onClick = { onEvent(AppEvent.Connect) },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    icon = if (isConnecting) null else Icons.AutoMirrored.Filled.ArrowForward,
                    isLoading = isConnecting
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
            }

            // Demo Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LtiTheme.colors.warning.copy(alpha = 0.06f), RoundedCornerShape(2.dp))
                    .border(1.dp, LtiTheme.colors.warning.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    null,
                    tint = LtiTheme.colors.warning,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Demo mode — any host/port will simulate a successful connection.",
                    color = LtiTheme.colors.textSecondary,
                    style = LtiTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp)
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
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
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
            shape = RoundedCornerShape(2.dp), // Radius 2 per design
            leadingIcon = if (icon != null) {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = LtiTheme.colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else null,
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
