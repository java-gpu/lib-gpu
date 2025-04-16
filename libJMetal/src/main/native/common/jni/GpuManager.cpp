#include <jni.h>
#include "tech_gpu_lib_gpu_GpuManager.h"
#include "GpuManager.h"

/** GpuInfo */
#ifdef __cplusplus
extern "C" {
#endif
    JNIEXPORT jstring JNICALL Java_tech_gpu_lib_GpuManager_getSystemDefaultGPUName(JNIEnv* env, jclass) {
        const char* name = getSystemDefaultGPUName();
        return env->NewStringUTF(name);
    }

    JNIEXPORT jobjectArray JNICALL Java_tech_gpu_lib_GpuManager_getAllGPUNames(JNIEnv* env, jclass) {
        size_t count = getMetalGPUCount();

        jclass stringClass = env->FindClass("java/lang/String");
        jobjectArray array = env->NewObjectArray(count, stringClass, nullptr);

        for (size_t i = 0; i < count; ++i) {
            const char* name = getMetalGPUNameAtIndex(i);
            env->SetObjectArrayElement(array, i, env->NewStringUTF(name));
        }

        return array;
    }

    JNIEXPORT jint JNICALL Java_tech_gpu_lib_GpuManager_getGPUCount(JNIEnv* env, jclass) {
        size_t count = getMetalGPUCount();
        return (jint) count;
    }

    JNIEXPORT jlong JNICALL Java_tech_gpu_lib_GpuManager_getGPUPointerAtIndex(JNIEnv* env, jclass, jint index) {
        return (jlong) getGPUPointerAtIndex(index);
    }

    JNIEXPORT jstring JNICALL Java_tech_gpu_lib_GpuManager_getGPUNameByPointer(JNIEnv* env, jclass, jlong pointer) {
        const char* name = getGPUNameByPointer((void*) pointer);
        return env->NewStringUTF(name);
    }

    JNIEXPORT void JNICALL Java_tech_gpu_lib_GpuManager_releaseGpu(JNIEnv* env, jclass, jlong pointer) {
        releaseGpu((void*) pointer);
    }

#ifdef __cplusplus
}
#endif
