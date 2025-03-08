package com.sintegra.splinter.data.repository

import com.sintegra.splinter.data.service.AudioSource
import com.sintegra.splinter.model.Envelope
import com.sintegra.splinter.model.Modulation
import com.sintegra.splinter.model.ModulationType
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.model.interpolateValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface AudioRepository {

    val currentWave: StateFlow<WaveModel>

    val audioBuffer: Flow<List<Float>>
    val currentCursorPosition: Flow<Int>

    fun playNote()

    fun releaseNote()

    fun setSineFrequency(value: Float)

    fun setEnvelopeFilter(envelope: Envelope)

    fun setModulation(value: Float, modulation: Modulation)

    fun setWaveType(waveType: WaveType, customWave: List<Float>? = null)
}

class AudioRepositoryImpl(private val audioSource: AudioSource) : AudioRepository {

    private var currentEnvelope: MutableStateFlow<Envelope> = MutableStateFlow(
        value = Envelope(0.1f, 0.0f, 1.0f, 0.0f)
    )

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

    override val audioBuffer: Flow<List<Float>> =
        audioSource
            .audioSignal
            .map { it.toList() }

    override val currentCursorPosition: Flow<Int> =
        audioSource.cursorPosition


    override fun playNote() {
        audioSource.playNote()
    }

    override fun releaseNote() {
        audioSource.releaseNote()
    }

    override fun setEnvelopeFilter(envelope: Envelope) {
        this.currentEnvelope.value = envelope
    }

    override fun setModulation(value: Float, modulation: Modulation) {
        when(modulation.type) {
            ModulationType.FREQUENCY -> {
                audioSource.setSineFrequency(modulation.interpolateValue(value))
            }
        }
    }

    override fun setSineFrequency(frequency: Float) {
        audioSource.setSineFrequency(frequency)
    }

    override fun setWaveType(waveType: WaveType, customWave: List<Float>?) {
        _currentWave.value = WaveModel(waveType, customWave)
    }
}