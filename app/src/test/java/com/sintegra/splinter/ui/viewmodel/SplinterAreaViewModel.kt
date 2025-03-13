package com.sintegra.splinter.ui.viewmodel

import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.SoundInput
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SplinterAreaViewModelTest {

    private val audioRepository = mock<AudioRepository>()

    private val viewModel = SplinterAreaViewModel(audioRepository)

    @Test
    fun splinterAreaViewModel_onTrackedPointer() {
        val id = 0
        val x = 10f
        val y = 10f
        viewModel.onTrackedPointer(id, x, y)

        val captor = argumentCaptor<Float>()
        verify(audioRepository).addSoundInput(soundInput = SoundInput(id = id, frequency = captor.capture()))
    }

    @Test
    fun splinterAreaViewModel_onRemovedPointer() {
        val id = 0
        viewModel.onPointerRelease(id)

        verify(audioRepository).removeSoundInput(id)
    }
}