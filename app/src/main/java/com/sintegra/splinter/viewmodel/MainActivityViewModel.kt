package com.sintegra.splinter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.WaveType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val audioRepository: AudioRepository): ViewModel() {

    val currentWave = audioRepository.currentWave

    private val _currentBufferViewState = MutableStateFlow<List<Float>>(listOf())
    val currentBufferViewState: StateFlow<List<Float>> = _currentBufferViewState

    private val _currentCursorPositionViewState = MutableStateFlow(0)
    val currentCursorPositionViewState: StateFlow<Int> = _currentCursorPositionViewState

    init {
        viewModelScope.launch {
            audioRepository.audioBuffer.collect {
                _currentBufferViewState.value = it
            }
        }

        // fetches the current cursor (point read on the wave table). this implementation is currently bad
        // so i commented out the fetching
//        viewModelScope.launch {
//            audioRepository.currentCursorPosition.collect {
//                _currentCursorPositionViewState.value = it
//            }
//        }
    }

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

}