#include <jni.h>
#import "tech_gpu_lib_graphics_Texture.h"
#import "Texture.h"

#ifdef __cplusplus
extern "C" {
#endif
    JNIEXPORT jlong JNICALL Java_tech_gpu_lib_graphics_Texture_loadTexture
      (JNIEnv* env, jobject obj, jlong gpuPointer, jstring jpath, jboolean useMipmaps, jint pixelFormat) {

        const char* path = env->GetStringUTFChars(jpath, 0);
        void* texture = loadTextureFromFile((void*) gpuPointer, (const char*) path, useMipmaps, pixelFormat);
        env->ReleaseStringUTFChars(jpath, path);
        return (jlong)texture;
    }

    JNIEXPORT jlong JNICALL Java_tech_gpu_lib_graphics_Texture_loadTextureFromStream
      (JNIEnv* env, jobject obj, jlong gpuPointer, jobject imageStream, jboolean useMipmaps, jint pixelFormat) {
        // Convert InputStream to byte array
        jclass inputStreamClass = env->GetObjectClass(imageStream);
        jmethodID readMethod = env->GetMethodID(inputStreamClass, "read", "()[B");
        jbyteArray byteArray = (jbyteArray)env->CallObjectMethod(imageStream, readMethod);
        jsize length = env->GetArrayLength(byteArray);
        jbyte* byteData = env->GetByteArrayElements(byteArray, NULL);

        // Pass byte data to Objective-C for texture creation
        void* texture = loadTextureFromStream((void*) gpuPointer, (unsigned char*)byteData, length, useMipmaps, pixelFormat);

        env->ReleaseByteArrayElements(byteArray, byteData, JNI_ABORT);  // Release byte array
        return (jlong)texture;
    }

    // JNI method to create an empty texture
    JNIEXPORT jlong JNICALL Java_tech_gpu_lib_graphics_Texture_createEmptyTexture
      (JNIEnv* env, jobject obj, jlong gpuPointer, jint width, jint height, jint pixelFormat) {

        void* texture = createEmptyTexture((void*) gpuPointer, width, height, pixelFormat);
        return (jlong)texture;
    }

    JNIEXPORT void JNICALL Java_tech_gpu_lib_graphics_Texture_release(JNIEnv* env, jobject obj, jlong texturePointer) {
        releaseTexture((void*) texturePointer);
    }

#ifdef __cplusplus
}
#endif