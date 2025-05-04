#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>

#include "jni_utils.h"
#include "tech_lib_bgfx_jni_Bgfx.h"

#include <bgfx/bgfx.h>
#include <bgfx/platform.h>
#include <exception>
#include <cstdio>
#include <sstream>
#include <fstream>         // <-- required
#include <cstring>

#include "bgfx_jni.h"

extern "C" {

    JNIEXPORT jboolean JNICALL Java_tech_lib_bgfx_jni_Bgfx_init(JNIEnv* env, jclass clzz, jlong windowPointer, jobject canvas) {
        try {

            bgfx::Init init;
            #ifdef __linux__
                bgfx::PlatformData pd = setupLinuxPlatformData(env, canvas);
                bgfx::setPlatformData(pd);
                init.type = bgfx::RendererType::Vulkan;
            #else
                bgfx::PlatformData pd{};
                pd.nwh = reinterpret_cast<void*>(windowPointer);

                if (pd.nwh == nullptr) {
                    jniLog(env, "ERROR", "bgfx_jni.cpp", "❌ No window handler set!");
                    return JNI_FALSE;
                } else {
                    std::stringstream ss;
                    ss << "Native window handle = " << pd.nwh;
                    jniLog(env, "DEBUG", "bgfx_jni.cpp", ss.str().c_str());
                }

                pd.ndt = nullptr;
                pd.context = nullptr;
                pd.backBuffer = nullptr;
                pd.backBufferDS = nullptr;
                bgfx::setPlatformData(pd);

                #ifdef __APPLE__
                    init.type = bgfx::RendererType::Metal;
                #else
                    init.type = bgfx::RendererType::Direct3D11;
                #endif
            #endif

            const bgfx::Caps* caps = bgfx::getCaps();
            std::stringstream ss;
            ss << "Renderer type:  " << bgfx::getRendererName(caps->rendererType);
            jniLog(env, "ERROR", "bgfx_jni.cpp", ss.str().c_str());

            jniLog(env, "DEBUG", "bgfx_jni.cpp", "Starting BGFX initialize...");

            init.platformData=pd;

            bool success = bgfx::init(init);
            if (!success) {
                jniLog(env, "ERROR", "bgfx_jni.cpp", "BGFX init failed!");
                return JNI_FALSE;
            } else {
                jniLog(env, "DEBUG", "bgfx_jni.cpp", "✅ BGFX initialize completed!");
                return JNI_TRUE;
            }
        } catch (const std::exception& e) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), e.what());
            return JNI_FALSE;
        } catch (...) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknown native exception");
            return JNI_FALSE;
        }
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_shutdown(JNIEnv* env, jclass) {
        jniLog(env, "DEBUG", "bgfx_jni.cpp", "Shutting down BGFX...");
        try {
            bgfx::shutdown();
            printf("🔥 Shutdown!!\n");
        } catch (const std::exception& e) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), e.what());
        } catch (...) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknown native exception");
        }
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_frame(JNIEnv* env, jclass) {
        jniLog(env, "DEBUG", "bgfx_jni.cpp", "Calling BGFX frame...");
        try {
            bgfx::frame();
        } catch (const std::exception& e) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), e.what());
        } catch (...) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknown native exception");
        }
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_getMacOSNativeHandlerFromCanvas(JNIEnv* env, jclass, jobject canvas) {
        #ifdef __APPLE__
            return reinterpret_cast<jlong> (getNSViewFromCanvas(env, canvas));
        #endif
        return 0;
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_reset(JNIEnv* env, jclass, jint width, jint height) {
        bgfx::reset(width, height, BGFX_RESET_VSYNC);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_touch(JNIEnv* env, jclass, jint viewId) {
        bgfx::touch(viewId);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setViewRect(JNIEnv* env, jclass, jint viewId, jint x, jint y, jint width, jint height) {
        bgfx::setViewRect(viewId, x, y, width, height);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_drawQuad(JNIEnv* env, jclass, jfloat x, jfloat y, jfloat width, jfloat height, jint abgrColor) {
    
        PosColorVertex::init();

        static PosColorVertex quadVertices[] = {
            { 0,    0,     0.0f, 0 },
            { 0,    100,   0.0f, 0 },
            { 100,  0,     0.0f, 0 },
            { 100,  100,   0.0f, 0 }
        };
    
        static const uint16_t quadIndices[] = { 0, 1, 2, 1, 3, 2 };
    
        for (int i = 0; i < 4; i++) {
            quadVertices[i].x = x + (i % 2 == 0 ? 0 : width);
            quadVertices[i].y = y + (i < 2 ? 0 : height);
            quadVertices[i].abgr = abgrColor;
        }
    
        bgfx::TransientVertexBuffer tvb;
        bgfx::TransientIndexBuffer tib;
    
        bgfx::allocTransientVertexBuffer(&tvb, 4, PosColorVertex::ms_layout);
        bgfx::allocTransientIndexBuffer(&tib, 6);
    
        memcpy(tvb.data, quadVertices, sizeof(quadVertices));
        memcpy(tib.data, quadIndices, sizeof(quadIndices));
    
        bgfx::setVertexBuffer(0, &tvb);
        bgfx::setIndexBuffer(&tib);
        bgfx::setState(BGFX_STATE_DEFAULT);
        // TODO Need shader program here
        //bgfx::submit(0, program); // You must set up a shader program
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setViewClear(JNIEnv* env, jclass, jint viewId, jint clearFlags, jint rgba, jfloat depth, jbyte stencil) {
        bgfx::setViewClear(
            static_cast<bgfx::ViewId>(viewId),
            static_cast<uint16_t>(clearFlags),
            static_cast<uint32_t>(rgba),
            depth,
            static_cast<uint8_t>(stencil)
        );
    }

    // Load binary shader file and create bgfx shader
    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_jni_Bgfx_loadShader (JNIEnv* env, jclass, jstring jpath) {
        const char* pathCStr = env->GetStringUTFChars(jpath, nullptr);
        std::string path(pathCStr);
        env->ReleaseStringUTFChars(jpath, pathCStr);

        std::ifstream file(path, std::ios::binary | std::ios::ate);
        if (!file.is_open()) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Fail to open file path!");
            return 0; // Failed to open file
        }

        std::streamsize size = file.tellg();
        file.seekg(0, std::ios::beg);

        const bgfx::Memory* mem = bgfx::alloc(static_cast<uint32_t>(size));
        if (!file.read(reinterpret_cast<char*>(mem->data), size)) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Failt to read file content!");
            return 0; // Failed to read entire file
        }

        bgfx::ShaderHandle handle = bgfx::createShader(mem);
        return static_cast<jlong>(handle.idx);
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_jni_Bgfx_createProgram(JNIEnv*, jclass, jlong vsHandle, jlong fsHandle, jboolean destroyShaders) {
        bgfx::ProgramHandle handle = bgfx::createProgram(
            { static_cast<uint16_t>(vsHandle) },
            { static_cast<uint16_t>(fsHandle) },
            destroyShaders == JNI_TRUE
        );
        return handle.idx;
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_jni_Bgfx_createVertexBuffer(JNIEnv* env, jclass, jobject jbuffer, jlong layoutPtr) {
        void* data = env->GetDirectBufferAddress(jbuffer);
        jlong size = env->GetDirectBufferCapacity(jbuffer);

        const bgfx::Memory* mem = bgfx::copy(data, (uint32_t)size);
        bgfx::VertexLayout* layout = reinterpret_cast<bgfx::VertexLayout*>(layoutPtr);
        bgfx::VertexBufferHandle handle = bgfx::createVertexBuffer(mem, *layout);
        return handle.idx;
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_jni_Bgfx_createIndexBuffer(JNIEnv* env, jclass, jobject jbuffer) {
        void* data = env->GetDirectBufferAddress(jbuffer);
        jlong size = env->GetDirectBufferCapacity(jbuffer);

        const bgfx::Memory* mem = bgfx::copy(data, (uint32_t)size);
        bgfx::IndexBufferHandle handle = bgfx::createIndexBuffer(mem);
        return handle.idx;
    }
}