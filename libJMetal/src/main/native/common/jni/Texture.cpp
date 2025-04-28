#include <jni.h>
#import "tech_gpu_lib_jni_TextureNative.h"
#import "Texture.h"
#include <cstdio>

#ifdef __cplusplus
extern "C" {
#endif
    JNIEXPORT jobject JNICALL Java_tech_gpu_lib_jni_TextureNative_loadTexture
      (JNIEnv* env, jclass jclzz, jlong gpuPointer, jstring jpath, jboolean useMipmaps, jint pixelFormat) {

        const char* path = env->GetStringUTFChars(jpath, 0);
        void* texturePtr = loadTextureFromFile((void*) gpuPointer, (const char*) path, useMipmaps, pixelFormat);
        env->ReleaseStringUTFChars(jpath, path);

        if (!texturePtr) return nullptr;
        TextureSize size = get_mtl_texture_size(texturePtr);

        // Find the TextureInfo Java class
        jclass textureInfoClass = env->FindClass("tech/gpu/lib/graphics/TextureInfo");
        if (!textureInfoClass) return nullptr;

        // Get the constructor of TextureInfo (constructor takes texturePointer, width, height, depth)
        jmethodID ctor = env->GetMethodID(textureInfoClass, "<init>", "(JIII)V");
        if (!ctor) return nullptr;

        // Create a new TextureInfo object and return it
        return env->NewObject(textureInfoClass, ctor, reinterpret_cast<jlong>(texturePtr), size.width, size.height, size.depth);
    }

    JNIEXPORT jobject JNICALL Java_tech_gpu_lib_jni_TextureNative_loadTextureFromStream
      (JNIEnv* env, jclass jclzz, jlong gpuPointer, jobject imageStream, jboolean useMipmaps, jint pixelFormat) {
        printf("Enter loadTextureFromStream....\n");
        // Convert InputStream to byte array
        jclass inputStreamClass = env->GetObjectClass(imageStream);
        jmethodID readMethod = env->GetMethodID(inputStreamClass, "readAllBytes", "()[B");
        printf("Convert InputStream to byte array....\n");
        jbyteArray byteArray = (jbyteArray)env->CallObjectMethod(imageStream, readMethod);
        printf("Reading data length....\n");
        jsize length = env->GetArrayLength(byteArray);
        printf("Length %d\n", length);
        jbyte* byteData = env->GetByteArrayElements(byteArray, NULL);

        // Pass byte data to Objective-C for texture creation
        void* texturePtr = loadTextureFromStream((void*) gpuPointer, (unsigned char*)byteData, length, useMipmaps, pixelFormat);

        env->ReleaseByteArrayElements(byteArray, byteData, JNI_ABORT);  // Release byte array

        if (!texturePtr) return nullptr;
        TextureSize size = get_mtl_texture_size(texturePtr);

        // Find the TextureInfo Java class
        jclass textureInfoClass = env->FindClass("tech/gpu/lib/graphics/TextureInfo");
        if (!textureInfoClass) return nullptr;

        // Get the constructor of TextureInfo (constructor takes texturePointer, width, height, depth)
        jmethodID ctor = env->GetMethodID(textureInfoClass, "<init>", "(JIII)V");
        if (!ctor) return nullptr;

        // Create a new TextureInfo object and return it
        return env->NewObject(textureInfoClass, ctor, reinterpret_cast<jlong>(texturePtr), size.width, size.height, size.depth);
    }

    // JNI method to create an empty texture
    JNIEXPORT jobject JNICALL Java_tech_gpu_lib_jni_TextureNative_createEmptyTexture
      (JNIEnv* env, jclass jclzz, jlong gpuPointer, jint width, jint height, jint pixelFormat) {

        void* texturePtr = createEmptyTexture((void*) gpuPointer, width, height, pixelFormat);

        if (!texturePtr) return nullptr;
        TextureSize size = get_mtl_texture_size(texturePtr);

        // Find the TextureInfo Java class
        jclass textureInfoClass = env->FindClass("tech/gpu/lib/graphics/TextureInfo");
        if (!textureInfoClass) return nullptr;

        // Get the constructor of TextureInfo (constructor takes texturePointer, width, height, depth)
        jmethodID ctor = env->GetMethodID(textureInfoClass, "<init>", "(JIII)V");
        if (!ctor) return nullptr;

        // Create a new TextureInfo object and return it
        return env->NewObject(textureInfoClass, ctor, reinterpret_cast<jlong>(texturePtr), size.width, size.height, size.depth);
    }

    JNIEXPORT void JNICALL Java_tech_gpu_lib_jni_TextureNative_release(JNIEnv* env, jclass jclzz, jlong texturePointer) {
        releaseTexture((void*) texturePointer);
    }

    // Get texture info (width, height, depth) using the pointer
    JNIEXPORT jobject JNICALL Java_tech_gpu_lib_jni_TextureNative_getTextureInfo (JNIEnv* env, jclass jclzz, jlong texturePointer) {
        TextureSize size = get_mtl_texture_size(reinterpret_cast<void*>(texturePointer));

        // Find the TextureInfo Java class
        jclass textureInfoClass = env->FindClass("tech/gpu/lib/graphics/TextureInfo");
        if (!textureInfoClass) return nullptr;

        // Get the constructor of TextureInfo (constructor takes texturePointer, width, height, depth)
        jmethodID ctor = env->GetMethodID(textureInfoClass, "<init>", "(JIII)V");
        if (!ctor) return nullptr;

        // Create a new TextureInfo object and return it
        return env->NewObject(textureInfoClass, ctor, texturePointer, size.width, size.height, size.depth);
    }

#ifdef __cplusplus
}
#endif