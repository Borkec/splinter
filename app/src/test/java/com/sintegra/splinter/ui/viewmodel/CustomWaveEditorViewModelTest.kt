package com.sintegra.splinter.ui.viewmodel

import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.WaveType
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class CustomWaveEditorViewModelTest {

    private val audioRepository = mock<AudioRepository>()

    private val viewModel = CustomWaveEditorViewModel(audioRepository)

    @Test
    fun customWaveEditorViewModel_onSetCustomWave_waveTypeSet() {
        val mockList = listOf(0.1f, 0.2f, 0.3f)
        viewModel.onSetCustomWave(mockList)

        verify(audioRepository).setWaveType(WaveType.CUSTOM, mockList)
    }

    @Test
    fun customWaveEditorViewModel_onPlayNote_addSoundInputCalled() {
        viewModel.onCustomWaveEditorStartSound()

        verify(audioRepository).addSoundInput(any())
    }

    @Test
    fun customWaveEditorViewModel_onStopSound_removeSoundInputCalled() {
        viewModel.onCustomWaveEditorStopSound()

        verify(audioRepository).removeSoundInput(any())
    }

}