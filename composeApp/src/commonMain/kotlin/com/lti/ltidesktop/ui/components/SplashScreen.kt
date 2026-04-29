package com.lti.ltidesktop.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import com.lti.ltidesktop.ui.imagevector.SplashIcon
import com.lti.ltidesktop.ui.imagevector.WordmarkLtipatcher
import kotlinx.coroutines.delay

// ─── Splash Screen ─────────────────────────────────────────────────────────────
// Spec: pure #000000 bg, 96×96 splash mark (logo-splash.svg), 240×48 wordmark,
// 180dp indeterminate progress bar, 2.5s total duration.
// Animation: 1200ms fade-in + spring(MediumBouncy, Low) scale 0.8→1.2.
// ───────────────────────────────────────────────────────────────────────────────

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var started by remember { mutableStateOf(false) }

    // Fade-in — 1200ms LinearOutSlowIn (spec: "1000ms tween fade-in")
    val alpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = LinearOutSlowInEasing),
        label = "splashAlpha"
    )

    // Scale spring — 0.8 → 1.2, MediumBouncy + Low stiffness (spec-exact)
    val scale by animateFloatAsState(
        targetValue = if (started) 1.2f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "splashScale"
    )

    // Glow radius — expands from near-0 to 1.5× icon radius over 1800ms
    val glowFactor by animateFloatAsState(
        targetValue = if (started) 1.5f else 0.05f,
        animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
        label = "splashGlow"
    )

    LaunchedEffect(Unit) {
        started = true
        delay(2500)
        onFinished()
    }

    // Root: pure black — no gradient, no texture (design-system spec)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ── Splash mark: 96×96 (logo-splash.svg) ──────────────────────────
            // chip with PCB pin traces, #4776E6 border + pins, #FFFFFF "LTI",
            // #F2994A orange via-dots — rendered from SplashIcon ImageVector
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(scale)
                    .alpha(alpha)
                    .drawBehind {
                        // Soft electric-blue glow halo behind the chip icon
                        val r = size.maxDimension * glowFactor
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4776E6).copy(alpha = 0.18f * alpha),
                                    Color(0xFF4776E6).copy(alpha = 0.06f * alpha),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = r
                            ),
                            radius = r,
                            center = center
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = SplashIcon,
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(96.dp)
                )
            }

            // ── Wordmark: 240×48 (logo-wordmark.svg) ──────────────────────────
            // "LTI" in #4776E6 electric-blue, "PATCHER" in #BDBDBD mid-grey,
            // vertical divider at 0.35α between the two halves
            Icon(
                imageVector = WordmarkLtipatcher,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .width(240.dp)
                    .height(48.dp)
                    .alpha(alpha)
            )

            // ── Indeterminate progress bar: 180×2dp ────────────────────────────
            // Track: #262626 at 40% α. Sweep: #00E5FF (primary cyan).
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(2.dp)
                    .alpha(alpha * 0.4f)
                    .background(Color(0xFF262626), CircleShape)
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "progress")
                val offset by infiniteTransition.animateFloat(
                    initialValue = -1f,
                    targetValue = 2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "progressOffset"
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.3f)
                        .offset(x = 180.dp * offset)
                        .background(Color(0xFF00E5FF), CircleShape)
                )
            }
        }
    }
}
