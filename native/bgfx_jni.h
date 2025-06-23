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

#include <GLFW/glfw3.h>
// Needed for native access: X11/Wayland
#include <GLFW/glfw3native.h>

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

    jboolean bgfx_initGeneral(JNIEnv* env, jclass clzz, jlong windowPointer, jboolean priority3D, jint gpuIndex, int8_t windowType, jobject canvas);

    #ifdef __APPLE__
        extern void* getNSViewFromCanvas(JNIEnv* env, jobject canvas);
        // extern void* createNativeWindow(JNIEnv* env, int x, int y, int width, int height, const char* title);
    #endif

    #ifdef __linux__
        bgfx::PlatformData setupLinuxPlatformDataForAwt(JNIEnv* env, jobject canvas);
        bgfx::PlatformData setupLinuxPlatformDataForGlfw(JNIEnv* env, GLFWwindow* window);
    #endif
}