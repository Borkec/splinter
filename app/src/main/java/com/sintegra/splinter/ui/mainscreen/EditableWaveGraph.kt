package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sintegra.splinter.model.WAVETABLE_SIZE
import java.util.UUID

@Composable
fun EditableWaveGraph(
    onWaveSave: () -> Unit,
    onSetCustomWave: (List<Float>) -> Unit,
    onStartSound: () -> Unit = {},
    onStopSound: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var mSize: Size? by remember { mutableStateOf(null) }
    var isSoundPlaying by remember { mutableStateOf(false) }
    var selectedPoint: EditableTouchInput? by remember { mutableStateOf(null) }
    var anchorPoints: List<EditableTouchInput> by remember(mSize) {
        mutableStateOf(
            mSize?.let {
                listOf(
                    EditableTouchInput(position = Offset(0f, it.height / 2)),
                    EditableTouchInput(position = Offset(it.width, it.height / 2))
                )
            } ?: emptyList()
        )
    }

    LaunchedEffect(anchorPoints) {
        onSetCustomWave(
            mSize?.let {
                generatePoints(anchorPoints, it)
            } ?: List(WAVETABLE_SIZE) { 0f }
        )
    }

    val waveLineColor = MaterialTheme.colors.primary
    val lineColor = MaterialTheme.colors.background

    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp, 50.dp)
                .padding(bottom = 12.dp)
                .background(if (isSoundPlaying) Color.Red else Color.Green, RoundedCornerShape(4.dp))
                .clickable {
                    when (isSoundPlaying) {
                        false -> onStopSound()
                        true -> onStartSound()
                    }
                    isSoundPlaying = !isSoundPlaying
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (!isSoundPlaying) "Play" else "Stop",
                color = Color.Black,
                fontSize = 12.sp
            )
        }

        Canvas(
            modifier = modifier
                .height(200.dp)
                .background(MaterialTheme.colors.onBackground)
                .border(width = 1.dp, color = MaterialTheme.colors.primary)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { point ->
                            val editableTouchInput = EditableTouchInput(position = point)
                            anchorPoints = (anchorPoints + editableTouchInput).sortedBy { it.position.x }
                            selectedPoint = editableTouchInput
                        },
                        onTap = { point ->
                            selectedPoint = findNearestPoint(anchorPoints, point)
                        }
                    )

                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (change.position.y < 0 || change.position.y > size.height || change.position.x < 0 || change.position.x > size.width) {
                            return@detectDragGestures
                        }

                        findNearestPoint(anchorPoints, change.position)?.let {
                            val newPoint = it.copy(position = change.position)
                            anchorPoints = (anchorPoints - it + newPoint).sortedBy { it.position.x }
                            selectedPoint = newPoint
                        }
                    }
                }
        ) {
            mSize = size

            drawLine(
                color = lineColor,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = 3f
            )

            drawPoints(
                points = anchorPoints.map { it.position },
                pointMode = PointMode.Polygon,
                color = waveLineColor,
                strokeWidth = 3f
            )

            for (anchorPoint in anchorPoints) {
                SplinterPointer(anchorPoint.position, showRing = anchorPoint == selectedPoint, color = waveLineColor)
            }
        }

        Box(
            modifier = Modifier
                .size(200.dp, 50.dp)
                .background(MaterialTheme.colors.primary, RoundedCornerShape(4.dp))
                .clickable {
                    onSetCustomWave(
                        mSize?.let {
                            generatePoints(anchorPoints, it)
                        } ?: List(WAVETABLE_SIZE) { 0f }
                    )
                    onWaveSave()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Save",
                color = MaterialTheme.colors.background,
                fontSize = 12.sp
            )
        }
    }

}

fun generatePoints(anchorPoints: List<EditableTouchInput>, canvasSize: Size): List<Float> {
    if (anchorPoints.size < 2) return emptyList()

    val deltaX: Float = canvasSize.width / WAVETABLE_SIZE

    val outPoints = mutableListOf<Float>()

    var anchorPointIdx = 1
    var first = anchorPoints[0]
    var second = anchorPoints[anchorPointIdx]
    var i = 0f

    while (i < canvasSize.width) {

        if (i >= second.position.x) {
            first = second.copy()
            second = anchorPoints[anchorPointIdx++]
        }

        // Calculate the slope between the two anchor points
        val slope = (second.position.y - first.position.y) / (second.position.x - first.position.x)
        val yIntercept = first.position.y - slope * first.position.x
        val y = slope * i + yIntercept


        if (!y.isNaN()) {
            outPoints.add(2 * (y - canvasSize.height / 2f) / canvasSize.height)
        }

        i += deltaX
    }

    return outPoints
}

fun findNearestPoint(anchorPoints: List<EditableTouchInput>, point: Offset): EditableTouchInput? {
    var nearestPoint: EditableTouchInput? = null

    for (anchorPoint in anchorPoints.drop(1).dropLast(1)) {
        nearestPoint = nearestPoint?.position?.squaredDistanceTo(point)?.let {
            if (anchorPoint.position.squaredDistanceTo(point) < it) {
                anchorPoint
            } else {
                nearestPoint
            }
        } ?: anchorPoint

    }

    if (nearestPoint != null && nearestPoint.position.squaredDistanceTo(point) > 30000) return null

    return nearestPoint
}

fun Offset.squaredDistanceTo(other: Offset): Float {
    val dx = this.x - other.x
    val dy = this.y - other.y
    return dx * dx + dy * dy
}

@Stable
data class EditableTouchInput(
    val id: String = UUID.randomUUID().toString(),
    val position: Offset
)

@Preview
@Composable
fun EditableWaveGraphPreview(modifier: Modifier = Modifier) {
    EditableWaveGraph({}, {}, {}, {}, Modifier)
}