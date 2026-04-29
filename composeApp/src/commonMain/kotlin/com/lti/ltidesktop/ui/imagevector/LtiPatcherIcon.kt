package com.lti.ltidesktop.ui.imagevector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val LtiPatcherIcon: ImageVector
    get() {
        if (_LtiPatcherIcon != null) {
            return _LtiPatcherIcon!!
        }
        _LtiPatcherIcon = ImageVector.Builder(
            name = "LtiPatcherIcon",
            defaultWidth = 108.dp,
            defaultHeight = 108.dp,
            viewportWidth = 108f,
            viewportHeight = 108f
        ).apply {
            // L trace
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(28f, 28f)
                lineTo(32f, 28f)
                lineTo(32f, 68f)
                lineTo(28f, 68f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(28f, 64f)
                lineTo(46f, 64f)
                lineTo(46f, 68f)
                lineTo(28f, 68f)
                close()
            }

            // T trace
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(48f, 28f)
                lineTo(72f, 28f)
                lineTo(72f, 32f)
                lineTo(48f, 32f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(58f, 28f)
                lineTo(62f, 28f)
                lineTo(62f, 68f)
                lineTo(58f, 68f)
                close()
            }

            // I trace
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(68f, 28f)
                lineTo(80f, 28f)
                lineTo(80f, 32f)
                lineTo(68f, 32f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(72f, 28f)
                lineTo(76f, 28f)
                lineTo(76f, 68f)
                lineTo(72f, 68f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(68f, 64f)
                lineTo(80f, 64f)
                lineTo(80f, 68f)
                lineTo(68f, 68f)
                close()
            }

            // Central via node
            path(fill = SolidColor(Color(0xFFF2994A))) {
                moveTo(52f, 46f)
                arcTo(6f, 6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 51.999f, 46f)
                close()
            }

            // Neon orange pulse dot
            path(fill = SolidColor(Color(0xFFF2994A))) {
                moveTo(79f, 29f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 78.999f, 29f)
                close()
            }

            // PCB trace horizontal
            path(
                fill = null,
                stroke = SolidColor(Color(0xFFF2994A)),
                strokeLineWidth = 1.5f
            ) {
                moveTo(58f, 48f)
                lineTo(80f, 48f)
            }

            // PCB trace vertical
            path(
                fill = null,
                stroke = SolidColor(Color(0xFFF2994A)),
                strokeLineWidth = 1.5f
            ) {
                moveTo(54f, 52f)
                lineTo(54f, 68f)
            }
        }.build()

        return _LtiPatcherIcon!!
    }

@Suppress("ObjectPropertyName")
private var _LtiPatcherIcon: ImageVector? = null
