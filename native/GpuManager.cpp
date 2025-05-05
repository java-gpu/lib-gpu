#include <jni.h>
#include "tech_lib_bgfx_jni_GpuManager.h"
#include "GpuManager.h"

/** GpuInfo */
#ifdef __cplusplus
extern "C" {
#endif

    JNIEXPORT jstring JNICALL Java_tech_lib_bgfx_jni_GpuManager_getSystemDefaultGPUName(JNIEnv* env, jclass) {
        const char* name = getSystemDefaultGPUName(env);
        return env->NewStringUTF(name);
    }

    JNIEXPORT jobjectArray JNICALL Java_tech_lib_bgfx_jni_GpuManager_getAllGPUNames(JNIEnv* env, jclass) {
        size_t count = getGPUCount(env);

        jclass stringClass = env->FindClass("java/lang/String");
        jobjectArray array = env->NewObjectArray(count, stringClass, nullptr);

        for (size_t i = 0; i < count; ++i) {
            const char* name = getGPUNameAtIndex(env, i);
            env->SetObjectArrayElement(array, i, env->NewStringUTF(name));
        }

        return array;
    }

    JNIEXPORT jint JNICALL Java_tech_lib_bgfx_jni_GpuManager_getGPUCount(JNIEnv* env, jclass) {
        size_t count = getGPUCount(env);
        return (jint) count;
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_GpuManager_getGPUPointerAtIndex(JNIEnv* env, jclass, jint index) {
        return (jlong) getGPUPointerAtIndex(env, index);
    }

    JNIEXPORT jstring JNICALL Java_tech_lib_bgfx_jni_GpuManager_getGPUNameByPointer(JNIEnv* env, jclass, jlong pointer) {
        const char* name = getGPUNameByPointer(env, (void*) pointer);
        return env->NewStringUTF(name);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_GpuManager_releaseGpu(JNIEnv* env, jclass, jlong pointer) {
        releaseGpu(env, (void*) pointer);
    }

#ifdef __cplusplus
}
#endif
