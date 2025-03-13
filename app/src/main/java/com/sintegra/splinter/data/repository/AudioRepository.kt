package com.sintegra.splinter.data.repository

import com.sintegra.splinter.data.service.AudioSource
import com.sintegra.splinter.model.SoundInput
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface AudioRepository {

    /**
     * Represents state of the current selected wave.
     */
    val currentWave: StateFlow<WaveModel>

    /**
     * Adds a [SoundInput] to the list of current soundInputs.
     *
     */
    fun addSoundInput(soundInput: SoundInput)

    /**
     * Changes the [frequency] of the [SoundInput] with the given [soundInputId].
     */
    fun changeSoundInputFrequency(soundInputId: Int, frequency: Float)

    /**
     * Removes the [SoundInput] with the given [soundInputId].
     */
    fun removeSoundInput(soundInputId: Int)

    /**
     * Sets the [WaveType] of the current wave. Can supply a [customWave] to set the wavetable to any list of float values.
     */
    fun setWaveType(waveType: WaveType, customWave: List<Float>? = null)
}

class AudioRepositoryImpl(private val audioSource: AudioSource) : AudioRepository {

    private val _currentWave = MutableStateFlow(WaveModel(WaveType.SINE))
    override val currentWave: StateFlow<WaveModel> = _currentWave

    private val audioCoroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        audioCoroutineScope.launch {
            _currentWave.collect { wave ->
                audioSource.setAudioBuffer(wave.audioData)
            }
        }
    }

    override fun addSoundInput(soundInput: SoundInput) {
        audioSource.addSoundInput(soundInput)
    }

    override fun removeSoundInput(soundInputId: Int) {
        audioSource.removeSoundInput(soundInputId)
    }

    override fun changeSoundInputFrequency(soundInputId: Int, frequency: Float) {
        audioSource.changeSoundInputFrequency(soundInputId, frequency)
    }

    override fun setWaveType(waveType: WaveType, customWave: List<Float>?) {
        _currentWave.value = WaveModel(waveType, customWave)
    }
}