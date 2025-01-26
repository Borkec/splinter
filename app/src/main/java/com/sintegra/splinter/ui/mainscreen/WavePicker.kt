package com.sintegra.splinter.ui.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sintegra.splinter.model.WaveModel
import com.sintegra.splinter.model.WaveType

@Composable
fun WavePicker(waveModel: WaveModel, onWavePicked: (WaveType) -> Unit, modifier: Modifier = Modifier) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .height(50.dp)
            .wrapContentWidth()
            .background(Color.DarkGray)
            .border(2.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .clickable { expanded = !expanded }
    ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                fontSize = 16.sp,
                text = waveModel.waveType.toString().capitalize(Locale.current)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            WaveType.entries.forEach { waveType ->
                DropdownMenuItem(
                    modifier = Modifier
                        .background(Color.DarkGray),
                    onClick = {
                        onWavePicked(waveType)
                        expanded = false
                    }
                ) {
                    Text(
                        fontSize = 16.sp,
                        text = waveType.name
                    )
                }
            }
        }
    }
}