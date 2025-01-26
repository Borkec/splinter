package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun SplinterArea(
    modifier: Modifier = Modifier,
    onPressed: () -> Unit,
    onHold: (Float, Float) -> Unit,
    onRelease: () -> Unit
) {
    var mid by remember { mutableStateOf(Offset(0f, 0f)) }

    var points by remember { mutableStateOf<List<TouchPoint>>(emptyList()) }
    val fadeDuration = 100L

    LaunchedEffect(points) {
        while (true) {
            val currentTime = System.currentTimeMillis()
            points = points.mapNotNull { point ->
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


    Surface(Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when (event.type) {
                        PointerEventType.Press -> {
                            onPressed()
                        }

                        PointerEventType.Release -> {
                            onRelease()
                            mid = Offset(0f, 0f)
                        }

                        else -> {
                            mid = event.changes.first().position
                            onHold(mid.x, mid.y)
                        }
                    }

                }
            }

        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    val currentTime = System.currentTimeMillis()
                    points = points + TouchPoint(position = offset, timestamp = currentTime)
                },
                onDrag = { change, _ ->
                    val currentTime = System.currentTimeMillis()
                    points = points + TouchPoint(position = change.position, timestamp = currentTime)
                }
            )
        }) {
        Canvas(Modifier.graphicsLayer(renderEffect = BlurEffect(5f, 5f))) {
            for (i in 0 until points.size - 1) { // Stop at the second-to-last element
                val current = points[i]
                val next = points[i + 1]

                drawLine(
                    Color.LightGray,
                    current.position,
                    next.position,
                    strokeWidth = 20f * current.alpha,
                    cap = StrokeCap.Round,
                    alpha = current.alpha * 20f,
                    blendMode = BlendMode.Src,
                )
            }
        }

        Canvas(Modifier) {

            drawCircle(
                color = Color.White,
                radius = 20f,
                center = mid
            )

            drawCircle(
                color = Color.White,
                style = Stroke(5f),
                radius = 30f,
                center = mid
            )
        }
    }
}

data class TouchPoint(
    val id: String = UUID.randomUUID().toString(),
    val position: Offset,
    val timestamp: Long,
    var alpha: Float = 1f
)