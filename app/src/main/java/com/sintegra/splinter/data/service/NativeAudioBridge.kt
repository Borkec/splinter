package com.sintegra.splinter.data.service

object NativeAudioBridge {

    external fun initializeBridge(size: Int)
    external fun getTableSize(): Int

    external fun openAudioStream(): Int
    external fun closeAudioStream(): Int
    external fun startAudioStream(): Int
    external fun stopAudioStream(): Int

    external fun addAudioListener(audioFrameListener: AudioFrameListener)
    external fun removeAudioListener(audioFrameListener: AudioFrameListener)

    external fun addAudioCursorListener(audioFrameListener: AudioCursorListener)

    external fun setDefaultStreamValues(sampleRate: Int, framesPerBurst: Int)
    external fun setFrequency(frequency: Float)
    external fun setAudioBuffer(buffer: FloatArray)
}