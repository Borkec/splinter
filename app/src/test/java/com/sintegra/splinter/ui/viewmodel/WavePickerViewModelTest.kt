package com.sintegra.splinter.ui.viewmodel

import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.WaveType
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class WavePickerViewModelTest {

    private val audioRepository = mock<AudioRepository>()
    private val viewModel = WavePickerViewModel(audioRepository)

    @Test
    fun wavePickerViewModel_onWaveTypeSelected() {
        val waveType = WaveType.SQUARE
        viewModel.onWaveTypeSelected(waveType)

        verify(audioRepository).setWaveType(waveType)
    }

    @Test
    fun wavePickerViewModel_verifyViewStateInitialized() {
        val received = viewModel.selectedWaveViewState.value
        Assert.assertEquals(SelectedWaveViewState.initial, received)
    }

}