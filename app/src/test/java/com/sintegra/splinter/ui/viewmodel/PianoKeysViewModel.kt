package com.sintegra.splinter.ui.viewmodel

import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.KeyType
import com.sintegra.splinter.model.SoundInput
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class PianoKeysViewModelTest {

    private val audioRepository = mock<AudioRepository>()

    private val viewModel = PianoKeysViewModel(audioRepository)

    @Test
    fun pianoKeysViewModel_onPlayKey_addsSoundInput() {
        val id = 0
        val keyType = KeyType.C
        val octaveLevel = 1

        viewModel.onPlayKey(id, keyType, octaveLevel)

        val captor = argumentCaptor<Float>()
        verify(audioRepository).addSoundInput(soundInput = SoundInput(id = id, frequency = captor.capture()))
    }

    @Test
    fun pianoKeysViewModel_onReleaseKey_removesSoundInput() {
        val id = 0
        viewModel.onReleaseKey(id)

        verify(audioRepository).removeSoundInput(id)
    }
}