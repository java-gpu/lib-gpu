#ifndef NATIVE_JNI_UTILS_H
#define NATIVE_JNI_UTILS_H

#include <jni.h>
#include <windows.h>
#include <dxgi.h>
#include <string>
#include <vector>
#include <d3d11.h> // Include DirectX 11 headers
#include <limits>

jstring toJString(JNIEnv* env, const std::string& str);

jstring toJString(JNIEnv* env, const std::wstring& wstr);

void HResultError(JNIEnv* env, HRESULT hr);
void HResultError(JNIEnv* env, HRESULT hr, const char* message);

#endif // NATIVE_JNI_UTILS_H