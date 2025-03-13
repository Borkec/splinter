package com.sintegra.splinter.data.repository

import com.sintegra.splinter.data.service.AudioSource
import com.sintegra.splinter.model.SoundInput
import com.sintegra.splinter.model.WaveType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AudioRepositoryTest {

    private val audioSource = mock<AudioSource>()

    private val audioRepository = AudioRepositoryImpl(audioSource)

    @Test
    fun verify_addSoundInput() {
        val soundInput = SoundInput(1, 440f)
        audioRepository.addSoundInput(soundInput)
        verify(audioSource).addSoundInput(soundInput)
    }

    @Test
    fun verify_removeSoundInput() {
        val soundInputId = 1
        audioRepository.removeSoundInput(soundInputId)
        verify(audioSource).removeSoundInput(soundInputId)
    }

    @Test
    fun verify_changeSoundInputFrequency() {
        val soundInputId = 1
        val frequency = 440f
        audioRepository.changeSoundInputFrequency(soundInputId, frequency)
        verify(audioSource).changeSoundInputFrequency(soundInputId, frequency)
    }

    @Test
    fun verify_setWaveType() = runTest {
        val waveType = WaveType.SINE
        audioRepository.setWaveType(waveType)

        val received = audioRepository.currentWave.first()

        assert(received.waveType == waveType)
        verify(audioSource).setAudioBuffer(received.audioData)
    }

}