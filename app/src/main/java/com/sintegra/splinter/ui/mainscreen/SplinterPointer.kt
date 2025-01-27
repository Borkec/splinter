package com.sintegra.splinter.ui.mainscreen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.SplinterPointer(midPoint: Offset, showRing: Boolean = true, color: Color = Color.White) {
        drawCircle(
            color = color,
            radius = 20f,
            center = midPoint
        )

        if(showRing) {
            drawCircle(
                color = color,
                style = Stroke(5f),
                radius = 30f,
                center = midPoint
            )
        }
}
