package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.presentation.AppEffect
import com.lti.ltidesktop.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommandInput(
    effect: Flow<AppEffect>,
    onEvent: (AppEvent) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    LaunchedEffect(effect) {
        effect.collectLatest { eff ->
            if (eff is AppEffect.UpdateInput) {
                text = eff.text
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LtiTheme.colors.terminalHeaderBackground) // border-t
            .background(LtiTheme.colors.terminalInputBackground)
            .padding(12.dp) // p-3
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "$",
                color = LtiTheme.colors.cmdPrompt,
                style = TerminalTypography.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 8.dp), // pl-2
            )

            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                        when (event.key) {
                            Key.Enter -> {
                                val cmd = text.trim()
                                if (cmd.isNotEmpty()) {
                                    onEvent(AppEvent.SendCommand(cmd))
                                    text = ""
                                }
                                true
                            }
                            Key.DirectionUp -> {
                                onEvent(AppEvent.HistoryUp(text))
                                true
                            }
                            Key.DirectionDown -> {
                                onEvent(AppEvent.HistoryDown(text))
                                true
                            }
                            Key.L -> {
                                if (event.isCtrlPressed || event.isMetaPressed) {
                                    onEvent(AppEvent.ClearOutput)
                                    true
                                } else false
                            }
                            else -> false
                        }
                    },
                singleLine = true,
                placeholder = {
                    Text(
                        "Type a command to execute...",
                        color = LtiTheme.colors.textSecondary,
                        style = TerminalTypography
                    )
                },
                textStyle = TerminalTypography.copy(color = LtiTheme.colors.textPrimary),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = LtiTheme.colors.primary,
                )
            )

            DesktopButton(
                text = "Execute",
                onClick = {
                    val cmd = text.trim()
                    if (cmd.isNotEmpty()) {
                        onEvent(AppEvent.SendCommand(cmd))
                        text = ""
                    }
                },
                icon = Icons.AutoMirrored.Filled.KeyboardReturn,
            )
        }
    }
}
