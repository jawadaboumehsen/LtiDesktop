package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.LineType
import com.lti.ltidesktop.OutputLine
import com.lti.ltidesktop.presentation.AppEffect
import com.lti.ltidesktop.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OutputPanel(
    lines: List<OutputLine>,
    effect: Flow<AppEffect>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new lines arrive
    LaunchedEffect(lines.size) {
        if (lines.isNotEmpty()) {
            listState.animateScrollToItem(lines.size - 1)
        }
    }

    // Handle explicit ScrollToBottom effect
    LaunchedEffect(effect) {
        effect.collectLatest { eff ->
            if (eff is AppEffect.ScrollToBottom && lines.isNotEmpty()) {
                listState.animateScrollToItem(lines.size - 1)
            }
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .clip(LtiTheme.shapes.medium)
            .background(LtiTheme.colors.terminalBackground)
            .border(1.dp, LtiTheme.colors.border, LtiTheme.shapes.medium)
    ) {
        // Terminal Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp) // h-8
                .background(LtiTheme.colors.terminalHeaderBackground)
                .border(1.dp, LtiTheme.colors.border)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(12.dp).background(LtiTheme.colors.surfaceContainerHigh, RoundedCornerShape(6.dp)))
                Box(modifier = Modifier.size(12.dp).background(LtiTheme.colors.surfaceContainerHigh, RoundedCornerShape(6.dp)))
                Box(modifier = Modifier.size(12.dp).background(LtiTheme.colors.surfaceContainerHigh, RoundedCornerShape(6.dp)))
            }

            Row(
                modifier = Modifier.padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.Lock, null, tint = LtiTheme.colors.textSecondary, modifier = Modifier.size(14.dp))
                Text(
                    text = "root@corp-sys-prod-01:~",
                    color = LtiTheme.colors.textSecondary,
                    style = TerminalTypography.copy(fontSize = 11.sp)
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            SelectionContainer {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(lines) { line ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "[${line.type.name}]",
                                color = lineColor(line.type).copy(alpha = 0.5f),
                                style = TerminalTypography.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = line.text,
                                color = lineColor(line.type),
                                style = TerminalTypography
                            )
                        }
                    }
                }
            }

            var isScrollbarHovered by remember { mutableStateOf(false) }
            val thickness by androidx.compose.animation.core.animateDpAsState(targetValue = if (isScrollbarHovered) 8.dp else 4.dp)

            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                when (event.type) {
                                    androidx.compose.ui.input.pointer.PointerEventType.Enter -> isScrollbarHovered = true
                                    androidx.compose.ui.input.pointer.PointerEventType.Exit -> isScrollbarHovered = false
                                }
                            }
                        }
                    },
                adapter = rememberScrollbarAdapter(
                    scrollState = listState
                ),
                style = ScrollbarStyle(
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

@Composable
private fun lineColor(type: LineType): Color = when (type) {
    LineType.CMD    -> LtiTheme.colors.cmdPrompt
    LineType.ERROR  -> LtiTheme.colors.cmdError
    LineType.SYS    -> LtiTheme.colors.cmdSys
    LineType.WARN   -> LtiTheme.colors.cmdWarn
    LineType.NORMAL -> LtiTheme.colors.cmdNormal
}
