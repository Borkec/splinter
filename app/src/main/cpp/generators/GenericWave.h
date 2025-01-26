//
// Created by Borko on 25.01.2025..
//

#include <cstddef>

class GenericWave {

public:

    virtual void fill(float* data, size_t size) const = 0;

    ~GenericWave() = default;

private:
};
