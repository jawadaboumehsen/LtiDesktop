cat << 'INNER_EOF' > composeApp/src/commonMain/kotlin/com/lti/ltidesktop/ui/components/ConsoleScreen.kt
package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.AppEffect
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.TerminalTypography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun ConsoleScreen(
    state: AppState,
    effectFlow: Flow<AppEffect>,
    onEvent: (AppEvent) -> Unit
) {
    var cmdInput by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is AppEffect.ScrollToBottom -> {
                    if (state.outputLines.isNotEmpty()) {
                        listState.animateScrollToItem(state.outputLines.size - 1)
                    }
                }
                is AppEffect.UpdateInput -> {
                    cmdInput = effect.text
                }
                is AppEffect.ShowToast -> {
                    // Ignored for now or implement snackbar
                }
            }
        }
    }

    // Auto-scroll to bottom fallback
    LaunchedEffect(state.outputLines.size) {
        if (state.outputLines.isNotEmpty()) {
            listState.animateScrollToItem(state.outputLines.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LtiTheme.colors.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Terminal Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF0F0F0F), RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Color(0xFF1A1A1A),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(modifier = Modifier.size(8.dp).background(LtiTheme.colors.success, CircleShape))
                    Text(
                        text = "${state.user}@${state.host}:~ — bash",
                        style = TerminalTypography.copy(fontSize = 11.sp, color = Color(0xFF71717A), letterSpacing = 0.5.sp)
                    )
                }
                Text(
                    text = "↑/↓ history · ⌃L clear",
                    style = TerminalTypography.copy(fontSize = 11.sp, color = Color(0xFF71717A), letterSpacing = 0.5.sp)
                )
            }

            // Terminal Output
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) { focusRequester.requestFocus() }
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.outputLines) { line ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Tag
                            Text(
                                text = line.tag,
                                modifier = Modifier.width(56.dp),
                                style = TerminalTypography.copy(
                                    fontSize = 13.sp,
                                    color = line.displayColor,
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                            // Message
                            Text(
                                text = line.text,
                                modifier = Modifier.weight(1f),
                                style = TerminalTypography.copy(
                                    fontSize = 13.sp,
                                    color = if (line.isEcho) Color.White else Color(0xFFE4E4E7)
                                )
                            )
                        }
                    }
                }
            }

            // Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Color(0xFF1A1A1A),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${state.user}@node-01 ~ $",
                    style = TerminalTypography.copy(fontSize = 13.sp, color = LtiTheme.colors.primary, fontWeight = FontWeight.Bold)
                )
                BasicTextField(
                    value = cmdInput,
                    onValueChange = { cmdInput = it },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onKeyEvent {
                            if (it.type == KeyEventType.KeyDown) {
                                when (it.key) {
                                    Key.Enter -> {
                                        if (cmdInput.isNotEmpty()) {
                                            onEvent(AppEvent.SendCommand(cmdInput))
                                            cmdInput = ""
                                        }
                                        true
                                    }
                                    Key.DirectionUp -> {
                                        onEvent(AppEvent.HistoryUp(cmdInput))
                                        true
                                    }
                                    Key.DirectionDown -> {
                                        onEvent(AppEvent.HistoryDown(cmdInput))
                                        true
                                    }
                                    Key.L -> {
                                        if (it.isCtrlPressed) {
                                            onEvent(AppEvent.ClearOutput)
                                            true
                                        } else false
                                    }
                                    else -> false
                                }
                            } else false
                        },
                    textStyle = TerminalTypography.copy(color = Color.White, fontSize = 13.sp),
                    cursorBrush = SolidColor(LtiTheme.colors.primary),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    decorationBox = { innerTextField ->
                        if (cmdInput.isEmpty()) {
                            Text(
                                "type a command (try: ls, status, patch, clear)",
                                style = TerminalTypography.copy(fontSize = 13.sp, color = Color(0xFF3F3F46))
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }
    }
}

// Map OutputLine to Terminal UI tokens
private val com.lti.ltidesktop.OutputLine.tag: String get() = when (type) {
    com.lti.ltidesktop.LineType.CMD -> "$"
    com.lti.ltidesktop.LineType.ERROR -> "ERR"
    com.lti.ltidesktop.LineType.SYS -> "SYS"
    com.lti.ltidesktop.LineType.WARN -> "WARN"
    else -> "INFO"
}

private val com.lti.ltidesktop.OutputLine.isEcho: Boolean get() = type == com.lti.ltidesktop.LineType.CMD

private val com.lti.ltidesktop.OutputLine.displayColor: Color get() = when (type) {
    com.lti.ltidesktop.LineType.ERROR -> Color(0xFFFF3366)
    com.lti.ltidesktop.LineType.WARN -> Color(0xFFFFD600)
    com.lti.ltidesktop.LineType.SYS -> Color(0xFFA1A1AA)
    com.lti.ltidesktop.LineType.CMD -> Color(0xFF00E5FF)
    else -> Color(0xFF00E5FF) // INFO
}
INNER_EOF
