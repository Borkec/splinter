package com.sintegra.splinter.model

import java.util.UUID

data class SoundInput(
    val id: Int = UUID.randomUUID().hashCode(),
    val frequency: Float
)