package com.sintegra.splinter.ui.mainscreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.sintegra.splinter.core.ui.getPointerInput
import com.sintegra.splinter.core.ui.toDp
import com.sintegra.splinter.model.KeyColor
import com.sintegra.splinter.model.KeyType
import com.sintegra.splinter.ui.viewmodel.KeyViewState

private const val WHITE_KEYS = 7;

@Composable
fun Octave(
    modifier: Modifier = Modifier,
    onKeyPressed: (KeyType) -> Unit,
    onKeyReleased: () -> Unit
) {

    var size by remember { mutableStateOf(Size(0f, 0f)) }
    val keyHeight = remember(size) { size.height / WHITE_KEYS }

    Box(
        modifier = modifier
            .onGloballyPositioned { size = it.size.toSize() },
    ) {

        for (index in Octave.indices) {
            Surface(
                color = when (Octave[index].color) {
                    KeyColor.White -> Color.White
                    KeyColor.Black -> Color.Black
                },
                shape = RectangleShape,
                modifier = Modifier
                    .offset(
                        y = (Octave[index].relativeY * size.height).toInt().toDp(),
                        x =
                        when (Octave[index].color) {
                            KeyColor.White -> 0
                            KeyColor.Black -> size.width / 2
                        }.toInt().toDp()
                    )
                    .height(keyHeight.toInt().toDp())
                    .width(
                        when (Octave[index].color) {
                            KeyColor.Black -> (size.width / 2)
                            KeyColor.White -> size.width
                        }.toInt().toDp()
                    )
                    .padding(
                        vertical =
                        when (Octave[index].color) {
                            KeyColor.Black -> 16.dp
                            KeyColor.White -> 4.dp
                        }
                    )
                    .pointerInput(Unit) {
                        getPointerInput(
                            onPressed = { onKeyPressed(Octave[index].type) },
                            onRelease = { onKeyReleased() }
                        )
                    }
                    .zIndex(
                        when (Octave[index].color) {
                            KeyColor.White -> 0f
                            KeyColor.Black -> 1f
                        }
                    )
            ) {}
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun OctavePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Octave(Modifier.fillMaxSize(), { Log.d("onKeyPressed", it.toString()) }, {})
    }
}

val Octave = listOf(
    KeyViewState(KeyType.C, KeyColor.White, 0f),
    KeyViewState(KeyType.CSharp, KeyColor.Black, 1 / 14f),
    KeyViewState(KeyType.D, KeyColor.White, 1 / 7f),
    KeyViewState(KeyType.DSharp, KeyColor.Black, 3 / 14f),
    KeyViewState(KeyType.E, KeyColor.White, 2 / 7f),
    KeyViewState(KeyType.F, KeyColor.White, 3 / 7f),
    KeyViewState(KeyType.FSharp, KeyColor.Black, 1 / 2f),
    KeyViewState(KeyType.G, KeyColor.White, 4 / 7f),
    KeyViewState(KeyType.GSharp, KeyColor.Black, 9 / 14f),
    KeyViewState(KeyType.A, KeyColor.White, 5 / 7f),
    KeyViewState(KeyType.ASharp, KeyColor.Black, 11 / 14f),
    KeyViewState(KeyType.B, KeyColor.White, 6 / 7f),
)