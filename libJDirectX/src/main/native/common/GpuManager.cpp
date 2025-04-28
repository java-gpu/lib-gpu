#include <jni.h>
#define NOMINMAX
#include <windows.h>
#include <dxgi.h>
#include <string>
#include <vector>
#include <d3d11.h> // Include DirectX 11 headers
#include "tech_gpu_lib_jni_GpuManager.h"
#include <limits>
#include "../NativeJniUtil.h"

#pragma comment(lib, "d3d11.lib") // Link DirectX library

// getSystemDefaultGPUName
JNIEXPORT jstring JNICALL Java_tech_gpu_lib_jni_GpuManager_getSystemDefaultGPUName(JNIEnv* env, jclass) {
    IDXGIFactory* pFactory = nullptr;
    CreateDXGIFactory(__uuidof(IDXGIFactory), (void**)&pFactory);

    IDXGIAdapter* pAdapter = nullptr;
    pFactory->EnumAdapters(0, &pAdapter);

    DXGI_ADAPTER_DESC desc;
    pAdapter->GetDesc(&desc);

    pAdapter->Release();
    pFactory->Release();

    // ✅ Pass std::wstring directly to the new helper
    std::wstring ws(desc.Description);
    return toJString(env, ws);
}

// getAllGPUNames
JNIEXPORT jobjectArray JNICALL Java_tech_gpu_lib_jni_GpuManager_getAllGPUNames(JNIEnv* env, jclass) {
    try {
        IDXGIFactory* pFactory = nullptr;
        CreateDXGIFactory(__uuidof(IDXGIFactory), (void**)&pFactory);

        std::vector<std::wstring> gpuNames;
        IDXGIAdapter* pAdapter = nullptr;
        UINT i = 0;
        while (pFactory->EnumAdapters(i++, &pAdapter) != DXGI_ERROR_NOT_FOUND) {
            DXGI_ADAPTER_DESC desc;
            pAdapter->GetDesc(&desc);

            std::wstring ws(desc.Description);
            gpuNames.push_back(ws);

            pAdapter->Release();
        }
        pFactory->Release();

        size_t gpuCount = gpuNames.size();
        if (gpuCount > static_cast<size_t>(std::numeric_limits<jsize>::max())) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "GPU count exceeds maximum allowable size for jobjectArray.");
        }

        jsize gpuCountAsJsize = static_cast<jsize>(gpuCount);
        jobjectArray result = env->NewObjectArray(gpuCountAsJsize, env->FindClass("java/lang/String"), nullptr);
        for (jsize j = 0; j < gpuCountAsJsize; j++) {
            env->SetObjectArrayElement(result, j, toJString(env, gpuNames[j]));
        }

        return result;
    } catch (const std::exception& e) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), e.what());
    } catch (...) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Unknown native exception");
    }
    jclass stringClass = env->FindClass("java/lang/String");
    jobjectArray emptyArray = env->NewObjectArray(0, stringClass, nullptr);
    return emptyArray;
}

// getGPUCount
JNIEXPORT jint JNICALL Java_tech_gpu_lib_jni_GpuManager_getGPUCount(JNIEnv* env, jclass) {
    try {
        IDXGIFactory* pFactory = nullptr;
        CreateDXGIFactory(__uuidof(IDXGIFactory), (void**)&pFactory);

        UINT count = 0;
        IDXGIAdapter* pAdapter = nullptr;
        while (pFactory->EnumAdapters(count, &pAdapter) != DXGI_ERROR_NOT_FOUND) {
            count++;
            pAdapter->Release();
        }
        pFactory->Release();

        return (jint)count;
    } catch (const std::exception& e) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), e.what());
    } catch (...) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Unknown native exception");
    }
    return 0;
}

// getGPUPointerAtIndex
JNIEXPORT jlong JNICALL Java_tech_gpu_lib_jni_GpuManager_getGPUPointerAtIndex(JNIEnv* env, jclass, jint index) {
    try {
        IDXGIFactory* pFactory = nullptr;
        CreateDXGIFactory(__uuidof(IDXGIFactory), (void**)&pFactory);

        IDXGIAdapter* pAdapter = nullptr;
        pFactory->EnumAdapters(index, &pAdapter);

        jlong pointer = (jlong)pAdapter;
        pFactory->Release();

        return pointer;
    } catch (const std::exception& e) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), e.what());
    } catch (...) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Unknown native exception");
    }
    return 0;
}

// getGPUNameByPointer
JNIEXPORT jstring JNICALL Java_tech_gpu_lib_jni_GpuManager_getGPUNameByPointer(JNIEnv* env, jclass, jlong pointer) {
    try {
        IDXGIAdapter* pAdapter = (IDXGIAdapter*)pointer;

        DXGI_ADAPTER_DESC desc;
        pAdapter->GetDesc(&desc);

       // ✅ Pass std::wstring directly to the new helper
       std::wstring ws(desc.Description);
       return toJString(env, ws);
    } catch (const std::exception& e) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), e.what());
    } catch (...) {
        env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Unknown native exception");
    }
    return env->NewStringUTF("");
}

// releaseGpu
JNIEXPORT void JNICALL Java_tech_gpu_lib_jni_GpuManager_releaseGpu(JNIEnv* env, jclass, jlong pointer) {
    IDXGIAdapter* pAdapter = (IDXGIAdapter*)pointer;
    if (pAdapter) {
        pAdapter->Release();
    }
}