package com.sintegra.splinter.data.repository

import com.sintegra.splinter.data.service.AudioSource
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType
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

    fun startAudioStream()

    fun stopAudioStream()

    fun setSineFrequency(frequency: Float)

    fun setOffset(offset: Float)

    fun setWaveType(waveType: WaveType, customWave: List<Float>? = null)
}

class AudioRepositoryImpl(private val audioSource: AudioSource) : AudioRepository {

    private val _currentWave = MutableStateFlow(WaveModel(WaveType.SINE))
    override val currentWave: StateFlow<WaveModel> = _currentWave

    private val audioCoroutineScope = CoroutineScope(Dispatchers.Default)
    private val waveTableSize = audioSource.getWaveTableSize()

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


    override fun startAudioStream() {
        audioSource.startAudioStream()
    }

    override fun stopAudioStream() {
        audioSource.stopAudioStream()
    }

    override fun setSineFrequency(frequency: Float) {
        audioSource.setSineFrequency(frequency)
    }

    override fun setOffset(offset: Float) {
        audioSource.setOffset(offset)
    }

    override fun setWaveType(waveType: WaveType, customWave: List<Float>?) {
        _currentWave.value = WaveModel(waveType, customWave)
    }
}