package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.network.ConnectionState
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.presentation.AppEffect
import com.lti.ltidesktop.presentation.Screen
import com.lti.ltidesktop.ui.theme.LtiTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun ConsoleScreen(
    state: AppState,
    effect: Flow<AppEffect>,
    onEvent: (AppEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LtiTheme.colors.background),
        ) {
            OutputPanel(
                lines = state.outputLines,
                effect = effect,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            )
            CommandInput(
                effect = effect,
                onEvent = onEvent
            )
        }

        if (state.connectionState != ConnectionState.CONNECTED) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LtiTheme.colors.background.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .width(400.dp)
                        .background(LtiTheme.colors.surface, LtiTheme.shapes.medium)
                        .border(1.dp, LtiTheme.colors.border, LtiTheme.shapes.medium)
                        .padding(48.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(LtiTheme.colors.error.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerOff,
                            contentDescription = null,
                            tint = LtiTheme.colors.error,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Connection Required",
                            color = LtiTheme.colors.textPrimary,
                            style = LtiTheme.typography.displayLarge
                        )
                        Text(
                            "You must establish a secure link to the server before using the remote console.",
                            color = LtiTheme.colors.textSecondary,
                            style = LtiTheme.typography.bodyMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    DesktopButton(
                        text = "Establish Connection",
                        onClick = { onEvent(AppEvent.NavigateTo(Screen.SETTINGS)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
