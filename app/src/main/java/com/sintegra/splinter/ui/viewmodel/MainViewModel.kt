package com.sintegra.splinter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.ui.viewmodel.MainViewSate.SelectedWave.Companion.fromWaveModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(private val audioRepository: AudioRepository) : ViewModel() {

    val selectedWaveViewState: StateFlow<MainViewSate.SelectedWave> =
        audioRepository.currentWave
            .map { waveModel ->
                fromWaveModel(waveModel)
            }.stateIn(viewModelScope, SharingStarted.Lazily, MainViewSate.SelectedWave.initial)

    private val _screenViewState: MutableStateFlow<MainViewSate.ScreenViewState> = MutableStateFlow(MainViewSate.ScreenViewState(CurrentScreen.MAIN))
    val screenViewState: StateFlow<MainViewSate.ScreenViewState> = _screenViewState

    fun onPressed() {
        audioRepository.startAudioStream()
    }

    fun onHold(x: Float, y: Float) {
        audioRepository.setSineFrequency(y)
        audioRepository.setOffset(x)
    }

    fun onRelease() {
        audioRepository.stopAudioStream()
    }

    fun onWaveTypeSelected(waveType: WaveType) {
        audioRepository.setWaveType(waveType)
    }

    fun onCustomWavePicked() {
        _screenViewState.value = MainViewSate.ScreenViewState(CurrentScreen.CUSTOM_PICKER)
    }

    fun onCustomWaveSaved() {
        _screenViewState.value = MainViewSate.ScreenViewState(CurrentScreen.MAIN)
    }
}

sealed class MainViewSate {
    data class SelectedWave(val waveName: String, val waveType: WaveType, val waveData: List<Float>) : MainViewSate() {
        companion object {
            val initial = fromWaveModel(WaveModel.DEFAULT)
            val common = WaveType.entries.map { waveType -> fromWaveModel(WaveModel(waveType)) }

            fun fromWaveModel(waveModel: WaveModel) =
                SelectedWave(waveModel.waveType.toString(), waveModel.waveType, waveModel.audioData.toList())
        }
    }

    data class ScreenViewState(val screen: CurrentScreen)
}
enum class CurrentScreen {
    MAIN, CUSTOM_PICKER
}