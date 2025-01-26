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
#include <generators/SineWave.h>
#include "generators/SquareWave.h"
#include "generators/TriangleWave.h"
#include "bridge/NativeCallback.h"
#include "bridge/jni_utils.h"

using namespace oboe;

SplinterAudioStream::SplinterAudioStream() {

    waveGenerator = std::make_shared<WaveGenerator>();
}

oboe::Result SplinterAudioStream::open() {

    AudioStreamBuilder builder = AudioStreamBuilder();

    // Use shared_ptr to prevent use of a deleted callback.
    mDataCallback = std::make_shared<MyDataCallback>(this);
    mErrorCallback = std::make_shared<MyErrorCallback>();

    oboe::Result result = builder.setSharingMode(oboe::SharingMode::Exclusive)
            ->setPerformanceMode(oboe::PerformanceMode::LowLatency)
            ->setFormat(oboe::AudioFormat::Float)
            ->setChannelCount(kChannelCount)
            ->setDataCallback(mDataCallback)
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

void SplinterAudioStream::setFrequency(float f) const {
    mDataCallback->frequency = f;
}

void SplinterAudioStream::setCursorCallback(NativeCallback *callback) {
    cursorCallback = callback;
}

/**
 * This callback method will be called from a high priority audio thread.
 * It should only do math and not do any blocking operations like
 * reading or writing files, memory allocation, or networking.
 * @param audioStream
 * @param audioData pointer to an array of samples to be filled
 * @param numFrames number of frames needed
 * @return
 */
DataCallbackResult SplinterAudioStream::MyDataCallback::onAudioReady(
        AudioStream *audioStream,
        void *audioData,
        int32_t numFrames) {

    auto wave = mParent->waveGenerator->getData();
    size_t waveSize = WaveGenerator::size;
    // We requested float when we built the stream.
    auto output = (float *) audioData;
    int sr = audioStream->getSampleRate();
    int phaseIncrement = frequency * waveSize / sr;

    for (int i = 0; i < numFrames; i++) {
        for (int j = 0; j < kChannelCount; j++) {
            *output++ = *(wave + currentIdx);
        }

        if (mParent->cursorCallback != nullptr) {
            mParent->cursorCallback->call((int) currentIdx);
        }

        currentIdx = (currentIdx + phaseIncrement) % waveSize;
    }

    return oboe::DataCallbackResult::Continue;
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

    waveGenerator->addOnDataChangedListener(listener);
}

void SplinterAudioStream::setGeneratorBuffer(const float *data) {
    waveGenerator->fill(data);
}