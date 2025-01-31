package com.sintegra.splinter.model

/**
 * Modulation represents a property that can be mapped to an input to modulate the sound.
 *
 * For example, frequency modulation with [minValue] set to 0.0 and [maxValue] set to 1000.0 corresponds to a frequency modulation
 * range from 0 Hz to 1000 Hz. This can then be mapped to any surface gradient so that each end of the gradient
 * corresponds to ends of these min-max values, with values linearly changing in between (e.g. a touch input to frequency map).
 */
data class Modulation(
    val type: ModulationType,
    val minValue: Float,
    val maxValue: Float
)

enum class ModulationType {
    FREQUENCY
}

/**
 * Interpolates the [value] that is in range [0, 1] to the range [Modulation.minValue], [Modulation.maxValue]
 */
fun Modulation.interpolateValue(value: Float) = ((maxValue-minValue)*value) + minValue