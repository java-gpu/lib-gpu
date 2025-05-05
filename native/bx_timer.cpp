
#include <jni.h>
#include <bx/timer.h>

extern "C" {
    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_BxTimer_getHPCounter(JNIEnv* env, jclass clazz) {
        return (jlong)bx::getHPCounter();
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_BxTimer_getHPFrequency(JNIEnv* env, jclass clazz) {
        return (jlong)bx::getHPFrequency();
    }
}