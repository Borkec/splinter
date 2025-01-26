package com.sintegra.splinter.ui.mainscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.ui.viewmodel.MainViewSate

@Composable
fun WavePicker(selectedWave: MainViewSate.SelectedWave, onWavePicked: (WaveType) -> Unit, modifier: Modifier = Modifier) {

    var expanded by remember { mutableStateOf(false) }


    AnimatedVisibility(!expanded, enter = fadeIn(), exit = fadeOut())  {
        WavePickerElement(selectedWave, onClick = { expanded = true })
    }
    AnimatedVisibility(expanded, enter = fadeIn(), exit = fadeOut())  {
        LazyColumn(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top) {
            items(MainViewSate.SelectedWave.common) { waveState ->
                WavePickerElement(waveState, onClick = {
                    onWavePicked(waveState.waveType)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun WavePickerElement(selectedWave: MainViewSate.SelectedWave, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(80.dp)
            .padding(start = 8.dp, top = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .border(2.dp, Color.LightGray, RoundedCornerShape(6.dp))
                .clickable { onClick() }
                .drawBehind {

                    drawCircle(Color.White, radius = 6f, center = Offset(size.center.x + size.width/2, size.center.y))
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                fontSize = 16.sp,
                text = selectedWave.waveName,
                textAlign = TextAlign.Center
            )
        }

        WaveGraph(selectedWave.waveData, Modifier.weight(2f))
    }
}

@Preview
@Composable
fun WavePickerPreview(modifier: Modifier = Modifier) {
    WavePicker(MainViewSate.SelectedWave.initial, {})
}