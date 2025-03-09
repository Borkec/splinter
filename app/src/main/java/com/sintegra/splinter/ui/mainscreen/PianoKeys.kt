package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.sintegra.splinter.core.ui.toDp
import com.sintegra.splinter.model.KeyColor
import com.sintegra.splinter.model.KeyType
import com.sintegra.splinter.ui.viewmodel.KeyViewState
import com.sintegra.splinter.ui.viewmodel.KeyViewState.Companion.OctaveViewState
import com.sintegra.splinter.ui.viewmodel.PianoKeysViewModel
import org.koin.androidx.compose.koinViewModel

private const val WHITE_KEYS = 7;

@Composable
fun Piano(
    numOfOctaves: Int = 1,
    viewModel: PianoKeysViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {

    var mSize by remember { mutableStateOf(IntSize.Zero) }
    var size by remember { mutableStateOf(Size(0f, 0f)) }
    val keyHeight = remember(size) { size.height / WHITE_KEYS }
    var pressedKey by remember { mutableStateOf<Pair<KeyType, Int>?>(null) }

    var positionMap by remember { mutableStateOf(mapOf<Offset, Pair<KeyType, Int>>()) }
    var sizeMap by remember { mutableStateOf(mapOf<Offset, Size>()) }

    Column(
        modifier
            .onSizeChanged { mSize = it }
            .pointerInput(Unit) {
                awaitPointerEventScope {

                    while (true) {
                        val event = awaitPointerEvent()
                        val position = event.changes.first().position
                        if (event.type == PointerEventType.Release) {
                            viewModel.onReleaseKey()
                            pressedKey = null
                            continue
                        }

                        // look up if this is possible to do without for loop
                        for (pos in positionMap.keys) {
                            val key = positionMap[pos]!!
                            val sz = sizeMap[pos]!!

                            if (sz.toRect().translate(pos).contains(position)) {
                                viewModel.onPlayKey(key.first, key.second)
                                pressedKey = key
                                break
                            }
                        }
                    }

                }
            },
    ) {
        for (octave in 1..numOfOctaves) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((mSize.height / numOfOctaves).toDp())
                    .onSizeChanged { size = it.toSize() }
            ) {
                for (viewState in OctaveViewState) {
                    val keyDrawData = viewState.getKeyDrawData(
                        keyPressed = viewState.type == pressedKey?.first && octave == pressedKey?.second,
                        keyHeight = keyHeight,
                        size = size)

                    Surface(
                        color = keyDrawData.color,
                        shape = RectangleShape,
                        modifier = Modifier
                            .offset(
                                x = keyDrawData.offsetX.toDp(),
                                y = keyDrawData.offsetY.toDp()
                            )
                            .height(keyDrawData.height.toDp())
                            .width(keyDrawData.width.toDp())
                            .padding(
                                vertical = 4.dp
                            )
                            .onGloballyPositioned {

                                val posInGp = it.parentLayoutCoordinates?.positionInParent() ?: Offset.Zero

                                positionMap += (posInGp + it.positionInParent()) to Pair(viewState.type, octave)
                                sizeMap += (posInGp + it.positionInParent()) to it.size.toSize()
                            }
                            .zIndex(keyDrawData.zIndex)
                    ) {}
                }
            }
        }
    }
}

private data class KeyDrawData(
    val color: Color,
    val offsetX: Int,
    val offsetY: Int,
    val width: Int,
    val height: Int,
    val zIndex: Float
)

private fun KeyViewState.getKeyDrawData(keyPressed: Boolean, keyHeight: Float, size: Size) =
    when (keyColor) {
        KeyColor.White -> KeyDrawData(
            color = if (keyPressed) Color.DarkGray else Color.White,
            offsetX = 0,
            offsetY = (relativeY * size.height).toInt(),
            width = size.width.toInt(),
            height = keyHeight.toInt(),
            zIndex = 0f

        )

        KeyColor.Black -> KeyDrawData(
            color = if (keyPressed) Color.LightGray else Color.Black,
            offsetY = (relativeY * size.height + keyHeight / 6).toInt(),
            offsetX = (size.width / 2).toInt(),
            width = (size.width / 2).toInt(),
            height = (keyHeight / 1.5).toInt(),
            zIndex = 1f
        )
    }

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun PianoPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Piano(modifier = Modifier.fillMaxSize())
    }
}