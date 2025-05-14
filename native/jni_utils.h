#ifndef JNI_UTILS_H
#define JNI_UTILS_H

#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>
#include <bgfx/bgfx.h>
#include <bgfx/platform.h>

#ifdef __cplusplus
extern "C" {
#endif

void jniLog(JNIEnv* env, const char* level, const char* fileName, const char* message);

bgfx::RendererType::Enum toRendererType(JNIEnv* env, jobject jRendererType);

jobject fromLimits(JNIEnv* env, const bgfx::Caps::Limits& limits);

#ifdef __cplusplus
}
#endif

#endif // JNI_UTILS_H