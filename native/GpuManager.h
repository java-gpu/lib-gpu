#ifndef GPUINFO_H
#define GPUINFO_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

    const char* getSystemDefaultGPUName(JNIEnv* env);

    size_t getGPUCount(JNIEnv* env);

    const char* getGPUNameByPointer(JNIEnv* env, void* devicePtr);

    void* getGPUPointerAtIndex(JNIEnv* env, size_t index);

    void releaseGpu(JNIEnv* env, void* devicePtr);

    const char* getGPUNameAtIndex(JNIEnv* env, size_t index);

#ifdef __cplusplus
}
#endif

#endif /* GPUINFO_H */
