//
// Created by Borko on 25.01.2025..
//

#include <android/log.h>
#include "WaveGenerator.h"


WaveGenerator::WaveGenerator() {
    for(float & i : m_data) {
        i = 0.0f;
    }
}


void WaveGenerator::fill(const GenericWave& wave) {

    wave.fill(m_data, size);
    notifyDataChanged();
}

void WaveGenerator::fill(const float* data) {
    for(size_t i = 0; i < size; i++) {
        m_data[i] = data[i];
    }
    notifyDataChanged();
}

const float* WaveGenerator::getData() const {
    return m_data;
}

void WaveGenerator::addOnDataChangedListener(const std::function<void(const float *, const size_t)> &listener) {
    onDataChangedListeners.push_back(listener);

    // notifies first state
    listener(m_data, size);
}

void WaveGenerator::removeOnDataChangedListener() {
    onDataChangedListeners.clear();
}

void WaveGenerator::notifyDataChanged() {
    for(const auto& listener: onDataChangedListeners) {
        listener(m_data, size);
    }
}