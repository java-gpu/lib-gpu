#include "GpuManager.h"
#include <dxgi1_6.h>
#include <wrl/client.h>
#include <vector>
#include <string>
#include <jni.h>
#include "jni_utils.h"

using Microsoft::WRL::ComPtr;

static std::vector<ComPtr<IDXGIAdapter1>> g_adapters;
static std::vector<std::string> g_adapterNames;

static void initializeAdapters() {
    if (!g_adapters.empty()) return;

    ComPtr<IDXGIFactory6> factory;
    if (FAILED(CreateDXGIFactory1(IID_PPV_ARGS(&factory)))) return;

    UINT index = 0;
    ComPtr<IDXGIAdapter1> adapter;

    while (factory->EnumAdapters1(index, &adapter) != DXGI_ERROR_NOT_FOUND) {
        DXGI_ADAPTER_DESC1 desc;
        adapter->GetDesc1(&desc);

        if (desc.Flags & DXGI_ADAPTER_FLAG_SOFTWARE) {
            ++index;
            continue;
        }

        g_adapters.push_back(adapter);

        char name[128] = {};
        wcstombs(name, desc.Description, sizeof(name));
        g_adapterNames.emplace_back(name);

        ++index;
    }
}

const char* getSystemDefaultGPUName(JNIEnv* env) {
    initializeAdapters();
    if (!g_adapterNames.empty())
        return g_adapterNames[0].c_str();
    return nullptr;
}

size_t getGPUCount(JNIEnv* env) {
    initializeAdapters();
    return g_adapters.size();
}

void* getGPUPointerAtIndex(JNIEnv* env, size_t index) {
    initializeAdapters();
    if (index >= g_adapters.size()) return nullptr;
    g_adapters[index]->AddRef(); // mimic retained
    return g_adapters[index].Get();
}

const char* getGPUNameAtIndex(JNIEnv* env, size_t index) {
    initializeAdapters();
    if (index >= g_adapterNames.size()) return nullptr;
    return g_adapterNames[index].c_str();
}

const char* getGPUNameByPointer(JNIEnv* env, void* devicePtr) {
    IDXGIAdapter1* adapter = reinterpret_cast<IDXGIAdapter1*>(devicePtr);
    if (!adapter) return nullptr;

    DXGI_ADAPTER_DESC1 desc;
    adapter->GetDesc1(&desc);

    static thread_local std::string name;
    char buf[128];
    wcstombs(buf, desc.Description, sizeof(buf));
    name = buf;
    return name.c_str();
}

void releaseGpu(JNIEnv* env, void* devicePtr) {
    IDXGIAdapter1* adapter = reinterpret_cast<IDXGIAdapter1*>(devicePtr);
    if (adapter) adapter->Release();
}
