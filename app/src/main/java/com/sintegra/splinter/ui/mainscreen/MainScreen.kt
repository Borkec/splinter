package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.ui.theme.SplinterTheme
import com.sintegra.splinter.ui.viewmodel.MainViewModel
import com.sintegra.splinter.ui.viewmodel.MainViewSate
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {

    val currentWaveModel by viewModel.selectedWaveViewState.collectAsState()

    MainScreen(
        selectedWave = currentWaveModel,
        onPressed = viewModel::onPressed,
        onHold = viewModel::onHold,
        onRelease = viewModel::onRelease,
        onWavePicked = viewModel::onWaveTypeSelected
    )
}

@Composable
fun MainScreen(
    selectedWave: MainViewSate.SelectedWave,
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

        WavePicker(selectedWave, onWavePicked)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplinterTheme {
        MainScreen(MainViewSate.SelectedWave.initial, {}, { _, _ -> }, {}, {})
    }
}