package com.sintegra.splinter.model

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin

data class WaveModel(val waveType: WaveType, val customAudioData: List<Float>? = null) {

    val audioData: FloatArray = generate(WAVETABLE_SIZE)

    private fun generate(size: Int): FloatArray {
        if (customAudioData != null) return customAudioData.toFloatArray()

        val array = FloatArray(size)

        for (i in 0 until size) {
            waveType.generator(array, i, size)
        }
        return array
    }

    companion object {
        val DEFAULT = WaveModel(WaveType.SINE)
    }
}

enum class WaveType(val generator: (FloatArray, Int, Int) -> Unit) {
    SINE(
        { array, i, size ->
            array[i] = sin(i.toFloat() / size.toFloat() * 2 * PI).toFloat()
        }
    ),

    SQUARE(
        { array, i, size ->
            if (i > size / 2) {
                array[i] = 1f
            } else {
                array[i] = -1f
            }
        }
    ),
    SAWTOOTH(
        { array, i, size ->
            array[i] = (i.toFloat() / size) * 2 - 1;
        }
    ),

    TRIANGLE(
        { array, i, size ->
            if (i < size / 2) {
                array[i] = (i.toFloat() / (size / 2)) * 2 - 1
            } else {
                array[i] = 1 - (i.toFloat() / (size / 2) - 1) * 2
            }
        }
    ),

    NOISE(
        { array, i, _ ->
            array[i] = (Math.random().toFloat() * 2 - 1)
        }
    ),

    WOBBLE(
        { array, i, size ->
            val normalized = i.toFloat() / size
            array[i] = (sin(2 * PI * normalized) * cos(4 * PI * normalized)).toFloat()
        }
    ),

    BURST(
        { array, i, size ->
            val segments = 5
            val segmentSize = size / segments
            array[i] = if ((i / segmentSize) % 2 == 0) {
                sin(i.toFloat() / size * 2 * PI).toFloat()
            } else {
                0f
            }
        }
    ),

    FRACTAL(
        { array, i, size ->
            val normalized = i.toFloat() / size
            array[i] = (sin(2 * PI * normalized) + sin(8 * PI * normalized) / 2).toFloat()
        }
    ),

    GLITCH(
        { array, i, size ->
            array[i] = if (i % (size / 20) in 0..5) 1f else sin(i.toFloat() / size * 2 * PI).toFloat()
        }
    ),

    PULSE(
        { array, i, size ->
            val freq = 5 // Number of pulses
            array[i] = (sin(freq * 2 * PI * i.toFloat() / size) * exp(-3 * i.toFloat() / size)).toFloat()
        }
    ),

    LOG_SPIRAL(
        { array, i, size ->
            val angle = i.toFloat() / size * 2 * PI
            val radius = ln(i.toDouble() + 1) / ln(size.toDouble())
            array[i] = sin(angle * radius).toFloat()
        }
    ),

    // A wave that generates a chaotic pattern using a logistic map
    CHAOTIC(
        { array, i, size ->
            var x = 0.5f // Initial value
            val r = 3.7f // Chaos parameter
            repeat(i) {
                x *= r * (1 - x) // Logistic map equation
            }
            array[i] = x * 2 - 1 // Scale to [-1, 1]
        }
    ),

    // A wave that creates a fractal-like pattern using the Weierstrass function
    WEIERSTRASS(
        { array, i, size ->
            val a = 0.5f
            val b = 3f
            var sum = 0f
            for (n in 0..10) {
                sum += sin((b.toDouble().pow(n) * i / size * 2 * PI).toFloat()) * a.toDouble().pow(n).toFloat()
            }
            array[i] = sum
        }
    ),

    // A wave that generates a random walk pattern
    RANDOM_WALK(
        { array, i, size ->
            if (i == 0) {
                array[i] = 0f
            } else {
                array[i] = array[i - 1] + (Math.random().toFloat() * 2 - 1) * 0.1f
                array[i] = array[i].coerceIn(-1f, 1f) // Clamp to [-1, 1]
            }
        }
    ),

    // A wave that creates a heartbeat-like pattern
    HEARTBEAT(
        { array, i, size ->
            val pulseWidth = size / 10
            if (i % pulseWidth < pulseWidth / 2) {
                array[i] = sin(i.toFloat() / pulseWidth * 2 * PI).toFloat()
            } else {
                array[i] = 0f
            }
        }
    ),


    CUSTOM({ array, i, size ->
        array[i] = 0f
    })
}