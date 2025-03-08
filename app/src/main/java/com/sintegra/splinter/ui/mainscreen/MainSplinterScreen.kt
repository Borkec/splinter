package com.sintegra.splinter.ui.mainscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sintegra.splinter.model.WaveType
import com.sintegra.splinter.ui.viewmodel.SelectedWaveViewState
import com.sintegra.splinter.ui.viewmodel.WavePickerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainSplinterScreen(
    switchState: Boolean = false,
    onSwitchChange: () -> Unit = {},
    viewModel: WavePickerViewModel = koinViewModel(),
    onCustomWaveScreenOpened: () -> Unit = {},
    content: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val selectedWave by viewModel.selectedWaveViewState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    SharedTransitionLayout {
        AnimatedContent(
            targetState = expanded,
            label = "wave picker transition",
            transitionSpec = {
                (fadeIn(animationSpec = tween(220, delayMillis = 40)))
                    .togetherWith(fadeOut(animationSpec = tween(40)))
            }
        ) {
            Column(
                modifier = Modifier
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!it) {
                        Switch(
                            modifier = Modifier.rotate(90f),
                            checked = switchState,
                            onCheckedChange = {
                                onSwitchChange()
                            }
                        )

                        WavePickerElement(
                            selectedWave = selectedWave,
                            onClick = { expanded = true },
                            onCustomWaveClicked = onCustomWaveScreenOpened,
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(selectedWave.waveType.name),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                        )
                    } else {
                        LazyColumn(
                            modifier = modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Top,
                            state = lazyListState
                        ) {
                            items(items = SelectedWaveViewState.common, key = { it.waveType }) { waveState ->
                                WavePickerElement(
                                    selectedWave = waveState,
                                    onClick = {
                                        viewModel.onWaveTypeSelected(waveState.waveType)
                                        expanded = false
                                    },
                                    onCustomWaveClicked = onCustomWaveScreenOpened,
                                    Modifier.sharedElement(
                                        state = rememberSharedContentState(waveState.waveType.name),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                                )
                            }
                        }
                    }
                }

                content()
            }
        }
    }

}

@Composable
fun WavePickerElement(
    selectedWave: SelectedWaveViewState,
    onClick: () -> Unit,
    onCustomWaveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val customClickable = if (selectedWave.waveType == WaveType.CUSTOM) {
        Modifier.clickable { onCustomWaveClicked() }
    } else Modifier

    Row(
        modifier = modifier
            .height(80.dp)
            .padding(start = 8.dp, top = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .border(width = 2.dp, color = MaterialTheme.colors.background, shape = RoundedCornerShape(6.dp))
                .background(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(6.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                fontSize = 16.sp,
                text = selectedWave.waveName,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.background
            )
        }

        WaveGraph(
            points = selectedWave.waveData,
            modifier = Modifier
                .weight(2f)
                .then(customClickable)
        )
    }
}

@Preview
@Composable
fun WavePickerPreview(modifier: Modifier = Modifier) {
    MainSplinterScreen()
}