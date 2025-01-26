//
// Created by Borko on 25.01.2025..
//

#include <jni.h>
#include "jni_utils.h"

JavaVM* javaVM = nullptr;

JNIEnv* getCurrentEnv() {
    JNIEnv* env;
    javaVM->AttachCurrentThread(&env, nullptr);
    return env;
}