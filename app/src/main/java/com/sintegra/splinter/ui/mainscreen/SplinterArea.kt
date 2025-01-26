package com.sintegra.splinter.ui.mainscreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun SplinterArea(
    modifier: Modifier = Modifier,
    onPressed: () -> Unit,
    onHold: (Float, Float) -> Unit,
    onRelease: () -> Unit
) {
    val k: Int? = null
    var mid by remember { mutableStateOf(Offset(0f, 0f)) }

    val radiusAnimated = remember { Animatable(initialValue = 1f) }

    LaunchedEffect(mid) {
        radiusAnimated.snapTo(1f)
        radiusAnimated.animateTo(1000f, animationSpec = tween(2000))
    }

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(k) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    if (event.type == PointerEventType.Press) {
                        onPressed()
                    }
                    if (event.type == PointerEventType.Release) {
                        onRelease()
                    }

                    //                    points = event.changes.map { it.position to Animatable(100f) } + points
                    //                    if (points.size > CAP) points = points.dropLast(points.size - CAP)

                    mid = event.changes.first().position
                    onHold(mid.x, mid.y)
                }
            }
        }
    ) {

    }

}