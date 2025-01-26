#pragma once
//
// Created by Borko on 25.01.2025..
//

#ifndef SPLINTER_JNI_UTILS_H
#define SPLINTER_JNI_UTILS_H

#include <jni.h>
#include "NativeCallback.h"

extern JavaVM* javaVM;

JNIEnv* getCurrentEnv();

#endif //SPLINTER_JNI_UTILS_H
