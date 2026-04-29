package com.lti.ltidesktop.ui.components.widgets

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.presentation.ActionLog
import com.lti.ltidesktop.ui.theme.LtiTheme
import com.lti.ltidesktop.ui.theme.TerminalTypography

/**
 * StatusChip: 6dp color-coded dot + text in a semantic 10% alpha container.
 */
@Composable
fun StatusChip(
    text: String,
    tone: String = "info"
) {
    val colors = LtiTheme.colors
    val (bg, border, contentColor, dotColor) = when (tone) {
        "success" -> Quad(colors.success.copy(alpha = 0.1f), colors.success.copy(alpha = 0.2f), colors.success, colors.success)
        "warn" -> Quad(colors.warning.copy(alpha = 0.1f), colors.warning.copy(alpha = 0.2f), colors.warning, colors.warning)
        "error" -> Quad(colors.error.copy(alpha = 0.1f), colors.error.copy(alpha = 0.2f), colors.error, colors.error)
        else -> Quad(colors.info.copy(alpha = 0.1f), colors.info.copy(alpha = 0.2f), colors.info, colors.info)
    }

    Row(
        modifier = Modifier
            .height(24.dp)
            .background(bg, RoundedCornerShape(4.dp))
            .border(1.dp, border, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(modifier = Modifier.size(6.dp).background(dotColor))
        Text(
            text = text.uppercase(),
            color = contentColor,
            style = LtiTheme.typography.labelSmall
        )
    }
}

/**
 * ActionTile: Premium action card matching Home.jsx exactly.
 */
@Composable
fun ActionTile(
    title: String,
    desc: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    isDone: Boolean = false,
    isDanger: Boolean = false,
    disabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f)
    val accent = if (isDanger) LtiTheme.colors.error else LtiTheme.colors.primary

    val bgColor = when {
        disabled -> Color(0xFF0F0F0F)
        isHovered -> Color(0xFF161616)
        else -> Color(0xFF0F0F0F)
    }

    val borderColor = when {
        disabled -> Color(0xFF1A1A1A)
        isHovered -> Color(0xFF262626)
        else -> Color(0xFF1A1A1A)
    }

    Surface(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(4.dp))
            .alpha(if (disabled) 0.4f else 1.0f)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Enter -> if (!disabled) isHovered = true
                            PointerEventType.Exit -> isHovered = false
                            PointerEventType.Press -> if (!disabled) isPressed = true
                            PointerEventType.Release -> isPressed = false
                        }
                    }
                }
            }
            .clickable(enabled = !disabled && !isLoading) { onClick() },
        color = bgColor,
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top: Icon + Optional Done Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(accent.copy(alpha = 0.1f), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = accent,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(imageVector = icon, null, tint = accent, modifier = Modifier.size(16.dp))
                    }
                }

                if (isDone) {
                    StatusChip(text = "DONE", tone = "success")
                }
            }

            // Middle: Text
            Column {
                Text(
                    text = title,
                    style = LtiTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.2.sp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = LtiTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                    color = LtiTheme.colors.textSecondary
                )
            }

            // Bottom: Status + Arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        isLoading -> "RUNNING…"
                        isDanger -> "DESTRUCTIVE"
                        else -> "READY"
                    },
                    style = TerminalTypography.copy(fontSize = 10.sp, letterSpacing = 0.5.sp),
                    color = Color(0xFF71717A)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * FileRow: List item for the file manager with extension-based coloring and hover states.
 */
