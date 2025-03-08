//
// Created by Borko on 25.01.2025..
//

#include <cstddef>
#include <functional>
#include <EnvelopeFilter.h>
#include <thread>
#include <android/log.h>

EnvelopeFilter::EnvelopeFilter() {
    workerThread = std::thread(&EnvelopeFilter::updateState, this);
}

EnvelopeFilter::~EnvelopeFilter() {
    stopWorker = true;
    if(workerThread.joinable()) {
        workerThread.join();
    }
}


void EnvelopeFilter::updateState() {
    while(!stopWorker) {
        if(currentTime == 0) {
            currentState = STOP;
        }
        else if(currentTime < attackTime) {
            currentState = ATTACK;
        }
        else if(currentTime < attackTime + decayTime) {
            currentState = DECAY;
        } else if(releaseDeltaTime > releaseTime) {
            currentState = STOP;
            releaseDeltaTime = 0;
        } else if(releaseDeltaTime > 0) {
            currentState = RELEASE;
        }  else {
            currentState = SUSTAIN;
        }
    }
}



float EnvelopeFilter::getEnvelopeForCurrentTime() {
    if(currentState == ATTACK) {
        return currentTime/attackTime;
    }
    else if(currentState == DECAY) {
        float point =  (currentTime-attackTime) / decayTime;
        return 1.0f - point * (1.0f - sustainLevel);
    }
    else if (currentState == SUSTAIN)
        return sustainLevel;
    else if(currentState == RELEASE) {
        float point = releaseDeltaTime / releaseTime;
        return sustainLevel * (1.0f - point);
    } else {
        return 0;
    }
}

void EnvelopeFilter::updateCurrentTime(float seconds) {

    if(currentState == RELEASE) {
        releaseDeltaTime = seconds - _releaseStartTime;
    } else {
        currentTime = seconds - _startTime;
    }
}

void EnvelopeFilter::startAttack(float startTime) {
    _startTime = startTime;
}

void EnvelopeFilter::startRelease(float releaseStartTime) {
    _releaseStartTime = releaseStartTime;
}