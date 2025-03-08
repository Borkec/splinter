package com.sintegra.splinter.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.Modulation
import com.sintegra.splinter.model.ModulationType

class SplinterAreaViewModel(private val audioRepository: AudioRepository) : ViewModel() {

    fun onPressed() {
        audioRepository.playNote()
    }

    fun onHold(x: Float, y: Float) {
        audioRepository.setModulation(y, Modulation(ModulationType.FREQUENCY, 20f, 1024f))
    }

    fun onRelease() {
        audioRepository.releaseNote()
    }

}