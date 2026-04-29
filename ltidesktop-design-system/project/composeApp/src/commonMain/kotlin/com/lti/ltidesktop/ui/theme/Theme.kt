package com.lti.ltidesktop.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val NexusShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(4.dp)
)

@Composable
fun LtiTheme(
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    val ltiColors = if (isDark) DarkColors else LightColors

    val materialColorScheme = if (isDark) {
        darkColorScheme(
            primary = ltiColors.primary,
            onPrimary = Color.White,
            primaryContainer = ltiColors.primaryContainer,
            background = ltiColors.background,
            surface = ltiColors.surface,
            surfaceVariant = ltiColors.surfaceVariant,
            outline = ltiColors.border,
            onBackground = ltiColors.textPrimary,
            onSurface = ltiColors.textPrimary,
            error = ltiColors.error
        )
    } else {
        lightColorScheme(
            primary = ltiColors.primary,
            onPrimary = Color.White,
            primaryContainer = ltiColors.primaryContainer,
            background = ltiColors.background,
            surface = ltiColors.surface,
            surfaceVariant = ltiColors.surfaceVariant,
            outline = ltiColors.border,
            onBackground = ltiColors.textPrimary,
            onSurface = ltiColors.textPrimary,
            error = ltiColors.error
        )
    }

    CompositionLocalProvider(LocalLtiColors provides ltiColors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            shapes = NexusShapes,
            typography = LtiTypography,
            content = content
        )
    }
}

object LtiTheme {
    val colors: LtiColors
        @Composable
        get() = LocalLtiColors.current
    val typography: androidx.compose.material3.Typography
        @Composable
        get() = MaterialTheme.typography
    val shapes: androidx.compose.material3.Shapes
        @Composable
        get() = MaterialTheme.shapes
}
