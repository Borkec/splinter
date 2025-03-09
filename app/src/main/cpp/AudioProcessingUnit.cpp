//
// Created by Borko on 07.02.2025..
//

#include <android/log.h>
#include "oboe/Oboe.h"
#include "AudioProcessingUnit.h"
#include "WaveGenerator.h"
#include "log_util.h"


AudioProcessingUnit::AudioProcessingUnit(int channelCount) {
    isPlaying = false;
    currentIdx = 0;
    framesProcessed = 0;
    playTime = 0;
    this->channelCount = channelCount;

    waveGenerator = std::make_shared<WaveGenerator>(TABLE_SIZE);
    envelopeFilter = new EnvelopeFilter();
}


AudioProcessingUnit::AudioProcessingUnit(int channelCount, size_t tableSize) {
    isPlaying = false;
    currentIdx = 0;
    framesProcessed = 0;
    playTime = 0;
    this->channelCount = channelCount;

    waveGenerator = std::make_shared<WaveGenerator>(tableSize);
    envelopeFilter = new EnvelopeFilter();
}

AudioProcessingUnit::~AudioProcessingUnit() {
    delete envelopeFilter;
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
oboe::DataCallbackResult AudioProcessingUnit::onAudioReady(
        oboe::AudioStream *audioStream,
        void *audioData,
        int32_t numFrames) {

    auto wave = waveGenerator->getData();
    size_t waveSize = waveGenerator->getTableSize();

    // We requested float when we built the stream.
    auto output = (float *) audioData;

    if(!isPlaying) {
        std::fill( output, output + numFrames*channelCount, 0);
        return oboe::DataCallbackResult::Continue;
    }

    int sampleRate = audioStream->getSampleRate();
    int phaseIncrement = frequency * waveSize / sampleRate;

    for (int i = 0; i < numFrames; i++) {
        float envelopeCoef = 1.; // envelopeFilter->getEnvelopeForCurrentTime();

        for (int j = 0; j < channelCount; j++) {
            *output++ = *(wave + currentIdx)  * envelopeCoef;
        }

        framesProcessed++;
        playTime = (float)framesProcessed / sampleRate;
        envelopeFilter->updateCurrentTime(playTime);

        currentIdx = (currentIdx + phaseIncrement) % waveSize;
    }

//    __android_log_print(ANDROID_LOG_INFO, "TAG",
//                        "PlayTime: %f",
//                        playTime
//    );

    return oboe::DataCallbackResult::Continue;
}

void AudioProcessingUnit::playNote() {
    isPlaying = true;
    envelopeFilter->startAttack(playTime);
}

void AudioProcessingUnit::releaseNote() {
    isPlaying = false;
    envelopeFilter->startRelease(playTime);
}

void AudioProcessingUnit::resetTime() {
    framesProcessed = 0;
    playTime = 0;
}


