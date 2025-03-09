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

    void addSoundInput(int id, float f) {
        auto found = soundInputs.find(id);
        if(found == soundInputs.end()) {
            soundInputs[id] = SoundInput(id, f);
        } else {
            soundInputs[id].frequency = f;
        }
    };

    void changeSoundInputFrequency(int id, float f) {
        soundInputs[id].frequency = f;
    }

    void removeSoundInput(int id) {
        soundInputs.erase(id);
    };

    void playNote();

    void releaseNote();

    WaveGenerator &getWaveGenerator() {
        return *waveGenerator;
    }

    oboe::DataCallbackResult onAudioReady(
            oboe::AudioStream *audioStream,
            void *audioData,
            int32_t numFrames) override;

private:

    struct SoundInput {
        int id;
        float frequency;
        int currentIdx = 0;
        int phaseIncrement = 0;

        SoundInput() : id(0), frequency(0) {}

        SoundInput(int _id, float _freq) : id(_id), frequency(_freq) {}

        void updatePhase(int waveSize, int sampleRate) {
            phaseIncrement = frequency * waveSize / sampleRate;
            currentIdx = (currentIdx + phaseIncrement) % waveSize;
        }
    };

    int framesProcessed;
    float playTime;
    int channelCount;
    std::unordered_map<int, SoundInput> soundInputs;

    std::shared_ptr<WaveGenerator> waveGenerator;
    EnvelopeFilter *envelopeFilter;

    enum PlayState {
        ATTACK,
        DECAY,
        SUSTAIN,
        RELEASE,
        STOP
    };
};

#endif //SPLINTER_AUDIOPROCESSINGUNIT_H
