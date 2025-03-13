package com.sintegra.splinter.data.service

import com.sintegra.splinter.model.SoundInput
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface AudioSource {

    val audioSignal: Flow<FloatArray>

    val cursorPosition: Flow<Int>

    fun addSoundInput(soundInput: SoundInput)

    fun changeSoundInputFrequency(soundInputId: Int, newFrequency: Float)

    fun removeSoundInput(soundInputId: Int)

    fun setAudioBuffer(buffer: FloatArray)
}

class AudioSourceImpl(private val nativeAudioBridge: NativeAudioBridge): AudioSource {

    override val audioSignal = callbackFlow {
        val listener = object : AudioFrameListener {
            override fun onAudioDataAvailable(floatArray: FloatArray) {
                trySend(floatArray)
            }
        }

        nativeAudioBridge.addAudioListener(listener)
        awaitClose {
            nativeAudioBridge.removeAudioListener(listener)
        }
    }

    override val cursorPosition = callbackFlow {
        val listener = object : AudioCursorListener {
            override fun onAudioCursorAvailable(cursorPosition: Int) {
                trySend(cursorPosition)
            }
        }

        nativeAudioBridge.addAudioCursorListener(listener)
        awaitClose()
    }

    override fun addSoundInput(soundInput: SoundInput) {
        nativeAudioBridge.addSoundInput(soundInput.id, soundInput.frequency)
    }

    override fun changeSoundInputFrequency(soundInputId: Int, newFrequency: Float) {
        nativeAudioBridge.changeSoundInputFrequency(soundInputId, newFrequency)
    }

    override fun removeSoundInput(soundInputId: Int) {
        nativeAudioBridge.removeSoundInput(soundInputId)
    }

    override fun setAudioBuffer(buffer: FloatArray) {
        nativeAudioBridge.setAudioBuffer(buffer)
    }
}

interface AudioFrameListener {
    fun onAudioDataAvailable(floatArray: FloatArray)
}

interface AudioCursorListener {
    fun onAudioCursorAvailable(cursorPosition: Int)
}