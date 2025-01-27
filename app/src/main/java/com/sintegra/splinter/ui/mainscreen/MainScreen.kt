package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.ui.theme.SplinterTheme
import com.sintegra.splinter.ui.viewmodel.CurrentScreen
import com.sintegra.splinter.ui.viewmodel.MainViewModel
import com.sintegra.splinter.ui.viewmodel.MainViewSate
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {

    val currentWaveModel by viewModel.selectedWaveViewState.collectAsState()
    val screenViewState by viewModel.screenViewState.collectAsState()

    MainScreenContent(
        selectedWave = currentWaveModel,
        screenViewState = screenViewState,
        onPressed = viewModel::onPressed,
        onHold = viewModel::onHold,
        onRelease = viewModel::onRelease,
        onWavePicked = viewModel::onWaveTypeSelected,
        onCustomWaveClicked = viewModel::onCustomWavePicked,
        onSetCustomWave = viewModel::onSetCustomWave,
        onCustomWaveEditorStartSound = viewModel::onCustomWaveEditorStartSound,
        onCustomWaveEditorStopSound = viewModel::onCustomWaveEditorStopSound,
        onWaveSave = viewModel::closeCustomScreen
    )
}

@Composable
fun MainScreenContent(
    selectedWave: MainViewSate.SelectedWave,
    screenViewState: MainViewSate.ScreenViewState,
    onPressed: () -> Unit = {},
    onHold: (Float, Float) -> Unit = { _, _ -> },
    onRelease: () -> Unit = {},
    onWavePicked: (WaveType) -> Unit,
    onCustomWaveClicked: () -> Unit,
    onSetCustomWave: (List<Float>) -> Unit,
    onCustomWaveEditorStartSound: () -> Unit,
    onCustomWaveEditorStopSound: () -> Unit,
    onWaveSave: () -> Unit
) {

    Box(
        modifier = Modifier
    ) {
        when (screenViewState.screen) {
            CurrentScreen.MAIN -> {
                SplinterArea(
                    overlay = { modifier ->

                    },
                    onPressed,
                    onHold,
                    onRelease,
                    Modifier
                )

                WavePicker(selectedWave, onWavePicked, onCustomWaveClicked)

            }

            CurrentScreen.CUSTOM_PICKER -> {
                EditableWaveGraph(onWaveSave, onSetCustomWave, onCustomWaveEditorStartSound, onCustomWaveEditorStopSound)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplinterTheme {
        MainScreenContent(
            MainViewSate.SelectedWave.initial,
            MainViewSate.ScreenViewState(CurrentScreen.MAIN),
            {},
            { _, _ -> },
            {},
            {},
            {},
            {},
            {},
            {},
            {})
    }
}