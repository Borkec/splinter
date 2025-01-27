package com.sintegra.splinter.ui.mainscreen

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import java.util.UUID
import kotlin.math.abs

@Composable
fun EditableWaveGraph(
    points: List<Float>,
    onCustomWaveSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mSize: Size? by remember { mutableStateOf(null) }

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

    var selectedPoint: EditableTouchInput? by remember {
        mutableStateOf(null)
    }

    Column(modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Canvas(
            modifier = modifier
                .height(200.dp)
                .background(Color.DarkGray)
                .border(width = 1.dp, color = Color.White)
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
                        findNearestPoint(anchorPoints, change.position)?.let {
                            val newPoint = it.copy(position = change.position)
                            anchorPoints = (anchorPoints - it + newPoint).sortedBy { it.position.x }
                            selectedPoint = newPoint
                        }
                    }
                }
        ) {
            mSize = size

            drawPoints(
                points = anchorPoints.map { it.position },
                pointMode = PointMode.Polygon,
                color = Color.LightGray,
                strokeWidth = 3f
            )

            drawLine(
                color = Color.Gray,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = 3f
            )

            for (anchorPoint in anchorPoints) {
                SplinterPointer(anchorPoint.position, showRing = anchorPoint == selectedPoint)
            }

        }

        Box(
            Modifier
                .size(200.dp, 50.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .clickable { onCustomWaveSaved() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Save",
                color = Color.Black,
                fontSize = 12.sp
            )
        }
    }

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

    if (nearestPoint != null && nearestPoint.position.squaredDistanceTo(point) > 10000) return null

    return nearestPoint
}

fun findIfPointerBetween(anchorPoints: List<Offset>, point: Offset): Boolean {
    var isPtBetween = false
    for (i in 0 until anchorPoints.size - 1) {
        val first = anchorPoints[i]
        val second = anchorPoints[i + 1]

        isPtBetween = isPtBetween or isPointerBetween(point, first, second).also { Log.d("update", "isBetween: ${it}") }
    }
    return isPtBetween
}

fun isPointerBetween(pointer: Offset, start: Offset, end: Offset): Boolean {
    // Calculate the vector from start to pointer and start to end
    val vectorStartToPointer = pointer - start
    val vectorStartToEnd = end - start

    // Check if the pointer is collinear with the line segment
    val crossProduct = vectorStartToPointer.x * vectorStartToEnd.y - vectorStartToPointer.y * vectorStartToEnd.x
    Log.d("update", "$start $end $pointer crossProduct: $crossProduct")
    if (abs(crossProduct) > 10000f) return false // Not collinear

    // Check if the pointer lies within the bounds of the segment
    val dotProduct = vectorStartToPointer.x * vectorStartToEnd.x + vectorStartToPointer.y * vectorStartToEnd.y
    if (dotProduct < 0f) return false // Pointer is behind the start point

    val segmentLengthSquared = vectorStartToEnd.x * vectorStartToEnd.x + vectorStartToEnd.y * vectorStartToEnd.y
    if (dotProduct > segmentLengthSquared) return false // Pointer is beyond the end point

    return true // Pointer is on the line segment
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
    EditableWaveGraph(listOf(), { }, Modifier)
}