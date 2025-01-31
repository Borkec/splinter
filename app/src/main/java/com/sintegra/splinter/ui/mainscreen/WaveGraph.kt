package com.sintegra.splinter.ui.mainscreen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun WaveGraph(
    points: List<Float>,
    modifier: Modifier = Modifier
) {
    val res = 8

    var mSize: IntSize by remember { mutableStateOf(IntSize.Zero) }
    val wave = remember(mSize) {
        val resolution = points.size / res

        points
            .filterIndexed { index, _ -> index % res == 0 }
            .zip(List(resolution) { it.toFloat() / resolution.toFloat() })
            .map { (y, x) -> Offset(x * mSize.width, (mSize.height / 2) + y * (mSize.height / 2)) }
    }

    val lineColor = MaterialTheme.colors.background
    val pointColor = MaterialTheme.colors.primary

    Canvas(
        modifier = modifier
            .height(50.dp)
            .background(MaterialTheme.colors.onBackground)
            .onSizeChanged { mSize = it }
    ) {

        drawLine(
            color = lineColor,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 3f
        )

        drawPoints(
            points = wave,
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