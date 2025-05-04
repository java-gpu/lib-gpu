#include <jni.h>
#include <bgfx/bgfx.h>
#include <memory>

// Convenience macro to get native pointer
#define LAYOUT_PTR reinterpret_cast<bgfx::VertexLayout*>(layoutPtr)

extern "C" {

// Signature: private native long begin(int rendererType);
JNIEXPORT jlong JNICALL Java_tech_gpu_lib_bgfx_jni_VertexLayout_begin
  (JNIEnv* env, jobject obj, jint rendererType) {
    auto layout = new bgfx::VertexLayout();
    layout->begin((bgfx::RendererType::Enum)rendererType);
    return reinterpret_cast<jlong>(layout);
}

// Signature: private native void add(long layoutPtr, int attrib, int num, int type, boolean normalized, boolean asInt);
JNIEXPORT void JNICALL Java_tech_gpu_lib_bgfx_jni_VertexLayout_add
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
JNIEXPORT void JNICALL Java_tech_gpu_lib_bgfx_jni_VertexLayout_end
  (JNIEnv* env, jobject obj, jlong layoutPtr) {
    LAYOUT_PTR->end();
}

// Signature: private native void destroy(long layoutPtr);
JNIEXPORT void JNICALL Java_tech_gpu_lib_bgfx_jni_VertexLayout_destroy
  (JNIEnv* env, jobject obj, jlong layoutPtr) {
    delete LAYOUT_PTR;
}

} // extern "C"
