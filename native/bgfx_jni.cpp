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

extern "C" {

#ifdef __APPLE__
extern void* getNSViewFromCanvas(JNIEnv* env, jobject canvas);
// extern void* createNativeWindow(JNIEnv* env, int x, int y, int width, int height, const char* title);
#endif

    JNIEXPORT jboolean JNICALL Java_tech_lib_bgfx_jni_Bgfx_init(JNIEnv* env, jclass clzz, jlong windowPointer) {
        try {

            bgfx::Init init;
            bgfx::PlatformData pd{};
            pd.nwh = reinterpret_cast<void*>(windowPointer);
            // bgfx::setDebug(BGFX_DEBUG_TEXT);
            pd.ndt = nullptr; // Or required for X11 (Linux)

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

            const bgfx::Caps* caps = bgfx::getCaps();
            std::stringstream ss;
            ss << "Renderer type:  " << bgfx::getRendererName(caps->rendererType);
            jniLog(env, "ERROR", "bgfx_jni.cpp", ss.str().c_str());

            jniLog(env, "DEBUG", "bgfx_jni.cpp", "Starting BGFX initialize...");

            init.platformData=pd;
            init.type = bgfx::RendererType::Metal;
            jniLog(env, "DEBUG", "bgfx_jni.cpp", "Set RendererType as RendererType::Count");
            init.resolution.width = 1280;
            jniLog(env, "DEBUG", "bgfx_jni.cpp", "Set resolution.width=1280");
            init.resolution.height = 720;
            jniLog(env, "DEBUG", "bgfx_jni.cpp", "Set resolution.height=720");
            init.resolution.reset = BGFX_RESET_VSYNC;
            jniLog(env, "DEBUG", "bgfx_jni.cpp", "Set resolution.reset=BGFX_RESET_VSYNC");

            // bgfx::renderFrame();
            bool success = bgfx::init(init);
            if (!success) {
                jniLog(env, "ERROR", "bgfx_jni.cpp", "BGFX init failed!");
                // env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "bgfx init failed");
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

}