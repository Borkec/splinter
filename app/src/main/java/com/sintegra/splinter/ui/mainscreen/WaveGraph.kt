package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WaveGraph(
    points: List<Float>,
    modifier: Modifier = Modifier
) {
    val resolution = points.size

    val xIndices = remember { List(resolution) { it.toFloat() / resolution.toFloat() } }

    val lineColor = MaterialTheme.colors.background
    val pointColor = MaterialTheme.colors.primary

    Canvas(
        modifier = modifier
            .height(50.dp)
            .background(MaterialTheme.colors.onBackground)
    ) {

        drawLine(
            color = lineColor,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 3f
        )
5
        drawPoints(
            points = xIndices.zip(points).map { (x, y) -> Offset(x * size.width, (size.height / 2) + y * (size.height / 2)) },
            pointMode = PointMode.Polygon,
            color = pointColor,
            strokeWidth = 5f
        )
    }
}

@Preview
@Composable
fun WaveGraphPreview(modifier: Modifier = Modifier) {
    WaveGraph(listOf())
}