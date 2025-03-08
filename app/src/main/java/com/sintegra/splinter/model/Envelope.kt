package com.sintegra.splinter.model

data class Envelope(
    val attackTime: Float,  // In seconds
    val decayTime: Float,   // In seconds
    val sustainLevel: Float, // Between 0.0 and 1.0
    val releaseTime: Float  // In seconds
)

fun Envelope.generateEnvelope(bufferSize: Int, sampleRate: Int): FloatArray {
    val envelopeValues = FloatArray(bufferSize)
    val attackSamples = (attackTime * sampleRate).toInt()
    val decaySamples = (decayTime * sampleRate).toInt()
    val releaseSamples = (releaseTime * sampleRate).toInt()

    for (i in 0 until bufferSize) {
        envelopeValues[i] = when {
            i < attackSamples -> i / attackSamples.toFloat() // Linear attack
            i < attackSamples + decaySamples -> {
                val decayProgress = (i - attackSamples) / decaySamples.toFloat()
                1.0f - decayProgress * (1.0f - sustainLevel)
            }
            i < bufferSize - releaseSamples -> sustainLevel // Sustain phase
            else -> {
                val releaseProgress = (i - (bufferSize - releaseSamples)) / releaseSamples.toFloat()
                sustainLevel * (1.0f - releaseProgress)
            }
        }
    }
    return envelopeValues
}