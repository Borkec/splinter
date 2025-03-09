//
// Created by Borko on 07.02.2025..
//

#include <android/log.h>
#include "oboe/Oboe.h"
#include "AudioProcessingUnit.h"
#include "WaveGenerator.h"
#include "log_util.h"


AudioProcessingUnit::AudioProcessingUnit(int channelCount) {
    framesProcessed = 0;
    playTime = 0;
    this->channelCount = channelCount;

    waveGenerator = std::make_shared<WaveGenerator>(TABLE_SIZE);
    envelopeFilter = new EnvelopeFilter();
}


AudioProcessingUnit::AudioProcessingUnit(int channelCount, size_t tableSize) {
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

    std::fill(output, output + numFrames * channelCount, 0);

    if (soundInputs.empty()) {
        return oboe::DataCallbackResult::Continue;
    }

    int sampleRate = audioStream->getSampleRate();

    for (int i = 0; i < numFrames; i++) {

        for (int j = 0; j < channelCount; j++) {
            for (auto &[id, soundInput]: soundInputs) {
                *output += *(wave + soundInput.currentIdx) * 1.0f/(soundInputs.size()*2.0f);
            }
            output++;
        }

        framesProcessed++;
        playTime = (float) framesProcessed / sampleRate;
        envelopeFilter->updateCurrentTime(playTime);
        for (auto &[id, soundInput]: soundInputs) {
            soundInput.updatePhase(waveSize, sampleRate);
        }
    }
    LOG_I(soundInputs.size());
    return oboe::DataCallbackResult::Continue;
}

void AudioProcessingUnit::playNote() {
    envelopeFilter->startAttack(playTime);
}

void AudioProcessingUnit::releaseNote() {
    envelopeFilter->startRelease(playTime);
}


