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

#include <cstdlib>

static const char *TAG = "SineAudioStream";

#include <android/log.h>
#include <cmath>
#include <SplinterAudioStream.h>
#include "bridge/NativeCallback.h"
#include "bridge/jni_utils.h"
#include "AudioProcessingUnit.h"

using namespace oboe;

oboe::Result SplinterAudioStream::open() {

    AudioStreamBuilder builder = AudioStreamBuilder();

    mErrorCallback = std::make_shared<MyErrorCallback>();

    oboe::Result result = builder.setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::LowLatency)
            ->setFormat(oboe::AudioFormat::Float)
            ->setChannelCount(kChannelCount)
            ->setDataCallback(audioProcessingUnit)
            ->setErrorCallback(mErrorCallback)
            ->openStream(mStream);

    return result;
}

oboe::Result SplinterAudioStream::start() {
    return mStream->requestStart();
}

oboe::Result SplinterAudioStream::stop() {
    mStream->flush();
    return mStream->requestStop();
}

oboe::Result SplinterAudioStream::close() {
    return mStream->close();
}

int SplinterAudioStream::getWavetableSize() const {
    return wavetableSize;
}

void SplinterAudioStream::setWavetableSize(int size) {
    wavetableSize = size;
}

void SplinterAudioStream::MyErrorCallback::onErrorAfterClose(oboe::AudioStream *oboeStream,
                                                             oboe::Result error) {
    __android_log_print(ANDROID_LOG_INFO, TAG,
                        "%s() - error = %s",
                        __func__,
                        oboe::convertToText(error)
    );
}

void SplinterAudioStream::addWaveGeneratorCallback(NativeCallback *callback) {

    std::function<void(const float *, const size_t)> listener = [callback](const float *data, const size_t size) {
        __android_log_print(ANDROID_LOG_INFO, "Test", "Calling callback, %s", __func__);

        callback->call(data, size);
    };

    audioProcessingUnit->getWaveGenerator().addOnDataChangedListener(listener);
}

void SplinterAudioStream::setGeneratorBuffer(const float *data) {
    audioProcessingUnit->getWaveGenerator().fill(data);
}