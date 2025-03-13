package com.sintegra.splinter.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.SoundInput

class SplinterAreaViewModel(private val audioRepository: AudioRepository) : ViewModel() {

    fun onTrackedPointer(id: Int, x: Float, y: Float) {
        audioRepository.addSoundInput(SoundInput(id, frequency = y*1000))
    }

    fun onPointerRelease(id: Int) {
        audioRepository.removeSoundInput(id)
    }

}