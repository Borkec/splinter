//
// Created by Borko on 25.01.2025..
//
#include <jni.h>
#include <functional>

#ifndef M_NATIVE
#define M_NATIVE

class NativeCallback {
public:
    NativeCallback(jobject listener, const char* funcName, const char* funcSig);
    ~NativeCallback();

    void call(const float* data, size_t size);

    void call(const int data);

private:

    jobject m_listener;
    jmethodID m_methodID;
};

#endif