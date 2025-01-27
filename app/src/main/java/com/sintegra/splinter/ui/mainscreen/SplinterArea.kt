package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun SplinterArea(
    overlay: @Composable (Modifier) -> Unit,
    onPressed: () -> Unit,
    onHold: (Float, Float) -> Unit,
    onRelease: () -> Unit,
    modifier: Modifier = Modifier
) {
    val touchCoords: MutableState<Offset?> = remember { mutableStateOf(null) }
    val points: MutableState<List<TouchPoint>> = remember { mutableStateOf(emptyList()) }

    val fadeDuration = 100L

    LaunchedEffect(points) {
        while (true) {
            val currentTime = System.currentTimeMillis()
            points.value = points.value.mapNotNull { point ->
                val age = currentTime - point.timestamp
                if (age < fadeDuration) {
                    point.copy(alpha = 1f - (age.toFloat() / fadeDuration))
                } else {
                    null
                }
            }
            delay(16L)
        }
    }

    val pointerColor = MaterialTheme.colors.primary

    Surface(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                getPointerInput(touchCoords, onPressed, onHold, onRelease)
            }
            .pointerInput(Unit) {
                getDragInput(points)
            },
        color = MaterialTheme.colors.background
    ) {

        overlay(Modifier.zIndex(1f))

        Canvas(
            modifier = Modifier.graphicsLayer(renderEffect = BlurEffect(5f, 5f))
        ) {
            for (i in 0 until points.value.size - 1) {
                val current = points.value[i]
                val next = points.value[i + 1]

                drawLine(
                    pointerColor,
                    current.position,
                    next.position,
                    strokeWidth = 20f * current.alpha,
                    cap = StrokeCap.Round,
                    alpha = current.alpha * 20f,
                    blendMode = BlendMode.Src,
                )
            }
        }

        touchCoords.value?.let { midPoint ->
            Canvas(
                modifier = Modifier
            ) {
                SplinterPointer(
                    midPoint = midPoint,
                    color = pointerColor
                )
            }
        }
    }

}

suspend fun PointerInputScope.getPointerInput(
    touchCoords: MutableState<Offset?>,
    onPressed: () -> Unit,
    onHold: (Float, Float) -> Unit,
    onRelease: () -> Unit
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
                    touchCoords.value = null
                }
            }

            touchCoords.value = event.changes.first().position.apply { onHold(x, y) }
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

@Preview
@Composable
fun SplinterAreaPreview() {
    SplinterArea({}, {}, { _, _ -> }, {})
}

data class TouchPoint(
    val id: String = UUID.randomUUID().toString(),
    val position: Offset,
    val timestamp: Long,
    var alpha: Float = 1f
)