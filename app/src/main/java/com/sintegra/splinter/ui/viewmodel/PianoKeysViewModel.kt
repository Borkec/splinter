package com.sintegra.splinter.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.KeyColor
import com.sintegra.splinter.model.KeyType
import com.sintegra.splinter.model.getFrequency

class PianoKeysViewModel(private val audioRepository: AudioRepository) : ViewModel() {

    fun onPlayKey(keyType: KeyType) {
        audioRepository.setSineFrequency(keyType.getFrequency())
        audioRepository.playNote()
    }

    fun onReleaseKey() {
        audioRepository.releaseNote()
    }
}

data class KeyViewState(val type: KeyType, val color: KeyColor, val relativeY: Float)