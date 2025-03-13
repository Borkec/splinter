package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.sintegra.splinter.core.ui.SplinterPointer
import com.sintegra.splinter.core.ui.getPointerInput
import com.sintegra.splinter.ui.viewmodel.SplinterAreaViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun SplinterArea(
    viewModel: SplinterAreaViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {

    var pointers by remember { mutableStateOf(mapOf<Int, Offset>()) }
    var surfaceSize: Size? by remember { mutableStateOf(null) }
    val pointerColor = MaterialTheme.colors.primary

    Surface(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                surfaceSize = size
            }
            .pointerInput(Unit) {
                surfaceSize?.let {
                    getPointerInput(
                        onHold = { id, x, y ->
                            viewModel.onTrackedPointer(id, x / it.width, y / it.height)
                            pointers += id to Offset(x, y)
                        },
                        onRelease = { id ->
                            viewModel.onPointerRelease(id)
                            pointers -= id
                        }
                    )
                }
            },
        color = MaterialTheme.colors.background
    ) {

        pointers.values.forEach { midPoint ->
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SplinterPointer(
                    midPoint = midPoint,
                    color = pointerColor
                )
            }
        }
    }

}

@Preview
@Composable
fun SplinterAreaPreview() {
    SplinterArea()
}

data class TouchPoint(
    val id: String = UUID.randomUUID().toString(),
    val position: Offset,
    val timestamp: Long,
    var alpha: Float = 1f
)