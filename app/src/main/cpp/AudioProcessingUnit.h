//
// Created by Borko on 07.02.2025..
//

#include "oboe/Oboe.h"
#include "WaveGenerator.h"
#include "EnvelopeFilter.h"

#ifndef SPLINTER_AUDIOPROCESSINGUNIT_H
#define SPLINTER_AUDIOPROCESSINGUNIT_H

class AudioProcessingUnit : public oboe::AudioStreamDataCallback {
public:

    AudioProcessingUnit(int channelCount);

    AudioProcessingUnit(int channelCount, size_t tableSize);

    ~AudioProcessingUnit();

    void setFrequency(float f) {
        frequency = f;
    };

    void playNote();

    void releaseNote();

    WaveGenerator& getWaveGenerator() {
        return *waveGenerator;
    }

    oboe::DataCallbackResult onAudioReady(
            oboe::AudioStream *audioStream,
            void *audioData,
            int32_t numFrames) override;

private:

    void resetTime();

    size_t currentIdx;
    int framesProcessed;
    float playTime;
    bool isPlaying;
    int channelCount;
    float frequency;

    std::shared_ptr<WaveGenerator> waveGenerator;
    EnvelopeFilter* envelopeFilter;

    enum PlayState {
        ATTACK,
        DECAY,
        SUSTAIN,
        RELEASE,
        STOP
    };
};

#endif //SPLINTER_AUDIOPROCESSINGUNIT_H
