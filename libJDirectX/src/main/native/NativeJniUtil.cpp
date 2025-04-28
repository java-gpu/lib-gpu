#include "NativeJniUtil.h"
#include <jni.h>
#include <windows.h>
#include <dxgi.h>
#include <string>
#include <vector>
#include <d3d11.h> // Include DirectX 11 headers
#include <limits>

// Helper function to convert std::string to jstring
jstring toJString(JNIEnv* env, const std::string& str) {
    return env->NewStringUTF(str.c_str());
}

// ✅ Helper: std::wstring to jstring (UTF-16 safe on Windows)
jstring toJString(JNIEnv* env, const std::wstring& wstr) {
    return env->NewString(reinterpret_cast<const jchar*>(wstr.c_str()), static_cast<jsize>(wstr.length()));
}

void HResultError(JNIEnv* env, HRESULT hr,const char* message) {
    int severity = (hr >> 31) & 0x1;
    int facility = (hr >> 16) & 0x7FFF;
    int code     = hr & 0xFFFF;

    // Step 1: Find the exception class
    jclass hresultExceptionClass = env->FindClass("tech/gpu/lib/directx/ex/HResultException");
    if (hresultExceptionClass == nullptr) {
        // Class not found, throw fallback
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Cannot find HResultException class");
        return;
    }

    // Step 2: Get constructor (int hresult)
    //jmethodID ctor = env->GetMethodID(hresultExceptionClass, "<init>", "(I)V");
    jmethodID ctor = env->GetMethodID(hresultExceptionClass, "<init>", "(Ljava/lang/String;I)V");
    if (ctor == nullptr) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Cannot find HResultException constructor");
        return;
    }

    // Step 3: Create exception object
    jobject exception = env->NewObject(hresultExceptionClass, ctor, env->NewStringUTF(message), hr);
    if (exception == nullptr) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Failed to create HResultException");
        return;
    }

    // Step 4: Throw the exception
    env->Throw((jthrowable) exception);
}

void HResultError(JNIEnv* env, HRESULT hr) {
    HResultError(env, hr, "");
}