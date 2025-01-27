package com.sintegra.splinter.data.service

import com.sintegra.splinter.data.service.NativeAudioBridge.removeAudioListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface AudioSource {

    val audioSignal: Flow<FloatArray>

    val cursorPosition: Flow<Int>

    fun startAudioStream()

    fun stopAudioStream()

    fun setSineFrequency(frequency: Float)

    fun setAudioBuffer(buffer: FloatArray)

    fun getWaveTableSize(): Int
}

class AudioSourceImpl: AudioSource {

    override val audioSignal = callbackFlow {
        val listener = object : AudioFrameListener {
            override fun onAudioDataAvailable(floatArray: FloatArray) {
                trySend(floatArray)
            }
        }

        NativeAudioBridge.addAudioListener(listener)
        awaitClose {
            removeAudioListener(listener)
        }
    }

    override val cursorPosition = callbackFlow {
        val listener = object : AudioCursorListener {
            override fun onAudioCursorAvailable(cursorPosition: Int) {
                trySend(cursorPosition)
            }
        }

        NativeAudioBridge.addAudioCursorListener(listener)
        awaitClose()
    }

    override fun startAudioStream() {
        NativeAudioBridge.startAudioStream()
    }

    override fun stopAudioStream() {
        NativeAudioBridge.stopAudioStream()
    }

    override fun setSineFrequency(frequency: Float) {
        NativeAudioBridge.setFrequency(frequency)
    }

    override fun setAudioBuffer(buffer: FloatArray) {
        NativeAudioBridge.setAudioBuffer(buffer)
    }

    override fun getWaveTableSize(): Int {
        return NativeAudioBridge.getTableSize()
    }


}

enum class AudioServerResult(statusCode: Int) {
    OK(0), UNKNOWN(1)
}

interface AudioFrameListener {
    fun onAudioDataAvailable(floatArray: FloatArray)
}

interface AudioCursorListener {
    fun onAudioCursorAvailable(cursorPosition: Int)
}