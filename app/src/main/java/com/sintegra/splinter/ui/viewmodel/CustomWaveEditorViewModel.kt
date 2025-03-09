package com.sintegra.splinter.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.SoundInput
import com.sintegra.splinter.model.WaveType

class CustomWaveEditorViewModel(private val audioRepository: AudioRepository) : ViewModel() {

    private val defaultSoundInput = SoundInput(frequency = 440f)

    fun onSetCustomWave(customWave: List<Float>) {
        audioRepository.setWaveType(WaveType.CUSTOM, customWave)
    }

    fun onCustomWaveEditorStopSound() {
        audioRepository.removeSoundInput(defaultSoundInput.id)
    }

    fun onCustomWaveEditorStartSound() {
        audioRepository.addSoundInput(defaultSoundInput)
    }

}