package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import com.lti.ltidesktop.ui.theme.LtiTheme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun DesktopButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPrimary: Boolean = true,
    isLoading: Boolean = false
) {
    var isHovered by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1.0f)
    
    val defaultBg = if (isPrimary) LtiTheme.colors.primary.copy(alpha = 0.1f) else LtiTheme.colors.surfaceVariant
    val hoverBg = if (isPrimary) LtiTheme.colors.primary.copy(alpha = 0.2f) else LtiTheme.colors.surfaceContainerHigh
    val contentColor = if (isPrimary) LtiTheme.colors.primary else LtiTheme.colors.textPrimary
    val borderColor = if (isPrimary) LtiTheme.colors.primary.copy(alpha = 0.5f) else LtiTheme.colors.border

    Row(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .height(28.dp)
            .clip(LtiTheme.shapes.small)
            .background(if (isHovered) hoverBg else defaultBg)
            .border(1.dp, borderColor, LtiTheme.shapes.small)
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
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(enabled = !isLoading, interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = contentColor,
                strokeWidth = 2.dp
            )
            Spacer(Modifier.width(8.dp))
        } else if (icon != null) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text = text,
            color = contentColor,
            style = LtiTheme.typography.bodyMedium.copy(fontSize = 12.sp)
        )
    }
}
