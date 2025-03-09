package com.sintegra.splinter.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sintegra.splinter.data.repository.AudioRepository
import com.sintegra.splinter.model.Key
import com.sintegra.splinter.model.KeyColor
import com.sintegra.splinter.model.KeyType
import com.sintegra.splinter.model.Octave
import kotlin.math.pow

class PianoKeysViewModel(private val audioRepository: AudioRepository) : ViewModel() {

    fun onPlayKey(keyType: KeyType, octaveLevel: Int) {
        audioRepository.setSineFrequency(keyType.getFrequency() * 2f.pow(octaveLevel - 1))
        audioRepository.playNote()
    }

    fun onReleaseKey() {
        audioRepository.releaseNote()
    }
}

data class KeyViewState(
    val type: KeyType,
    val keyColor: KeyColor,
    val relativeY: Float
) {
    companion object {
        fun fromKey(key: Key) =
            when (key.type) {
                KeyType.C -> KeyViewState(KeyType.C, KeyColor.White, 0f)
                KeyType.CSharp -> KeyViewState(KeyType.CSharp, KeyColor.Black, 1 / 14f)
                KeyType.D -> KeyViewState(KeyType.D, KeyColor.White, 1 / 7f)
                KeyType.DSharp -> KeyViewState(KeyType.DSharp, KeyColor.Black, 3 / 14f)
                KeyType.E -> KeyViewState(KeyType.E, KeyColor.White, 2 / 7f)
                KeyType.F -> KeyViewState(KeyType.F, KeyColor.White, 3 / 7f)
                KeyType.FSharp -> KeyViewState(KeyType.FSharp, KeyColor.Black, 1 / 2f)
                KeyType.G -> KeyViewState(KeyType.G, KeyColor.White, 4 / 7f)
                KeyType.GSharp -> KeyViewState(KeyType.GSharp, KeyColor.Black, 9 / 14f)
                KeyType.A -> KeyViewState(KeyType.A, KeyColor.White, 5 / 7f)
                KeyType.ASharp -> KeyViewState(KeyType.ASharp, KeyColor.Black, 11 / 14f)
                KeyType.B -> KeyViewState(KeyType.B, KeyColor.White, 6 / 7f)
            }

        val OctaveViewState = Octave.map(::fromKey)
    }
}