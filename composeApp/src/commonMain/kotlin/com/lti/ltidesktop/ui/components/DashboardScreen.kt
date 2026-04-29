package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.AppState
import com.lti.ltidesktop.presentation.AppEvent
import com.lti.ltidesktop.presentation.ActionLog
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.TerminalTypography

@Composable
fun DashboardScreen(
    state: AppState,
    onEvent: (AppEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LtiTheme.colors.background)
            .padding(24.dp), // Match Dashboard.jsx padding
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Bento Status Row (4 columns)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusCard(
                label = "POWER",
                value = "ON",
                icon = Icons.Default.Power,
                toneColor = LtiTheme.colors.success,
                modifier = Modifier.weight(1f)
            )
            StatusCard(
                label = "HDD STATUS",
                value = "UNLOCKED",
                icon = Icons.Default.Lock,
                toneColor = LtiTheme.colors.success,
                modifier = Modifier.weight(1f)
            )
            StatusCard(
                label = "NETWORK",
                value = "ACTIVE",
                icon = Icons.Default.WifiTethering,
                toneColor = LtiTheme.colors.success,
                modifier = Modifier.weight(1f)
            )
            StatusCard(
                label = "UPTIME",
                value = "04:21:14",
                icon = Icons.Default.Timer,
                toneColor = LtiTheme.colors.info,
                isMono = true,
                modifier = Modifier.weight(1f)
            )
        }

        // Main Content Area (1.4fr : 1fr)
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // System Activity Card
            DashboardCard(
                title = "System Activity",
                subtitle = "${state.host}:${state.port}",
                modifier = Modifier.weight(1.4f)
            ) {
                SystemActivityChart()
            }

            // Recent Activity Card
            DashboardCard(
                title = "Recent Activity",
                rightContent = {
                    TextButton(
                        onClick = { /* View All */ },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "VIEW ALL",
                            style = LtiTheme.typography.labelSmall,
                            color = LtiTheme.colors.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                RecentActivityList(state.recentActions)
            }
        }
    }
}

@Composable
private fun StatusCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    toneColor: Color,
    isMono: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFF0F0F0F), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = label,
                style = LtiTheme.typography.labelSmall,
                color = LtiTheme.colors.textSecondary,
                letterSpacing = 0.5.sp
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = toneColor,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = value,
            style = LtiTheme.typography.displayLarge.copy(
                fontSize = 22.sp,
                color = toneColor,
                fontFamily = if (isMono) TerminalTypography.fontFamily else FontFamily.SansSerif,
                letterSpacing = if (isMono) 0.5.sp else 0.sp
            )
        )
    }
}

@Composable
private fun DashboardCard(
    title: String,
    subtitle: String? = null,
    rightContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(Color(0xFF0F0F0F), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 0.dp, color = Color.Transparent) // Border hack
                .drawBehind {
                    drawLine(
                        color = Color(0xFF1A1A1A),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = LtiTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                    color = Color.White
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = TerminalTypography.copy(fontSize = 11.sp),
                        color = Color(0xFF71717A)
                    )
                }
            }
            rightContent?.invoke()
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SystemActivityChart() {
    val points = listOf(40f, 35f, 50f, 42f, 60f, 55f, 72f, 65f, 78f, 70f, 85f, 80f, 92f, 88f, 76f, 82f, 70f, 78f, 88f, 95f, 82f, 75f, 68f, 80f)
    val primaryColor = LtiTheme.colors.primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val pad = 24.dp.toPx()
        val dx = (width - pad * 2) / (points.size - 1)
        val max = 100f

        val path = Path()
        points.forEachIndexed { i, p ->
            val x = pad + i * dx
            val y = height - pad - (p / max) * (height - pad * 2)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        // Fill area
        val fillPath = Path().apply {
            addPath(path)
            lineTo(width - pad, height - pad)
            lineTo(pad, height - pad)
            close()
        }

        // Draw grid lines
        for (i in 0..3) {
            val y = pad + i * (height - pad * 2) / 3
            drawLine(Color(0xFF1A1A1A), Offset(pad, y), Offset(width - pad, y), strokeWidth = 1.dp.toPx())
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(primaryColor.copy(alpha = 0.35f), Color.Transparent),
                startY = pad,
                endY = height - pad
            )
        )

        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Draw points every 4 steps
        points.forEachIndexed { i, p ->
            if (i % 4 == 0) {
                val x = pad + i * dx
                val y = height - pad - (p / max) * (height - pad * 2)
                drawCircle(primaryColor, radius = 2.5.dp.toPx(), center = Offset(x, y))
            }
        }
    }
}

@Composable
private fun RecentActivityList(logs: List<ActionLog>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        logs.take(5).forEachIndexed { i, log ->
            val color = when (log.tone) {
                "success" -> LtiTheme.colors.success
                "warn" -> LtiTheme.colors.warning
                "error" -> LtiTheme.colors.error
                else -> LtiTheme.colors.info
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .drawBehind {
                        if (i < 4) { // Only if not last
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
                        .background(color.copy(alpha = 0.1f), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when (log.title.lowercase()) {
                        "patch completed" -> Icons.Default.CloudSync
                        "user login" -> Icons.AutoMirrored.Filled.Login
                        "permission denied" -> Icons.Default.Warning
                        "build successful" -> Icons.Default.Build
                        "backup uploaded" -> Icons.Default.Cloud
                        else -> Icons.Default.Bolt
                    }
                    Icon(icon, null, tint = color, modifier = Modifier.size(14.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = log.title,
                        style = LtiTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 0.2.sp),
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
    }
}
