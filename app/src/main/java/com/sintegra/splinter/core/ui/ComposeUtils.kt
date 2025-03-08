package com.sintegra.splinter.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Int.toDp(): Dp = let {
    LocalDensity.current.run { it.toDp() }
}