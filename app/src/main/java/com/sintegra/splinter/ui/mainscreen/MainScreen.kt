package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sintegra.splinter.model.KeyType
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
        onPressedKey = viewModel::onPlayKey,
        onReleasedKey = viewModel::onReleaseKey,
        onWavePicked = viewModel::onWaveTypeSelected,
        onCustomWaveClicked = viewModel::openCustomWavePickerScreen,
        onSetCustomWave = viewModel::onSetCustomWave,
        onCustomWaveEditorStartSound = viewModel::onCustomWaveEditorStartSound,
        onCustomWaveEditorStopSound = viewModel::onCustomWaveEditorStopSound,
        onWaveSave = viewModel::closeCustomWavePickerScreen
    )
}

@Composable
fun MainScreenContent(
    selectedWave: MainViewSate.SelectedWave,
    screenViewState: MainViewSate.ScreenViewState,
    onPressed: () -> Unit = {},
    onHold: (Float, Float) -> Unit = { _, _ -> },
    onRelease: () -> Unit = {},
    onPressedKey: (KeyType) -> Unit = {},
    onReleasedKey: () -> Unit = {},
    onWavePicked: (WaveType) -> Unit,
    onCustomWaveClicked: () -> Unit,
    onSetCustomWave: (List<Float>) -> Unit,
    onCustomWaveEditorStartSound: () -> Unit,
    onCustomWaveEditorStopSound: () -> Unit,
    onWaveSave: () -> Unit
) {

    var isKeys by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
    ) {
        when (screenViewState.screen) {
            CurrentScreen.MAIN -> {

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        modifier = Modifier.rotate(90f),
                        checked = isKeys,
                        onCheckedChange = {
                            isKeys = !isKeys
                        }
                    )

                    WavePicker(
                        selectedWave = selectedWave,
                        onWavePicked = onWavePicked,
                        onCustomWaveClicked = onCustomWaveClicked,
                    )
                }


                when (isKeys) {
                    false ->
                        SplinterArea(
                            onPressed,
                            onHold,
                            onRelease,
                            Modifier
                        )

                    true ->
                        Octave(
                            Modifier.fillMaxSize(),
                            onPressedKey,
                            onReleasedKey
                        )

                }
            }

            CurrentScreen.CUSTOM_PICKER -> {
                EditableWaveGraph(onWaveSave, onSetCustomWave, onCustomWaveEditorStartSound, onCustomWaveEditorStopSound)
            }
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun DefaultPreview() {
    SplinterTheme {
        MainScreenContent(
            MainViewSate.SelectedWave.initial,
            MainViewSate.ScreenViewState(CurrentScreen.MAIN),
            {},
            { _, _ -> },
            {},
            { _ -> },
            {},
            {},
            {},
            {},
            {},
            {},
            {})
    }
}