package com.lti.ltidesktop.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class LtiColors(
    val background: Color,
    val surface: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceVariant: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val primary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val surfaceTint: Color,
    val error: Color,
    val success: Color,
    val warning: Color,
    val info: Color,
    
    // Sidebar specific
    val sidebarBackground: Color,
    val sidebarBorder: Color,
    val sidebarItemActive: Color,
    
    // Terminal specific
    val terminalBackground: Color,
    val terminalHeaderBackground: Color,
    val terminalInputBackground: Color,
    val cmdPrompt: Color,
    val cmdError: Color,
    val cmdSys: Color,
    val cmdWarn: Color,
    val cmdNormal: Color
)

val DarkColors = LtiColors(
    background = Color(0xFF000000), // Deep space black
    surface = Color(0xFF0C0C0C),    // Very dark grey
    surfaceContainerLowest = Color(0xFF0C0C0C), // Slightly lighter than background
    surfaceContainer = Color(0xFF161616),
    surfaceContainerHigh = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF161616),
    border = Color(0xFF262626),
    textPrimary = Color(0xFFFFFFFF), // Pure white
    textSecondary = Color(0xFFA1A1AA),
    primary = Color(0xFF00E5FF), // Antigravity neon cyan
    primaryContainer = Color(0xFF00333D), 
    onPrimaryContainer = Color(0xFFE0FFFF),
    surfaceTint = Color(0xFF00B3CC),
    error = Color(0xFFFF3366), // Neon red/pink
    success = Color(0xFF00FFAA), // Neon mint/green
    warning = Color(0xFFFFD600), // Neon yellow
    info = Color(0xFF00E5FF),
    
    sidebarBackground = Color(0xFF0C0C0C),
    sidebarBorder = Color(0xFF1A1A1A),
    sidebarItemActive = Color(0xFF121212),
    
    terminalBackground = Color(0xFF000000),
    terminalHeaderBackground = Color(0xFF0A0A0A),
    terminalInputBackground = Color(0xFF000000),
    cmdPrompt = Color(0xFF00FFAA),
    cmdError = Color(0xFFFF3366),
    cmdSys = Color(0xFF00E5FF),
    cmdWarn = Color(0xFFFFD600),
    cmdNormal = Color(0xFFE4E4E7)
)

val LightColors = LtiColors(
    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainer = Color(0xFFF4F4F5),
    surfaceContainerHigh = Color(0xFFE4E4E7),
    surfaceVariant = Color(0xFFF4F4F5),
    border = Color(0xFFE4E4E7),
    textPrimary = Color(0xFF09090B),
    textSecondary = Color(0xFF71717A),
    primary = Color(0xFF00A3C4), // Deeper cyan for light mode contrast
    primaryContainer = Color(0xFFCCF6FF),
    onPrimaryContainer = Color(0xFF003D4A),
    surfaceTint = Color(0xFF00A3C4),
    error = Color(0xFFDC2626),
    success = Color(0xFF059669),
    warning = Color(0xFFD97706),
    info = Color(0xFF0284C7),
    
    sidebarBackground = Color(0xFF000000), // Keep sidebar dark for pro look
    sidebarBorder = Color(0xFF1A1A1A),
    sidebarItemActive = Color(0xFF121212),
    
    terminalBackground = Color(0xFF000000), // Terminal stays dark
    terminalHeaderBackground = Color(0xFF0A0A0A),
    terminalInputBackground = Color(0xFF000000),
    cmdPrompt = Color(0xFF00FFAA),
    cmdError = Color(0xFFFF3366),
    cmdSys = Color(0xFF00E5FF),
    cmdWarn = Color(0xFFFFD600),
    cmdNormal = Color(0xFFE4E4E7)
)

val LocalLtiColors = staticCompositionLocalOf { LightColors }
