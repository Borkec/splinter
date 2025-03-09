package com.sintegra.splinter.core.ui

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import com.sintegra.splinter.ui.mainscreen.TouchPoint

fun DrawScope.SplinterPointer(midPoint: Offset, showRing: Boolean = true, color: Color = Color.White) {
    drawCircle(
        color = color,
        radius = 20f,
        center = midPoint
    )

    if (showRing) {
        drawCircle(
            color = color,
            style = Stroke(5f),
            radius = 30f,
            center = midPoint
        )
    }
}

suspend fun PointerInputScope.getPointerInput(
    onHold: (Int, Float, Float) -> Unit = { _,  _, _ -> },
    onRelease: (Int) -> Unit = {}
) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()

            for(event in event.changes) {
                val position = event.position
                val pointerId = event.id.value.toInt()

                if(event.pressed) {
                    onHold(pointerId, position.x, position.y)
                } else {
                    onRelease(pointerId)
                }
            }
        }
    }
}