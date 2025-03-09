package com.sintegra.splinter.data.service

import com.sintegra.splinter.data.service.NativeAudioBridge.removeAudioListener
import com.sintegra.splinter.model.SoundInput
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface AudioSource {

    val audioSignal: Flow<FloatArray>

    val cursorPosition: Flow<Int>

    fun startAudioStream()

    fun stopAudioStream()

    fun playNote()

    fun releaseNote()

    fun addSoundInput(soundInput: SoundInput)

    fun changeSoundInputFrequency(soundInput: SoundInput, newFrequency: Float)

    fun removeSoundInput(soundInputId: Int)

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

    override fun playNote() {
        NativeAudioBridge.playNote()
    }

    override fun releaseNote() {
        NativeAudioBridge.releaseNote()
    }

    override fun startAudioStream() {
        NativeAudioBridge.startAudioStream()
    }

    override fun stopAudioStream() {
        NativeAudioBridge.stopAudioStream()
    }

    override fun addSoundInput(soundInput: SoundInput) {
        NativeAudioBridge.addSoundInput(soundInput.id, soundInput.frequency)
    }

    override fun changeSoundInputFrequency(soundInput: SoundInput, newFrequency: Float) {
        NativeAudioBridge.changeSoundInputFrequency(soundInput.id, newFrequency)
    }

    override fun removeSoundInput(soundInputId: Int) {
        NativeAudioBridge.removeSoundInput(soundInputId)
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