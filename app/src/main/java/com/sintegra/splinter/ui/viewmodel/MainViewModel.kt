package com.sintegra.splinter.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.KeyColor
import com.sintegra.splinter.model.KeyType
import com.sintegra.splinter.model.Modulation
import com.sintegra.splinter.model.ModulationType
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.model.getFrequency
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
        audioRepository.playNote()
    }

    fun onHold(x: Float, y: Float) {
        audioRepository.setModulation(y, Modulation(ModulationType.FREQUENCY, 20f, 1024f))
    }

    fun onRelease() {
        audioRepository.releaseNote()
    }

    fun onWaveTypeSelected(waveType: WaveType) {
        audioRepository.setWaveType(waveType)
    }

    fun openCustomWavePickerScreen() {
        _screenViewState.value = MainViewSate.ScreenViewState(CurrentScreen.CUSTOM_PICKER)
    }

    fun onSetCustomWave(customWave: List<Float>) {
        audioRepository.setWaveType(WaveType.CUSTOM, customWave)
    }

    fun onPlayKey(keyType: KeyType) {

        Log.d("pressed", "${keyType} ${keyType.getFrequency()}")
        audioRepository.setSineFrequency(keyType.getFrequency())
        audioRepository.playNote()
    }

    fun onReleaseKey() {
        audioRepository.releaseNote()
    }

    fun closeCustomWavePickerScreen() {
        audioRepository.releaseNote()
        _screenViewState.value = MainViewSate.ScreenViewState(CurrentScreen.MAIN)
    }

    fun onCustomWaveEditorStopSound() {
        audioRepository.releaseNote()
    }

    fun onCustomWaveEditorStartSound() {
        audioRepository.setSineFrequency(420f)
        audioRepository.playNote()
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

data class KeyViewState(val type: KeyType, val color: KeyColor, val relativeY: Float)

enum class CurrentScreen {
    MAIN, CUSTOM_PICKER
}