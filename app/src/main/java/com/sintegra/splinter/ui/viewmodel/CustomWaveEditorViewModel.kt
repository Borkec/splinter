package com.sintegra.splinter.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.WaveType

class CustomWaveEditorViewModel(private val audioRepository: AudioRepository) : ViewModel() {

    fun onSetCustomWave(customWave: List<Float>) {
        audioRepository.setWaveType(WaveType.CUSTOM, customWave)
    }

    fun onCustomWaveEditorStopSound() {
        audioRepository.releaseNote()
    }

    fun onCustomWaveEditorStartSound() {
        audioRepository.setSineFrequency(420f)
        audioRepository.playNote()
    }

}