package com.sintegra.splinter.model

import kotlin.math.PI
import kotlin.math.sin

data class WaveModel(val waveType: WaveType) {

    fun generate(size: Int): FloatArray {
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
    );
}