#include <jni.h>
#include <windows.h>
#include <d3d11.h>
#include <wrl/client.h>
#include <string>
#include <vector>
#include <fstream>
#include <WICTextureLoader.h>
#include "tech_gpu_lib_jni_TextureNative.h"
#include "../NativeJniUtil.h"

using Microsoft::WRL::ComPtr;

#pragma comment(lib, "d3d11.lib")
#pragma comment(lib, "dxgi.lib")

extern "C" {

    JNIEXPORT jobject JNICALL Java_tech_gpu_lib_directx_jni_Texture_getTextureInfo
      (JNIEnv* env, jclass clazz, jlong texturePtr) {

        ID3D11Texture2D* texture = reinterpret_cast<ID3D11Texture2D*>(texturePtr);
        if (!texture) {
          env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
              "Cannot retrieve Texture from input pointer!");
          return nullptr;
        }

        D3D11_TEXTURE2D_DESC desc;
        texture->GetDesc(&desc);

        jclass textureInfoClass = env->FindClass("tech/gpu/lib/graphics/TextureInfo");
        if (!textureInfoClass) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
                "Cannot find class tech/gpu/lib/graphics/TextureInfo");
            return nullptr;
        }

        // Constructor: (JIII)V — long, int, int, int
        jmethodID ctor = env->GetMethodID(textureInfoClass, "<init>", "(JIII)V");
        if (!ctor) {
           env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
                "Cannot find constructor for class tech/gpu/lib/graphics/TextureInfo");
           return nullptr;
        }

        jobject textureInfoObject = env->NewObject(
            textureInfoClass, ctor,
            (jlong)texturePtr,
            (jint)desc.Width,
            (jint)desc.Height,
            (jint)desc.ArraySize  // Often used as depth
        );

        return textureInfoObject;
    }

    JNIEXPORT jobject JNICALL Java_tech_gpu_lib_jni_TextureNative_loadTexture
    (JNIEnv* env, jclass jclzz, jlong gpuPointer, jstring jPath, jboolean useMipMaps, jint pixelFormat) {
        const char* utfPath = env->GetStringUTFChars(jPath, nullptr);

        // Convert UTF-8 (from Java) to UTF-16 (Windows)
        int sizeNeeded = MultiByteToWideChar(CP_UTF8, 0, utfPath, -1, nullptr, 0);
        std::wstring pathW(sizeNeeded - 1, 0);
        MultiByteToWideChar(CP_UTF8, 0, utfPath, -1, &pathW[0], sizeNeeded);
        env->ReleaseStringUTFChars(jPath, utfPath);

        ID3D11Device* device = reinterpret_cast<ID3D11Device*>(gpuPointer);
        ID3D11Resource* resource = nullptr;
        ID3D11ShaderResourceView* srv = nullptr;

        HRESULT hr = DirectX::CreateWICTextureFromFileEx(
            device,
            pathW.c_str(),
            0,
            D3D11_USAGE_DEFAULT,
            D3D11_BIND_SHADER_RESOURCE,
            0,
            0,
            DirectX::WIC_LOADER_DEFAULT,
            &resource,
            &srv
        );

        if (FAILED(hr)) {
           HResultError(env, hr);
           return nullptr;
        }

        // Try casting resource to ID3D11Texture2D
        ComPtr<ID3D11Texture2D> texture;
        hr = resource->QueryInterface(__uuidof(ID3D11Texture2D), reinterpret_cast<void**>(texture.GetAddressOf()));
        if (FAILED(hr) || !texture) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
                   "Fail to cast resource to ID3D11Texture2D!");
            return nullptr;
        }

        // AddRef to retain ownership before passing to Java
        texture->AddRef();

        return Java_tech_gpu_lib_directx_jni_Texture_getTextureInfo (env, jclzz, reinterpret_cast<jlong>(texture.Get()));
    }

    JNIEXPORT jobject JNICALL Java_tech_gpu_lib_jni_TextureNative_loadTextureFromStream
    (JNIEnv* env, jclass clazz, jlong gpuPointer, jobject imageStream, jboolean useMipMaps, jint pixelFormat) {
        try {
            // Get InputStream.read(byte[]) method
            jclass inputStreamClass = env->GetObjectClass(imageStream);
            jmethodID readMethod = env->GetMethodID(inputStreamClass, "readAllBytes", "()[B");

            printf("Convert InputStream to byte array....\n");
            jbyteArray byteArray = (jbyteArray)env->CallObjectMethod(imageStream, readMethod);
            printf("Reading data length....\n");
            jsize length = env->GetArrayLength(byteArray);
            printf("Length %d\n", length);
            // jbyte* byteData = env->GetByteArrayElements(byteArray, NULL);
            jbyte* byteData = static_cast<jbyte*>(env->GetPrimitiveArrayCritical(byteArray, nullptr));

            if (length == 0 || byteData == nullptr) {
              env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
                                 "Data empty!");
              return nullptr;
            }

            ID3D11Device* device = reinterpret_cast<ID3D11Device*>(gpuPointer);
            if (!device) {
                env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
                                                 "Cannot find GPU device!");
                return nullptr;
            }
            ID3D11Resource* resource = nullptr;
            ID3D11ShaderResourceView* srv = nullptr;
            ID3D11DeviceContext* context = nullptr;

            HRESULT hr = DirectX::CreateWICTextureFromMemoryEx(
                device, context,
                reinterpret_cast<const uint8_t*>(byteData),
                static_cast<size_t>(length),
                0,                   // maxsize (0 = full size)
                D3D11_USAGE_DEFAULT,  // usage
                D3D11_BIND_SHADER_RESOURCE, // bindflags
                0,                   // cpuaccessflags
                0,                   // miscflags
                DirectX::WIC_LOADER_DEFAULT,
                &resource, &srv
            );
            // env->ReleaseByteArrayElements(byteArray, byteData, JNI_ABORT);
            env->ReleasePrimitiveArrayCritical(byteArray, byteData, JNI_ABORT);

            if (FAILED(hr)) {
                HResultError(env, hr);
                return nullptr;
            }

            ComPtr<ID3D11Texture2D> texture;
            srv->GetResource(reinterpret_cast<ID3D11Resource**>(texture.GetAddressOf()));


            if (!texture) {
              env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"),
                     "Fail to cast resource to ID3D11Texture2D!");
              return nullptr;
            }

            // AddRef to retain ownership before passing to Java
            texture->AddRef();

            return Java_tech_gpu_lib_directx_jni_Texture_getTextureInfo (env, clazz, reinterpret_cast<jlong>(texture.Get()));
        } catch (const std::exception& e) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), e.what());
        } catch (...) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Unknown native exception");
        }
        return nullptr;
    }

    JNIEXPORT jobject JNICALL Java_tech_gpu_lib_jni_TextureNative_createEmptyTexture
    (JNIEnv* env, jclass clazz, jlong gpuPointer, jint width, jint height, jint pixelFormat) {
        try {
            ID3D11Device* device = reinterpret_cast<ID3D11Device*>(gpuPointer);

            D3D11_TEXTURE2D_DESC desc = {};
            desc.Width = static_cast<UINT>(width);
            desc.Height = static_cast<UINT>(height);
            desc.Format = static_cast<DXGI_FORMAT>(pixelFormat);
            desc.SampleDesc.Count = 1;
            desc.MipLevels = 1;             // Number of mipmap levels
            desc.ArraySize = 1;             // Single texture
            desc.Usage = D3D11_USAGE_DEFAULT;
            desc.BindFlags = D3D11_BIND_SHADER_RESOURCE;
            desc.CPUAccessFlags = 1;

            ID3D11Texture2D* texture = nullptr;
            HRESULT hr = device->CreateTexture2D(&desc, nullptr, &texture);

            if (FAILED(hr)) {
                HResultError(env, hr);
                return nullptr;
            }

            if (!texture) {
                env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Texture is null");
                return nullptr;
            }

            // AddRef to retain ownership before passing to Java
            texture->AddRef();

            return Java_tech_gpu_lib_directx_jni_Texture_getTextureInfo(env, clazz, reinterpret_cast<jlong>(texture));
        } catch (const std::exception& e) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), e.what());
        } catch (...) {
            env->ThrowNew(env->FindClass("tech/gpu/lib/ex/JniRuntimeException"), "Unknown native exception");
        }
        return nullptr;
    }

    JNIEXPORT void JNICALL Java_tech_gpu_lib_jni_TextureNative_release
      (JNIEnv*, jclass jclzz, jlong texturePointer) {
        ID3D11Texture2D* texture = reinterpret_cast<ID3D11Texture2D*>(texturePointer);
        if (texture) texture->Release();
    }
}
