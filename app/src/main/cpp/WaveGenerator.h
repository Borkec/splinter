//
// Created by Borko on 25.01.2025..
//

#include <cstddef>
#include <functional>
#include "generators/GenericWave.h"
#include "const.h"

class WaveGenerator {

public:

    static constexpr size_t size = TABLE_SIZE;

    WaveGenerator();

    void fill(const GenericWave& wave);
    void fill(const float* data);

    const float* getData() const;

    void addOnDataChangedListener(const std::function<void(const float*, const size_t)>& listener);
    void removeOnDataChangedListener();

private:

    float m_data[size]{};
    void notifyDataChanged();

    std::vector<std::function<void(const float*, const size_t)>> onDataChangedListeners;
};
