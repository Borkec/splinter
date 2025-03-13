package com.sintegra.splinter.data.service

open class NativeAudioBridge {

    external fun initializeBridge(size: Int)
    external fun getTableSize(): Int

    external fun openAudioStream(): Int
    external fun closeAudioStream(): Int
    external fun startAudioStream(): Int
    external fun stopAudioStream(): Int

    external fun playNote()
    external fun releaseNote()

    external fun addAudioListener(audioFrameListener: AudioFrameListener)
    external fun removeAudioListener(audioFrameListener: AudioFrameListener)

    external fun addAudioCursorListener(audioFrameListener: AudioCursorListener)

    external fun setDefaultStreamValues(sampleRate: Int, framesPerBurst: Int)
    external fun setAudioBuffer(buffer: FloatArray)

    open external fun addSoundInput(id: Int, frequency: Float)
    open external fun changeSoundInputFrequency(id: Int, frequency: Float)
    open external fun removeSoundInput(id: Int)
}