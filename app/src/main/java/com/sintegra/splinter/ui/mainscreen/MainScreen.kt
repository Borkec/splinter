package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.ui.theme.SplinterTheme
import com.sintegra.splinter.viewmodel.MainActivityViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainActivityViewModel = koinViewModel()) {

    val currentWaveModel by viewModel.currentWave.collectAsState()
    val currentAudioBuffer by viewModel.currentBufferViewState.collectAsState()
    val cursorPosition by viewModel.currentCursorPositionViewState.collectAsState()

    MainScreen(
        waveModel = currentWaveModel,
        currentBuffer = currentAudioBuffer,
        cursorPosition = cursorPosition,
        onPressed = viewModel::onPressed,
        onHold = viewModel::onHold,
        onRelease = viewModel::onRelease,
        onWavePicked = viewModel::onWaveTypeSelected
    )
}

@Composable
fun MainScreen(
    waveModel: WaveModel,
    currentBuffer: List<Float>,
    cursorPosition: Int,
    onPressed: () -> Unit = {},
    onHold: (Float, Float) -> Unit = { _, _ -> },
    onRelease: () -> Unit = {},
    onWavePicked: (WaveType) -> Unit
) {

    Box {

        SplinterArea(
            Modifier.background(Color.Black),
            onPressed,
            onHold,
            onRelease
        )

        Row(Modifier.fillMaxWidth().align(Alignment.TopCenter)) {
            WavePicker(waveModel, onWavePicked, Modifier.weight(1f))
            WaveGraph(currentBuffer, cursorPosition, Modifier.weight(2f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplinterTheme {
        MainScreen(WaveModel(WaveType.SINE), listOf(), 0, {}, { _, _ -> }, {}, {})
    }
}