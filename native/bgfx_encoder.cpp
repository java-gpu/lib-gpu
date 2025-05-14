#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>

#include "jni_utils.h"
#include "tech_lib_bgfx_util_BgfxEncoder.h"

#include <bgfx/bgfx.h>
#include <bgfx/platform.h>
#include <exception>
#include <cstdio>
#include <sstream>
#include <fstream>         // <-- required
#include <cstring>

extern "C" {

    static jlong getEncoderPtr(JNIEnv* env, jobject obj) {
        jclass cls = (*env)->GetObjectClass(env, obj);
        jfieldID fid = (*env)->GetFieldID(env, cls, "nativePtr", "J");
        return (*env)->GetLongField(env, obj, fid);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_util_BgfxEncoder_setScissor(JNIEnv* env, jobject obj, jint x, jint y, jint width, jint height) {
        jlong ptr = getEncoderPtr(env, obj);
        bgfx_encoder_set_scissor((bgfx_encoder_t*)ptr, x, y, width, height);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_util_BgfxEncoder_setState(JNIEnv* env, jobject obj, jlong state) {
        jlong ptr = getEncoderPtr(env, obj);
        bgfx_encoder_set_state((bgfx_encoder_t*)ptr, (uint64_t)state, 0);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_util_BgfxEncoder_setTexture(JNIEnv* env, jobject obj, jint stage, jshort sampler, jint texture) {
        jlong ptr = getEncoderPtr(env, obj);
        bgfx_encoder_set_texture((bgfx_encoder_t*)ptr, (uint8_t)stage,
            (bgfx_uniform_handle_t){ sampler },
            (bgfx_texture_handle_t){ (uint16_t)texture },
            UINT32_MAX);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_util_BgfxEncoder_setVertexBuffer(JNIEnv* env, jobject obj, jint stream, jobject tvbObj, jint startVertex, jint numVertices) {
        jlong ptr = getEncoderPtr(env, obj);

        jclass cls = (*env)->GetObjectClass(env, tvbObj);
        jfieldID fid = (*env)->GetFieldID(env, cls, "nativePtr", "J");
        jlong tvbPtr = (*env)->GetLongField(env, tvbObj, fid);

        bgfx_encoder_set_transient_vertex_buffer((bgfx_encoder_t*)ptr, (uint8_t)stream,
            (const bgfx_transient_vertex_buffer_t*)tvbPtr, (uint32_t)startVertex, (uint32_t)numVertices);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_util_BgfxEncoder_setIndexBuffer(JNIEnv* env, jobject obj, jobject tibObj, jint firstIndex, jint numIndices) {
        jlong ptr = getEncoderPtr(env, obj);

        jclass cls = (*env)->GetObjectClass(env, tibObj);
        jfieldID fid = (*env)->GetFieldID(env, cls, "nativePtr", "J");
        jlong tibPtr = (*env)->GetLongField(env, tibObj, fid);

        bgfx_encoder_set_transient_index_buffer((bgfx_encoder_t*)ptr,
            (const bgfx_transient_index_buffer_t*)tibPtr, (uint32_t)firstIndex, (uint32_t)numIndices);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_util_BgfxEncoder_submit(JNIEnv* env, jobject obj, jint viewId, jlong shaderProgramHandle) {
        jlong ptr = getEncoderPtr(env, obj);
        bgfx_encoder_submit((bgfx_encoder_t*)ptr, (uint8_t)viewId,
            (bgfx_program_handle_t){ (uint16_t)shaderProgramHandle }, 0, false);
    }
}