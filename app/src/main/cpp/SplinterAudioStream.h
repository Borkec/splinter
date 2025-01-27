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

#ifndef SIMPLE_NOISE_MAKER_H
#define SIMPLE_NOISE_MAKER_H

#include "oboe/Oboe.h"
#include "jni.h"
#include "WaveGenerator.h"
#include "bridge/NativeCallback.h"

/**
 * Play white noise using Oboe.
 */
class SplinterAudioStream {
public:

    SplinterAudioStream();

    /**
     * Open an Oboe stream.
     * @return OK or negative error code.
     */
    oboe::Result open();

    oboe::Result start();

    oboe::Result stop();

    oboe::Result close();

    void addWaveGeneratorCallback(NativeCallback* callback);

    void setGeneratorBuffer(const float* data);

    void setCursorCallback(NativeCallback* callback);

    void setFrequency(float f) const;

    int getWavetableSize() const;

    void setWavetableSize(int size);

private:

    class MyDataCallback : public oboe::AudioStreamDataCallback {
    public:
        float frequency = 420;

        MyDataCallback(SplinterAudioStream *parent) : mParent(parent) {}

        oboe::DataCallbackResult onAudioReady(
                oboe::AudioStream *audioStream,
                void *audioData,
                int32_t numFrames) override;

    private:
        size_t currentIdx = 0;
        SplinterAudioStream *mParent;
    };

    class MyErrorCallback : public oboe::AudioStreamErrorCallback {
    public:

        void onErrorAfterClose(oboe::AudioStream *oboeStream, oboe::Result error) override;

    };

    std::shared_ptr<oboe::AudioStream> mStream;
    std::shared_ptr<MyErrorCallback> mErrorCallback;
    std::shared_ptr<WaveGenerator> waveGenerator;

    NativeCallback* cursorCallback;

    static constexpr int kChannelCount = 2;

    int wavetableSize = TABLE_SIZE;
public:

    std::shared_ptr<MyDataCallback> mDataCallback;
};

#endif //SIMPLE_NOISE_MAKER_H