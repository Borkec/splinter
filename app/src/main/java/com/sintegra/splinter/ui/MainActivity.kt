/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sintegra.splinter.ui

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.sintegra.splinter.data.service.NativeAudioBridge
import com.sintegra.splinter.ui.navigation.MainController
import com.sintegra.splinter.ui.theme.SplinterTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val nativeAudioBridge: NativeAudioBridge by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        nativeAudioBridge.openAudioStream()
        nativeAudioBridge.startAudioStream()
        setDefaultStreamValues()


        setContent {
            SplinterTheme {
                Surface(
                    modifier = Modifier,
                    color =  MaterialTheme.colors.background
                ) {
                    MainController()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nativeAudioBridge.startAudioStream()
    }

    override fun onStop() {
        super.onStop()
        nativeAudioBridge.stopAudioStream()
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeAudioBridge.stopAudioStream()
        nativeAudioBridge.closeAudioStream()
    }

    private fun setDefaultStreamValues() {
        val myAudioMgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
        val defaultSampleRate = sampleRateStr.toInt()
        val framesPerBurstStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
        val defaultFramesPerBurst = framesPerBurstStr.toInt()

        nativeAudioBridge.setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
    }
}