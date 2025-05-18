#include "jni_utils.h"
#include <jni.h>
#include <cstdio>

void jniLog(JNIEnv* env, const char* level, const char* fileName, const char* message) {
    // 1. Find the logger class
    jclass loggerClass = env->FindClass("tech/lib/bgfx/util/JniLogger");
    if (loggerClass == nullptr) {
        printf("❌ Failed to find loggerClass JniLogger\n");
        return;
    }

    // 2. Find the singleton method ID
    jmethodID getInstanceMethod = env->GetStaticMethodID(
        loggerClass,
        "getInstance",
        "()Ltech/lib/bgfx/util/JniLogger;"
    );
    if (getInstanceMethod == nullptr) {
        printf("❌ Failed to find getInstance method in JniLogger\n");
        return;
    }

    // 3. Get logger instance
    jobject loggerInstance = env->CallStaticObjectMethod(loggerClass, getInstanceMethod);

    // 4. Find the method ID
    jmethodID logMethod = env->GetMethodID(
        loggerClass,
        "log",
        "(Ltech/lib/bgfx/jni/JniLogData$LogLevel;Ljava/lang/String;Ljava/lang/String;)V"
    );

    // 5. Get LogLevel enum value
    jclass logLevelClass = env->FindClass("tech/lib/bgfx/jni/JniLogData$LogLevel");
    jfieldID levelField = env->GetStaticFieldID(logLevelClass, level, "Ltech/lib/bgfx/jni/JniLogData$LogLevel;");
    jobject logLevelObj = env->GetStaticObjectField(logLevelClass, levelField);

    // 6. Convert fileName and message to jstring
    jstring fileNameStr = env->NewStringUTF(fileName);
    jstring messageStr = env->NewStringUTF(message);

    // 7. Call the method
    env->CallVoidMethod(loggerInstance, logMethod, logLevelObj, fileNameStr, messageStr);
}

// Convert Java enum to bgfx::RendererType::Enum
bgfx::RendererType::Enum fromJRendererToBgfxRendererType(JNIEnv* env, jobject jRendererType) {
    jclass enumClass = env->GetObjectClass(jRendererType);
    jmethodID ordinalMethod = env->GetMethodID(enumClass, "ordinal", "()I");
    jint ordinal = env->CallIntMethod(jRendererType, ordinalMethod);
    return static_cast<bgfx::RendererType::Enum>(ordinal);
}


// JNI method to map bgfx::Caps::Limits to Java BgfxLimits
jobject fromLimits(JNIEnv* env, const bgfx::Caps::Limits& limits) {
    // Find the Java BgfxLimits class
    jclass limitsClass = env->FindClass("tech/lib/bgfx/jni/BgfxLimits");
    if (limitsClass == nullptr) {
        // Handle error if class is not found
        return nullptr;
    }

    // Get the constructor of BgfxLimits class: (all the required integers)
    jmethodID constructor = env->GetMethodID(limitsClass, "<init>", "(IIIIIIIIIIIIIIIIIIIIII)V");
    if (constructor == nullptr) {
        // Handle error if constructor is not found
        return nullptr;
    }

    // Create the Java BgfxLimits object and initialize it with values from bgfx::Caps::Limits
    jobject limitsObject = env->NewObject(
        limitsClass, constructor,
        limits.maxDrawCalls,
        limits.maxBlits,
        limits.maxTextureSize,
        limits.maxTextureLayers,
        limits.maxViews,
        limits.maxFrameBuffers,
        limits.maxFBAttachments,
        limits.maxPrograms,
        limits.maxShaders,
        limits.maxTextures,
        limits.maxTextureSamplers,
        limits.maxComputeBindings,
        limits.maxVertexLayouts,
        limits.maxVertexStreams,
        limits.maxIndexBuffers,
        limits.maxVertexBuffers,
        limits.maxDynamicIndexBuffers,
        limits.maxDynamicVertexBuffers,
        limits.maxUniforms,
        limits.maxOcclusionQueries,
        limits.maxEncoders,
        limits.minResourceCbSize,
        limits.transientVbSize,
        limits.transientIbSize
    );

    return limitsObject;
}

const char* getRendererTypeName(bgfx::RendererType::Enum type) {
    switch (type) {
        case bgfx::RendererType::Noop: return "Noop";
        case bgfx::RendererType::Agc: return "Agc";
        case bgfx::RendererType::Direct3D11: return "Direct3D11";
        case bgfx::RendererType::Direct3D12: return "Direct3D12";
        case bgfx::RendererType::Gnm: return "Gnm";
        case bgfx::RendererType::Metal: return "Metal";
        case bgfx::RendererType::Nvn: return "Nvn";
        case bgfx::RendererType::OpenGLES: return "OpenGL";
        case bgfx::RendererType::OpenGL: return "OpenGLES";
        case bgfx::RendererType::Vulkan: return "Vulkan";
        default: return "Unknown";
    }
}