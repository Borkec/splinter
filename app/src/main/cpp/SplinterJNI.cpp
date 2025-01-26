
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

#include <jni.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

static const char *TAG = "MinimalOboeJNI";

#include <android/log.h>
#include "bridge/jni_utils.h"
#include "SplinterAudioStream.h"
#include "bridge/NativeCallback.h"

// JNI functions are "C" calling convention
#ifdef __cplusplus
extern "C" {
#endif

using namespace oboe;

// Use a static object so we don't have to worry about it getting deleted at the wrong time.
static SplinterAudioStream sPlayer;

/**
 * Native (JNI) implementation of AudioPlayer.startAudiostreamNative()
 */
JNIEXPORT void JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_initializeBridge(
        JNIEnv* env, jobject) {
    // Save javaVM globally
    env->GetJavaVM(&javaVM);
    sPlayer.open();
}

JNIEXPORT jint JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_openAudioStream(
        JNIEnv* env, jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);

    auto result = sPlayer.open();
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_closeAudioStream(
        JNIEnv* env, jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);

    auto result = sPlayer.close();
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_startAudioStream(
        JNIEnv* env, jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);

    auto result = sPlayer.start();
    return (jint) result;
}

JNIEXPORT jint JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_stopAudioStream(
        JNIEnv * /* env */, jobject) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", __func__);
    // We need to close() even if the stop() fails because we need to delete the resources.
    Result result1 = sPlayer.stop();
    // Return first failure code.
    return (jint) result1;
}

JNIEXPORT void JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_addAudioListener(
        JNIEnv* env, jobject, jobject listener) {
    //env->NewGlobalRef(listener);
    __android_log_print(ANDROID_LOG_INFO, TAG, "Setting %s", __func__);

    auto nativeCallback = new NativeCallback(listener, "onAudioDataAvailable", "([F)V");
    sPlayer.addWaveGeneratorCallback(nativeCallback);
}

JNIEXPORT void JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_addAudioCursorListener(
        JNIEnv* env, jobject, jobject listener) {
    //env->NewGlobalRef(listener);
    __android_log_print(ANDROID_LOG_INFO, TAG, "Setting %s", __func__);

    auto nativeCallback = new NativeCallback(listener, "onAudioCursorAvailable", "(I)V");
    sPlayer.setCursorCallback(nativeCallback);
}

JNIEXPORT void JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_setAudioBuffer(
        JNIEnv* env, jobject, jfloatArray array) {
    //env->NewGlobalRef(listener);
    __android_log_print(ANDROID_LOG_INFO, TAG, "Setting %s", __func__);
    float* buf = env->GetFloatArrayElements(array, nullptr);
    sPlayer.setGeneratorBuffer(buf);
}

JNIEXPORT jint JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_getTableSize(
        JNIEnv* env, jobject) {
    return (jint)TABLE_SIZE;
}

JNIEXPORT void JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_removeAudioListener(
        JNIEnv* env, jobject, jobject listener) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Setting %s", __func__);
    env->DeleteGlobalRef(listener);
}

JNIEXPORT void JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_setDefaultStreamValues(
        JNIEnv* env, jobject,
            jint sampleRate,
            jint framesPerBurst
        ) {
    __android_log_print(ANDROID_LOG_INFO, TAG, "Setting %s", __func__);
    oboe::DefaultStreamValues::SampleRate = (int32_t) sampleRate;
    oboe::DefaultStreamValues::FramesPerBurst = (int32_t) framesPerBurst;
}

JNIEXPORT void JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_setSineFrequency(
        JNIEnv* env, jobject,
        jfloat freq
) {
    sPlayer.mDataCallback->frequency = freq;
}

JNIEXPORT void JNICALL Java_com_sintegra_splinter_data_service_NativeAudioBridge_setSecondOffset(
        JNIEnv* env, jobject,
        jfloat freq
) {
    sPlayer.mDataCallback->offset = freq;
}

#ifdef __cplusplus
}
#endif


