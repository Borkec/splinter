package com.sintegra.splinter.model


data class Key(val type: KeyType)

enum class KeyColor {
    White, Black
}

enum class KeyType {
    C, CSharp, D, DSharp, E, F, FSharp, G, GSharp, A, ASharp, B;

    fun getFrequency(): Float {
        return when (this) {
            C -> 261.63f
            CSharp -> 277.18f
            D -> 293.66f
            DSharp -> 311.13f
            E -> 329.63f
            F -> 349.23f
            FSharp -> 369.99f
            G -> 392.00f
            GSharp -> 415.30f
            A -> 440.00f
            ASharp -> 466.16f
            B -> 493.88f
        }
    }
}

val Octave = listOf(
    Key(KeyType.C),
    Key(KeyType.CSharp),
    Key(KeyType.D),
    Key(KeyType.DSharp),
    Key(KeyType.E),
    Key(KeyType.F),
    Key(KeyType.FSharp),
    Key(KeyType.G),
    Key(KeyType.GSharp),
    Key(KeyType.A),
    Key(KeyType.ASharp),
    Key(KeyType.B)
)