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

struct PosColorVertex {
    float x, y, z;
    uint32_t abgr;

    // Static vertex layout (must be initialized)
    static bgfx::VertexLayout ms_layout;

    static void init() {
        ms_layout.begin()
            .add(bgfx::Attrib::Position, 3, bgfx::AttribType::Float)
            .add(bgfx::Attrib::Color0,   4, bgfx::AttribType::Uint8, true)
            .end();
    }
};

// Define the static member
bgfx::VertexLayout PosColorVertex::ms_layout;

extern "C" {

    #ifdef __APPLE__
        extern void* getNSViewFromCanvas(JNIEnv* env, jobject canvas);
        // extern void* createNativeWindow(JNIEnv* env, int x, int y, int width, int height, const char* title);
    #endif

    #ifdef __linux__
        bgfx::PlatformData setupLinuxPlatformData(JNIEnv* env, jobject canvas);
    #endif
}