package com.sintegra.splinter.ui.mainscreen

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.SplinterPointer(midPoint: Offset, showRing: Boolean = true, modifier: Modifier = Modifier) {
        drawCircle(
            color = Color.White,
            radius = 20f,
            center = midPoint
        )

        if(showRing) {
            drawCircle(
                color = Color.White,
                style = Stroke(5f),
                radius = 30f,
                center = midPoint
            )
        }
}
