package com.sintegra.splinter.data.service

import com.sintegra.splinter.model.SoundInput
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AudioSourceTest {

    private val nativeAudioBridge = mock<NativeAudioBridge>()

    private val audioSource = AudioSourceImpl(nativeAudioBridge)

    @Test
    fun verify_addSoundInput() {
        val idMock = 1
        val frequencyMock = 1.0f

        val mockSoundInput = SoundInput(id = idMock, frequency = frequencyMock)
        audioSource.addSoundInput(mockSoundInput)

        verify(nativeAudioBridge).addSoundInput(idMock, frequencyMock)
    }

    @Test
    fun verify_removeSoundInput() {
        val idMock = 1
        audioSource.removeSoundInput(idMock)

        verify(nativeAudioBridge).removeSoundInput(idMock)
    }

    @Test
    fun verify_changeSoundInputFrequency() {
        val idMock = 1
        val frequencyMock = 1.0f
        audioSource.changeSoundInputFrequency(idMock, frequencyMock)

        verify(nativeAudioBridge).changeSoundInputFrequency(idMock, frequencyMock)
    }
}