#include <jni.h>
#include "tech_gpu_lib_directx_jni_DirectxRenderer.h"
#include <d3d11.h>
#include <iostream>
#include <comdef.h>
#include "../NativeJniUtil.h"

// DirectX device and context pointers
ID3D11Device* g_device = nullptr;
ID3D11DeviceContext* g_deviceContext = nullptr;
IDXGISwapChain* g_swapChain = nullptr;
ID3D11RenderTargetView* g_renderTargetView = nullptr;

namespace DirectX {
    // Initialize DirectX
    uintptr_t init(JNIEnv* env, int gpuIndex) {
        HRESULT hr;

        // Setup DXGI (DirectX Graphics Infrastructure) Factory and Adapter
        IDXGIFactory1* factory = nullptr;
        IDXGIAdapter1* adapter = nullptr;

        hr = CreateDXGIFactory1(__uuidof(IDXGIFactory1), (void**)&factory);
        if (FAILED(hr)) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
                     "Failed to create DXGI Factory.");
            return 0;
        }

        // Get the adapter (GPU) based on the index
        hr = factory->EnumAdapters1(gpuIndex, &adapter);
        if (FAILED(hr)) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
                   ("Failed to get adapter for GPU index" + std::to_string(gpuIndex)).c_str());
            return 0;
        }

        // Create DirectX 11 device and context
        hr = D3D11CreateDevice(
            adapter, D3D_DRIVER_TYPE_UNKNOWN, NULL, 0, NULL, 0, D3D11_SDK_VERSION,
            &g_device, NULL, &g_deviceContext);
        if (FAILED(hr)) {
            HResultError(env, hr, "Failed to create DirectX 11 device.");
            return 0;
        }

        // Create SwapChain
        DXGI_SWAP_CHAIN_DESC scDesc = {};
        scDesc.BufferCount = 1;
        scDesc.BufferDesc.Format = DXGI_FORMAT_R8G8B8A8_UNORM;
        scDesc.BufferDesc.Width = 1920; // Window width
        scDesc.BufferDesc.Height = 1080; // Window height
        scDesc.BufferUsage = DXGI_USAGE_RENDER_TARGET_OUTPUT;
        scDesc.OutputWindow = nullptr;  // You would normally pass a window handle here
        scDesc.SampleDesc.Count = 1;
        scDesc.Windowed = TRUE;

        hr = factory->CreateSwapChain(g_device, &scDesc, &g_swapChain);
        if (FAILED(hr)) {
            HResultError(env, hr, "Failed to create swap chain.");
            return 0;
        }

        // Create RenderTargetView
        ID3D11Texture2D* backBuffer = nullptr;
        hr = g_swapChain->GetBuffer(0, __uuidof(ID3D11Texture2D), (void**)&backBuffer);
        if (FAILED(hr)) {
            HResultError(env, hr, "Failed to get swap chain buffer.");
            return 0;
        }

        hr = g_device->CreateRenderTargetView(backBuffer, nullptr, &g_renderTargetView);
        backBuffer->Release();
        if (FAILED(hr)) {
            HResultError(env, hr, "Failed to create render target view.");
            return 0;
        }

        // Set the render target
        g_deviceContext->OMSetRenderTargets(1, &g_renderTargetView, nullptr);

        return reinterpret_cast<uintptr_t>(g_device); // Return a pointer as uintptr_t
    }

    // Draw a basic frame (clear the screen)
    void draw() {
        if (g_deviceContext == nullptr) return;

        // Set clear color (light blue)
        FLOAT clearColor[4] = {0.3f, 0.5f, 1.0f, 1.0f};  // RGBA
        g_deviceContext->ClearRenderTargetView(g_renderTargetView, clearColor);

        // Present the back buffer
        g_swapChain->Present(1, 0); // Present with vsync
    }

    // Release DirectX resources
    void release() {
        if (g_renderTargetView) g_renderTargetView->Release();
        if (g_swapChain) g_swapChain->Release();
        if (g_deviceContext) g_deviceContext->Release();
        if (g_device) g_device->Release();
    }
}

// JNI implementations
extern "C" {

JNIEXPORT jlong JNICALL Java_tech_gpu_lib_directx_jni_DirectxRenderer_init
  (JNIEnv* env, jobject obj, jint gpuIndex) {
    return static_cast<jlong>(DirectX::init(env, gpuIndex));
}

JNIEXPORT void JNICALL Java_tech_gpu_lib_directx_jni_DirectxRenderer_draw
  (JNIEnv *env, jobject obj) {
    DirectX::draw();  // Draw (clear the screen)
}

JNIEXPORT void JNICALL Java_tech_gpu_lib_directx_jni_DirectxRenderer_release
  (JNIEnv *env, jobject obj) {
    DirectX::release();  // Clean up DirectX resources
}

}
