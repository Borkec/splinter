//
// Created by Borko on 25.01.2025..
//

#include <cstddef>
#include <cmath> 

class TriangleWave: public GenericWave {

public:

    void fill(float* data, size_t size) const override {
        for (size_t i = 0; i < size; ++i) {
            if(i < size/2) {
                data[i] = (i/(size/2) * 2) - 1;
            } else {
                data[i] = ((i/(size/2)-1)*2 - 1);
            }
        }
    }
};
