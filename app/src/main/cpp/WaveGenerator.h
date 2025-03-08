//
// Created by Borko on 25.01.2025..
//

#include <cstddef>
#include <functional>
#include "const.h"


#ifndef SPLINTER_WAVEGENERATOR_H
#define SPLINTER_WAVEGENERATOR_H

class WaveGenerator {

public:

    WaveGenerator();

    WaveGenerator(size_t tableSize);

    void fill(const float* data);

    [[nodiscard]] const float* getData() const;

    [[nodiscard]] size_t getTableSize() const {
        return mTableSize;
    };

    void addOnDataChangedListener(const std::function<void(const float*, const size_t)>& listener);
    void removeOnDataChangedListener();

private:

    size_t mTableSize;
    float *m_data;
    void notifyDataChanged();

    std::vector<std::function<void(const float*, const size_t)>> onDataChangedListeners;
};

#endif