@Composable
fun FileRow(
    name: String,
    ext: String,
    size: String,
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        when {
            isSelected -> LtiTheme.colors.primary.copy(alpha = 0.15f)
            isHovered -> Color(0xFF161616).copy(alpha = 0.5f)
            else -> Color.Transparent
        }
    )

    val iconColor = when (ext.lowercase()) {
        "hpp", "cpp", "h", "c" -> Color(0xFF4776E6) // Electric Blue
        "json" -> Color(0xFFFFD600) // Warning Yellow
        "txt", "log" -> Color(0xFFA1A1AA) // Grey
        else -> LtiTheme.colors.primary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Enter -> isHovered = true
                            PointerEventType.Exit -> isHovered = false
                        }
                    }
                }
            }
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Extension Icon
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (ext.lowercase()) {
                    "hpp", "cpp", "h", "c" -> Icons.Default.Code
                    "json" -> Icons.Default.SettingsApplications
                    "txt", "log" -> Icons.Default.Description
                    else -> Icons.AutoMirrored.Filled.InsertDriveFile
                },
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = LtiTheme.typography.bodyMedium,
                color = if (isHovered || isSelected) Color.White else LtiTheme.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$size • $time",
                style = LtiTheme.typography.bodySmall,
                color = LtiTheme.colors.textSecondary
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = LtiTheme.colors.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * ActivityLogItem: Clean log entries using middle dot (•) separators and semantic icon-chip glyphs.
 */
@Composable
fun ActivityLogItem(
    log: ActionLog,
    modifier: Modifier = Modifier
) {
    val colors = LtiTheme.colors
    val (iconBg, iconTint) = when (log.tone) {
        "success" -> colors.success.copy(alpha = 0.1f) to colors.success
        "warn" -> colors.warning.copy(alpha = 0.1f) to colors.warning
        "error" -> colors.error.copy(alpha = 0.1f) to colors.error
        else -> colors.info.copy(alpha = 0.1f) to colors.info
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icon Box
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(iconBg, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            val icon = when (log.title.lowercase()) {
                "login" -> Icons.AutoMirrored.Filled.Login
                "sync" -> Icons.Default.CloudDownload
                "warning" -> Icons.Default.Warning
                else -> Icons.Default.Bolt
            }
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = log.title,
                    color = colors.textPrimary,
                    style = LtiTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = log.meta,
                    color = colors.textSecondary,
                    style = LtiTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Time
        Text(
            text = log.time,
            color = colors.textSecondary,
            style = LtiTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
        )
    }
}

/**
 * AntigravitySwitch: Custom toggle matching the design system.
 */
@Composable
fun AntigravitySwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackColor by animateColorAsState(if (checked) LtiTheme.colors.primary.copy(alpha = 0.2f) else Color(0xFF1A1A1A))
    val thumbColor by animateColorAsState(if (checked) LtiTheme.colors.primary else Color(0xFF71717A))
    val thumbOffset by animateDpAsState(if (checked) 18.dp else 2.dp)

    Box(
        modifier = modifier
            .width(36.dp)
            .height(20.dp)
            .clip(CircleShape)
            .background(trackColor)
            .clickable { onCheckedChange(!checked) }
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(16.dp)
                .background(thumbColor, CircleShape)
        )
    }
}

/**
 * AntigravitySlider: Square-thumb slider for numeric inputs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntigravitySlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    modifier: Modifier = Modifier
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        modifier = modifier.height(12.dp), // Reduced height
        colors = SliderDefaults.colors(
            thumbColor = LtiTheme.colors.primary,
            activeTrackColor = LtiTheme.colors.primary,
            inactiveTrackColor = Color(0xFF1A1A1A)
        ),
        track = { sliderState ->
            SliderDefaults.Track(
                sliderState = sliderState,
                modifier = Modifier.height(2.dp), // Thin premium track
                colors = SliderDefaults.colors(
                    activeTrackColor = LtiTheme.colors.primary,
                    inactiveTrackColor = Color(0xFF1A1A1A)
                ),
                thumbTrackGapSize = 0.dp
            )
        },
        thumb = {
            Box(
                modifier = Modifier
                    .size(10.dp) // Slightly smaller thumb
                    .background(LtiTheme.colors.primary, RoundedCornerShape(1.dp))
                    .border(1.dp, Color.Black.copy(alpha = 0.5f), RoundedCornerShape(1.dp))
            )
        }
    )
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
