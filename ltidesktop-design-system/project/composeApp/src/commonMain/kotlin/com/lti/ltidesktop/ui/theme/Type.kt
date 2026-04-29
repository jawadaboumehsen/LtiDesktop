package com.lti.ltidesktop.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LtiTypography = Typography(
    // display-lg: 20px, LineHeight 28px, 700
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 28.sp,
        letterSpacing = (-0.4).sp
    ),
    // headline-md: 16px, LineHeight 24px, 600
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        letterSpacing = (-0.16).sp
    ),
    // title-sm: 14px, LineHeight 20px, 600
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp
    ),
    // body-base: 13px, LineHeight 18px, 400
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    // body-sm: 12px, LineHeight 16px, 400
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp
    ),
    // label-uppercase: 11px, LineHeight 14px, 600
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)

val TerminalTypography = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontSize = 13.sp,
    fontWeight = FontWeight.Medium, // 450 weight is roughly Medium (500) or Normal (400)
    lineHeight = 20.sp
)
