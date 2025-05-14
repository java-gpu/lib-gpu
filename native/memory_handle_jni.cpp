#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>

#include "jni_utils.h"
#include "tech_lib_bgfx_data_TransientIndexBuffer.h"
#include "tech_lib_bgfx_data_TransientVertexBuffer.h"

#include <bgfx/bgfx.h>
#include <cstring>

extern "C" {
    /*
     * Class:     tech_lib_bgfx_data_TransientIndexBuffer
     * Method:    copyFrom
     * Signature: (Ljava/nio/ByteBuffer;)V
     */
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_data_TransientIndexBuffer_copyFrom(JNIEnv* env, jobject obj, jobject srcBuffer) {
        if (srcBuffer == nullptr) return;

        jclass cls = env->GetObjectClass(obj);
        jmethodID mid = env->GetMethodID(cls, "data", "()J");
        jlong dstPtr = env->CallLongMethod(obj, mid);

        void* dst = reinterpret_cast<void*>(dstPtr);
        void* src = env->GetDirectBufferAddress(srcBuffer);
        jlong size = env->GetDirectBufferCapacity(srcBuffer);

        if (src && dst) {
            std::memcpy(dst, src, (size_t)size);
        }
    }

    /*
     * Class:     tech_lib_bgfx_data_TransientVertexBuffer
     * Method:    copyFrom
     * Signature: (Ljava/nio/ByteBuffer;)V
     */
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_data_TransientVertexBuffer_copyFrom(JNIEnv* env, jobject obj, jobject srcBuffer) {
        if (srcBuffer == nullptr) return;

        // Get native address to copy destination
        jclass cls = env->GetObjectClass(obj);
        jmethodID mid = env->GetMethodID(cls, "data", "()J");
        jlong dstPtr = env->CallLongMethod(obj, mid);

        void* dst = reinterpret_cast<void*>(dstPtr);
        void* src = env->GetDirectBufferAddress(srcBuffer);
        jlong size = env->GetDirectBufferCapacity(srcBuffer);

        if (src && dst) {
            std::memcpy(dst, src, (size_t)size);
        }
    }
}