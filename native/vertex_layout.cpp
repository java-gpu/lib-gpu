#include <jni.h>
#include <bgfx/bgfx.h>
#include <memory>
#include <jni_utils.h>

// Convenience macro to get native pointer
#define LAYOUT_PTR reinterpret_cast<bgfx::VertexLayout*>(layoutPtr)

extern "C" {

JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_VertexLayout_begin(JNIEnv* env, jobject obj, jobject rendererType) {
    auto* layout = new bgfx::VertexLayout();
    bgfx::RendererType::Enum type = fromJRendererToBgfxRendererType(env, rendererType);
    layout->begin(type);
    return reinterpret_cast<jlong>(layout);
}

JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_VertexLayout_add
  (JNIEnv* env, jobject obj, jlong layoutPtr, jint attrib, jint num, jint type, jboolean normalized, jboolean asInt) {
    LAYOUT_PTR->add(
        (bgfx::Attrib::Enum)attrib,
        (uint8_t)num,
        (bgfx::AttribType::Enum)type,
        normalized == JNI_TRUE,
        asInt == JNI_TRUE
    );
}

// Signature: private native void end(long layoutPtr);
JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_VertexLayout_end(JNIEnv* env, jobject obj, jlong layoutPtr) {
    LAYOUT_PTR->end();
}

// Signature: private native void destroy(long layoutPtr);
JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_VertexLayout_destroy(JNIEnv* env, jobject obj, jlong layoutPtr) {
    delete LAYOUT_PTR;
}

} // extern "C"
