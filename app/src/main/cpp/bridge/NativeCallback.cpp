//
// Created by Borko on 25.01.2025..
//

#include "NativeCallback.h"
#include "jni_utils.h"
#include <jni.h>
#include <functional>
#include <iostream>
#include <android/log.h>
#include <asm/unistd.h>


NativeCallback::NativeCallback(jobject listener, const char* funcName, const char* funcSig){
    JNIEnv* env = getCurrentEnv();

    m_listener = env->NewGlobalRef(listener);

    jclass listenerClass = env->GetObjectClass(m_listener);
    m_methodID = env->GetMethodID(listenerClass, funcName, funcSig);
}

NativeCallback::~NativeCallback() {

    __android_log_print(ANDROID_LOG_INFO, "Test", "Destroying");
    if(m_listener != nullptr) {
        getCurrentEnv()->DeleteGlobalRef(m_listener);
    }
}

void NativeCallback::call(const float *data, const size_t size) {
    if(m_listener == nullptr) {
        std::cerr << "Listener class is null!" << std::endl;
        return;
    }

    JNIEnv* env = getCurrentEnv();

    jfloatArray javaArray = env->NewFloatArray(size);
    env->SetFloatArrayRegion(javaArray, 0, size, data);

    __android_log_print(ANDROID_LOG_INFO, "Test", "Heree 2");
    env->CallVoidMethod(m_listener, m_methodID, javaArray);


    env->DeleteLocalRef(javaArray);
}

void NativeCallback::call(const int data) {
    if(m_listener == nullptr) {
        std::cerr << "Listener class is null!" << std::endl;
        return;
    }

    JNIEnv* env = getCurrentEnv();

    env->CallVoidMethod(m_listener, m_methodID, (jint)data);
}
