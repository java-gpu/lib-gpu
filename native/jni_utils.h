#ifndef JNI_UTILS_H
#define JNI_UTILS_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

void jniLog(JNIEnv* env, const char* level, const char* fileName, const char* message);

#ifdef __cplusplus
}
#endif

#endif // JNI_UTILS_H