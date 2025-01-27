//
// Created by Borko on 25.01.2025..
//

#include <cstddef>
#include <functional>
#include "const.h"

class WaveGenerator {

public:

    WaveGenerator(size_t tableSize);

    void fill(const float* data);

    const float* getData() const;

    void addOnDataChangedListener(const std::function<void(const float*, const size_t)>& listener);
    void removeOnDataChangedListener();

private:

    size_t mTableSize;
    float *m_data;
    void notifyDataChanged();

    std::vector<std::function<void(const float*, const size_t)>> onDataChangedListeners;
};
