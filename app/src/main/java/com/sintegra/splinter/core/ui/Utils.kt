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
    onPressed: () -> Unit = {},
    onHold: (Float, Float) -> Unit = { _, _ -> },
    onRelease: () -> Unit = {},
    touchCoords: MutableState<Offset?>? = null,
    size: Size? = null
) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Press -> {
                    onPressed()
                }

                PointerEventType.Release -> {
                    onRelease()
                    touchCoords?.value = null
                }
            }
            val position = event.changes.first().position

            touchCoords?.value = position
            if (size != null) {
                onHold(position.x / size.width, position.y / size.width)
            }
        }
    }
}

suspend fun PointerInputScope.getDragInput(
    points: MutableState<List<TouchPoint>>,
) {
    detectDragGestures(
        onDragStart = { offset ->
            val currentTime = System.currentTimeMillis()
            points.value += TouchPoint(position = offset, timestamp = currentTime)
        },
        onDrag = { change, _ ->
            val currentTime = System.currentTimeMillis()
            points.value += TouchPoint(position = change.position, timestamp = currentTime)
        }
    )
}