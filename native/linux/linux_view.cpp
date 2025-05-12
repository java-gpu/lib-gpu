#include <jni.h>
#include <jawt.h>
#include <jawt_md.h> // For JAWT_X11DrawingSurfaceInfo
#include <bgfx/platform.h>
#include <string>
#include <cstdlib>
#include <cstring>
#include <cstdio>
#include "jni_utils.h"
#include <sstream>

extern "C" {
    bgfx::PlatformData setupLinuxPlatformData(JNIEnv* env, jobject canvas) {
        bgfx::PlatformData pd{};
        JAWT awt;
        awt.version = JAWT_VERSION_1_4;
        if (JAWT_GetAWT(env, &awt) == JNI_FALSE) {
            jniLog(env, "ERROR", "linux_view.cpp", "❌ JAWT_GetAWT failed");
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "❌ JAWT_GetAWT failed");
            return pd;
        }

        JAWT_DrawingSurface* ds = awt.GetDrawingSurface(env, canvas);
        if (ds == nullptr) {
            jniLog(env, "ERROR", "linux_view.cpp", "❌ Failed to get drawing surface");
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "❌ Failed to get drawing surface");
            return pd;
        }

        jint lock = ds->Lock(ds);
        if ((lock & JAWT_LOCK_ERROR) != 0) {
            jniLog(env, "ERROR", "linux_view.cpp", "❌ Failed to lock drawing surface");
            awt.FreeDrawingSurface(ds);
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "❌ Failed to lock drawing surface");
            return pd;
        }

        JAWT_DrawingSurfaceInfo* dsi = ds->GetDrawingSurfaceInfo(ds);

        void* ndt = nullptr;
        void* nwh = nullptr;

        const char* session = getenv("XDG_SESSION_TYPE");
        if (session && strcmp(session, "wayland") == 0) {
           // 🔴 Wayland is not supported via JAWT — must use GLFW/SDL2 or platform layer
           jniLog(env, "ERROR", "linux_view.cpp", "❌ Wayland session detected: JAWT cannot get wl_display");
        //    ds->FreeDrawingSurfaceInfo(dsi);
        //    ds->Unlock(ds);
        //    awt.FreeDrawingSurface(ds);
        //    env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "❌ Wayland session detected: JAWT cannot get wl_display");
        //    return pd;
        } 
        // ✅ Assume X11
        JAWT_X11DrawingSurfaceInfo* dsi_x11 = (JAWT_X11DrawingSurfaceInfo*)dsi->platformInfo;
        ndt = (void*)dsi_x11->display;              // X11 Display*
        nwh = (void*)(uintptr_t)dsi_x11->drawable;  // X11 Window
        std::stringstream ss;
        ss << "X11 Display: " << ndt << ", Window: " << (unsigned long)(uintptr_t)nwh;
        jniLog(env, "INFO", "linux_view.cpp", ss.str().c_str());


        // Set into BGFX
        pd.ndt = ndt;
        pd.nwh = nwh;

        // Cleanup
        ds->FreeDrawingSurfaceInfo(dsi);
        ds->Unlock(ds);
        awt.FreeDrawingSurface(ds);

        return pd;
    }
}
