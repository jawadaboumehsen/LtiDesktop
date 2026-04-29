package com.lti.ltidesktop.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lti.ltidesktop.ui.theme.LtiTheme

@Composable
fun AnalyticsSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LtiTheme.colors.surfaceContainerLowest, LtiTheme.shapes.medium)
            .border(1.dp, LtiTheme.colors.border, LtiTheme.shapes.medium)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "System Performance",
                    color = LtiTheme.colors.textPrimary,
                    style = LtiTheme.typography.titleSmall
                )
                Text(
                    text = "Real-time network throughput and latency.",
                    color = LtiTheme.colors.textSecondary,
                    style = LtiTheme.typography.bodySmall
                )
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LegendItem("Throughput", LtiTheme.colors.primary)
                LegendItem("Latency", LtiTheme.colors.info)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            NetworkChart(
                points = listOf(0.2f, 0.4f, 0.35f, 0.6f, 0.45f, 0.8f, 0.7f, 0.9f, 0.65f, 0.5f, 0.4f, 0.6f),
                color = LtiTheme.colors.primary,
                modifier = Modifier.fillMaxSize()
            )
            NetworkChart(
                points = listOf(0.1f, 0.2f, 0.15f, 0.3f, 0.25f, 0.4f, 0.3f, 0.5f, 0.45f, 0.3f, 0.2f, 0.35f),
                color = LtiTheme.colors.info,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).background(color, LtiTheme.shapes.small))
        Text(text = label, color = LtiTheme.colors.textSecondary, style = LtiTheme.typography.labelSmall)
    }
}

@Composable
private fun NetworkChart(
    points: List<Float>,
    color: Color,
    modifier: Modifier = Modifier
) {
    val borderColor = LtiTheme.colors.border
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val spacing = width / (points.size - 1)
        
        val path = Path().apply {
            moveTo(0f, height - (points[0] * height))
            for (i in 1 until points.size) {
                lineTo(i * spacing, height - (points[i] * height))
            }
        }

        val fillPath = Path().apply {
            addPath(path)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(color.copy(alpha = 0.2f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2.dp.toPx())
        )
        
        // Draw grid lines
        val gridLines = 4
        for (i in 0..gridLines) {
            val y = i * (height / gridLines)
            drawLine(
                color = borderColor.copy(alpha = 0.2f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}
