package com.sintegra.splinter.ui.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.ui.viewmodel.SelectedWaveViewState.Companion.fromWaveModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WavePickerViewModel(
    private val audioRepository: AudioRepository
) : ViewModel() {

    val selectedWaveViewState: StateFlow<SelectedWaveViewState> =
        audioRepository.currentWave
            .map { waveModel ->
                fromWaveModel(waveModel)
            }.stateIn(viewModelScope, SharingStarted.Lazily, SelectedWaveViewState.initial)

    fun onWaveTypeSelected(waveType: WaveType) {
        audioRepository.setWaveType(waveType)
    }
}

data class SelectedWaveViewState(val waveName: String, val waveType: WaveType, val waveData: List<Offset>) {
    companion object {
        fun fromWaveModel(waveModel: WaveModel): SelectedWaveViewState {
            val res = 16
            val data = waveModel.audioData.toList()
            val resolution = data.size / res

            return SelectedWaveViewState(
                waveModel.waveType.toString(),
                waveModel.waveType,
                data
                    .filterIndexed { index, _ -> index % res == 0 }
                    .zip(List(resolution) { it.toFloat() / resolution.toFloat() })
                    .map { (x, y) -> Offset(x, y) }
            )
        }

        val initial = fromWaveModel(WaveModel.DEFAULT)
        val common = WaveType.entries.map { waveType -> fromWaveModel(WaveModel(waveType)) }
    }
}