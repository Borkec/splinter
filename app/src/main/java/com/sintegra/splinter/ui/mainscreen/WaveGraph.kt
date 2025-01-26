package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.unit.dp

@Composable
fun WaveGraph(
    points: List<Float>,
    modifier: Modifier = Modifier
) {
    val resolution = points.size

    val xIndices = remember { List(resolution) { it.toFloat() / resolution.toFloat() } }

//    val cursorPositionNorm = cursorPosition.toFloat() / resolution.toFloat()

    Canvas(
        modifier = modifier.height(50.dp)
    ) {

        drawPoints(
            points = xIndices.zip(points).map { (x, y) -> Offset(x * size.width, (size.height / 2) + y * (size.height / 2)) },
            pointMode = PointMode.Polygon,
            color = Color.LightGray,
            strokeWidth = 3f
        )

        drawLine(
            color = Color.White,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 3f
        )

//        drawLine(
//            color = Color.Blue,
//            start = Offset(cursorPositionNorm * size.width, 0f),
//            end = Offset(cursorPositionNorm * size.width, size.height),
//            strokeWidth = 3f
//        )
    }
}