package com.sintegra.splinter.ui.mainscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * State for screen cutouts in any direction. Keeps track of lines that separate every Splinter screen
 */

@Stable
class Cutouts(private val size: Size) {

    val coefs: MutableState<Pair<Float, Float>> = mutableStateOf(Pair(0f, 0f))

    val startPoint: MutableState<Offset?> = mutableStateOf(null)
    val endPoint: MutableState<Offset?> = mutableStateOf(null)
    val lineArgs: MutableState<Pair<Float, Float>?> = mutableStateOf(null)

    fun startSelection(startPoint: Offset) {
        this.startPoint.value = startPoint
    }

    fun endSelection(newEndPoint: Offset) {
        val currentStartPoint = startPoint.value ?: return
        if(calculateDistance(currentStartPoint, newEndPoint) < 10f) return

        this.endPoint.value = newEndPoint
        lineArgs.value = calculateLine(currentStartPoint, newEndPoint)
    }

    fun confirmSelection() {
        coefs.value = lineArgs.value ?: Pair(0f, 0f)
        startPoint.value = null
        endPoint.value = null
        lineArgs.value = null
    }

    fun getCurrentScreenSplitPoints(): Pair<Offset, Offset> {
        val currentLineArgs = lineArgs.value ?: Pair(0f, 0f)
        return startAndEndPointsForLine(coeffs = currentLineArgs)
    }

    private fun calculateLine(startPoint: Offset, endPoint: Offset): Pair<Float, Float> {
        val a = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x)
        val normalizedB = (startPoint.y-a*startPoint.x)
        return a to normalizedB
    }

    private fun calculateDistance(startPoint: Offset, endPoint: Offset) =
        sqrt((endPoint.x.toDouble() - startPoint.x).pow(2.0) + (endPoint.y.toDouble() - startPoint.y).pow(2.0))

    private fun startAndEndPointsForLine(coeffs: Pair<Float, Float>): Pair<Offset, Offset> {

        val bottomX = coeffs.second
        val topX = size.width*coeffs.first + coeffs.second

        val intersections = mutableListOf<Offset>()

        if(bottomX >= 0f && bottomX <= size.height) {
            intersections.add(Offset(0f, bottomX))
        }
        if(topX >= 0f && topX <= size.height) {
            intersections.add(Offset(size.width, topX))
        }
        if(abs(coeffs.first - 0.0f) > 1e-10) {
            val bottomY = -coeffs.second / coeffs.first
            if(bottomY >= 0f && bottomY <= size.width) {
                intersections.add(Offset(bottomY, 0f))
            }
        }
        if(abs(coeffs.first - 0.0f) > 1e-10) {
            val topY = (size.height-coeffs.second) / coeffs.first
            if(topY >= 0f && topY <= size.width) {
                intersections.add(Offset(topY, size.height))
            }
        }
        if (intersections.size != 2) throw IllegalStateException("Can't find two interceptions?")
        val firstPoint = intersections[0]
        val secondPoint = intersections[1]

        return firstPoint to secondPoint
    }
}

suspend fun PointerInputScope.listenForTouchInput(cutouts: Cutouts) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            val mid = event.changes.first().position
            if (event.type == PointerEventType.Press) {
                cutouts.startSelection(mid)
            }
            if (event.type == PointerEventType.Release) {
                cutouts.confirmSelection()
            }
            cutouts.endSelection(mid)
        }
    }
}

fun DrawScope.drawCutouts(cutouts: Cutouts, sizeState: MutableState<Size>) {
    sizeState.value = size

    cutouts.startPoint.value?.let { startPoint ->
        drawCircle(
            color = Color.White,
            radius = 30f,
            center = startPoint
        )

        cutouts.endPoint.value?.let { endPoint ->
            val backgroundLine = cutouts.getCurrentScreenSplitPoints()
            drawLine(
                start = backgroundLine.first,
                end = backgroundLine.second,
                color = Color.White,
                strokeWidth = 5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(100f, 20f))
            )
            drawLine(
                start = startPoint,
                end = endPoint,
                color = Color.White,
                strokeWidth = 10f
            )

            drawCircle(
                color = Color.White,
                radius = 30f,
                center = endPoint
            )
        }
    }
}