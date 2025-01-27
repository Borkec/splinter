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
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.sintegra.splinter.data.service.NativeAudioBridge
import com.sintegra.splinter.ui.mainscreen.MainScreen
import com.sintegra.splinter.ui.theme.SplinterTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        NativeAudioBridge.openAudioStream()
        setDefaultStreamValues()


        setContent {
            SplinterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color =  MaterialTheme.colors.background.also { Log.d("debug_log", "$it") }
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onDestroy() {
        super.onDestroy()
        NativeAudioBridge.closeAudioStream()
    }

    private fun setDefaultStreamValues() {
        val myAudioMgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val sampleRateStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
        val defaultSampleRate = sampleRateStr.toInt()
        val framesPerBurstStr = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
        val defaultFramesPerBurst = framesPerBurstStr.toInt()

        NativeAudioBridge.setDefaultStreamValues(defaultSampleRate, defaultFramesPerBurst)
    }
}