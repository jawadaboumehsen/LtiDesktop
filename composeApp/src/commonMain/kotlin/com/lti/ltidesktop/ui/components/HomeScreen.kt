package com.lti.ltidesktop.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.presentation.ActionLog
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.TerminalTypography
import com.lti.ltidesktop.ui.components.widgets.ActionTile
import com.lti.ltidesktop.ui.components.widgets.ActivityLogItem
import com.lti.ltidesktop.ui.components.widgets.StatusChip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    state: AppState,
    onEvent: (AppEvent) -> Unit
) {
    val colors = LtiTheme.colors
    val scope = rememberCoroutineScope()

    // Local state for action simulation (matching Home.jsx behavior)
    var busyActionKey by remember { mutableStateOf<String?>(null) }
    var completedActions by remember { mutableStateOf(setOf<String>()) }

    // Reset "Done" state after 2.4s per Home.jsx spec
    LaunchedEffect(completedActions) {
        if (completedActions.isNotEmpty()) {
            delay(2400)
            completedActions = emptySet()
        }
    }

    val triggerAction = { key: String, ms: Long ->
        if (busyActionKey == null) {
            scope.launch {
                busyActionKey = key
                delay(ms)
                busyActionKey = null
                completedActions = completedActions + key
                onEvent(AppEvent.ExecuteAction(key))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Banner: Connected Host
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F0F0F), RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "CONNECTED HOST",
                        style = LtiTheme.typography.labelSmall,
                        color = colors.textSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color.White)) {
                                append("${state.user}@${state.host}")
                            }
                            withStyle(SpanStyle(color = Color(0xFF71717A))) {
                                append(":${state.port}")
                            }
                        },
                        style = TerminalTypography.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                StatusChip(text = "Session Active", tone = "success")
            }
        }

        // Quick Actions Grid
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "QUICK ACTIONS",
                style = LtiTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.textSecondary,
                letterSpacing = 0.5.sp
            )

            val actions = listOf(
                ActionData("dump", Icons.Default.Download, "Get Dump Files", "Pull crash + memory dumps from the host", 1800),
                ActionData("reconnect", Icons.Default.Cable, "Reconnect Session", "Re-establish the connection", 1200),
                ActionData("memdump", Icons.Default.Memory, "Capture Memory Snapshot", "Dump RAM regions to .hpp", 2000),
                ActionData("logs", Icons.Default.Description, "Sync Logs", "Pull /var/log/* to local store", 1500),
                ActionData("diag", Icons.Default.BugReport, "Run Diagnostics", "Smoke-test the patcher pipeline", 1700),
            )

            // Row 1: first 3 tiles
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                actions.take(3).forEach { a ->
                    ActionTile(
                        title = a.title,
                        desc = a.desc,
                        icon = a.icon,
                        onClick = { triggerAction(a.key, a.ms) },
                        isLoading = busyActionKey == a.key,
                        isDone = completedActions.contains(a.key),
                        isDanger = a.isDanger,
                        disabled = busyActionKey != null && busyActionKey != a.key,
                        modifier = Modifier.weight(1f).height(140.dp)
                    )
                }
            }
            // Row 2: remaining 2 tiles (padded to match width)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                actions.drop(3).forEach { a ->
                    ActionTile(
                        title = a.title,
                        desc = a.desc,
                        icon = a.icon,
                        onClick = { triggerAction(a.key, a.ms) },
                        isLoading = busyActionKey == a.key,
                        isDone = completedActions.contains(a.key),
                        isDanger = a.isDanger,
                        disabled = busyActionKey != null && busyActionKey != a.key,
                        modifier = Modifier.weight(1f).height(140.dp)
                    )
                }
                // Empty spacer to keep left-alignment
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // Recent Actions Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Actions",
                    style = LtiTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                    color = Color.White
                )
                Text(
                    text = "last 24h",
                    style = TerminalTypography.copy(fontSize = 11.sp),
                    color = Color(0xFF71717A)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                state.recentActions.take(4).forEachIndexed { i, log ->
                    RecentActionRow(log, isLast = i == state.recentActions.take(4).size - 1)
                }
            }
        }
    }
}

@Composable
private fun RecentActionRow(log: ActionLog, isLast: Boolean) {
    val colors = LtiTheme.colors
    val toneColor = when (log.tone) {
        "success" -> colors.success
        "warn" -> colors.warning
        "error" -> colors.error
        else -> colors.info
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .drawBehind {
                if (!isLast) {
                    drawLine(
                        color = Color(0xFF1A1A1A),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(toneColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            val icon = when (log.key) {
                "dump" -> Icons.Default.Download
                "logs" -> Icons.Default.Description
                "reconnect" -> Icons.Default.Cable
                "diag" -> Icons.Default.BugReport
                else -> Icons.Default.Info
            }
            Icon(icon, null, tint = toneColor, modifier = Modifier.size(14.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = log.title.uppercase(),
                style = LtiTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 0.5.sp),
                color = Color.White
            )
            Text(
                text = log.meta,
                style = TerminalTypography.copy(fontSize = 11.sp),
                color = Color(0xFFA1A1AA)
            )
        }

        Text(
            text = log.time,
            style = TerminalTypography.copy(fontSize = 10.sp),
            color = Color(0xFF71717A)
        )
    }
}

private data class ActionData(
    val key: String,
    val icon: ImageVector,
    val title: String,
    val desc: String,
    val ms: Long,
    val isDanger: Boolean = false
)

// Add key to ActionLog to support icon mapping
private val ActionLog.key: String get() = when {
    title.contains("dump", ignoreCase = true) -> "dump"
    title.contains("log", ignoreCase = true) -> "logs"
    title.contains("reconnect", ignoreCase = true) -> "reconnect"
    title.contains("diag", ignoreCase = true) -> "diag"
    else -> "info"
}
