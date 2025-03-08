package com.sintegra.splinter.model


data class Key(val type: KeyType)

enum class KeyType { C, CSharp, D, DSharp, E, F, FSharp, G, GSharp, A, ASharp, B }
enum class KeyColor { White, Black }

fun KeyType.getFrequency(): Float {
    return when (this) {
        KeyType.C -> 261.63f
        KeyType.CSharp -> 277.18f
        KeyType.D -> 293.66f
        KeyType.DSharp -> 311.13f
        KeyType.E -> 329.63f
        KeyType.F -> 349.23f
        KeyType.FSharp -> 369.99f
        KeyType.G -> 392.00f
        KeyType.GSharp -> 415.30f
        KeyType.A -> 440.00f
        KeyType.ASharp -> 466.16f
        KeyType.B -> 493.88f
    }
}
