//
// Created by Borko on 25.01.2025..
//

#include <cstddef>
#include <cmath>

class SineWave: public GenericWave {

public:

    void fill(float* data, size_t size) const override {
        for (size_t i = 0; i < size; ++i) {
            data[i] = (float)std::sin(static_cast<float>(i)/(float)size * 2*M_PI);
        }
    }
};
