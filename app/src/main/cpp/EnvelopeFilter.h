//
// Created by Borko on 25.01.2025..
//

#include <cstddef>
#include <functional>
#include <thread>
#include "const.h"

#ifndef SPLINTER_ENVELOPEFILTER_H
#define SPLINTER_ENVELOPEFILTER_H

class EnvelopeFilter {

public:
    EnvelopeFilter();

    ~EnvelopeFilter();

    float getEnvelopeForCurrentTime();

    void startAttack(float startTime);

    void startRelease(float releaseStartTime);

    void updateCurrentTime(float seconds);

    void updateState();

private:

    enum FilterState {
        ATTACK,
        DECAY,
        SUSTAIN,
        RELEASE,
        STOP
    };

    std::thread workerThread;

    float _startTime = 0;
    float _releaseStartTime = 0;
    float currentTime = 0;
    float releaseDeltaTime = 0; // seconds

    float attackTime = 0.1; // seconds
    float decayTime = 1.0; // seconds
    float sustainLevel = 1; // between 0.0 and 1.0
    float releaseTime = 5; // seconds

    bool stopWorker = false;

    FilterState currentState = FilterState::STOP;
};

#endif