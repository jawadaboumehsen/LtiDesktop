package com.lti.ltidesktop.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lti.ltidesktop.ui.imagevector.LtiPatcherIcon
import com.lti.ltidesktop.ui.imagevector.WordmarkLtipatcher
import com.lti.ltidesktop.ui.theme.LtiTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1.2f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500) // Show for 2.5 seconds
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LtiTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                imageVector = LtiPatcherIcon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(100.dp)
                    .scale(scaleAnim)
                    .alpha(alphaAnim)
            )

            Icon(
                imageVector = WordmarkLtipatcher,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .height(40.dp)
                    .alpha(alphaAnim)
            )
        }
    }
}
