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
// <-- required
#include <fstream>
#include <cstring>
// brings in mtxLookAt, mtxProj, etc.
#include <bx/math.h>
#include <filesystem>

#include "bgfx_jni.h"

#include <GLFW/glfw3.h>
// Needed for native access: X11/Wayland
#include <GLFW/glfw3native.h>

/**
* JNI Type Codes Summary (for quick reference):
  JNI Code ->	Java Type
  Z	boolean
  B	byte
  C	char
  S	short
  I	int
  J	long
  F	float
  D	double
  V	void
  L<classname>;	Object (class reference)
  [<type>	Array (e.g., [I is int[])
  (Ltech/lib/ui/event/KeyEnum;IZJ)V => input: (Object "tech/lib/ui/event/KeyEnum", int, boolean, long) and return void
**/

// Function to map the Java enum to bgfx enum
bgfx::TextureFormat::Enum javaToBgfxTextureFormat(JNIEnv* env, jobject format) {
    jint value = env->CallIntMethod(format, env->GetMethodID(env->GetObjectClass(format), "getValue", "()I"));
    return static_cast<bgfx::TextureFormat::Enum>(value);
}

extern "C" {

    JNIEXPORT jboolean JNICALL Java_tech_lib_bgfx_jni_Bgfx_initForAwt(JNIEnv* env, jclass clzz, jlong windowPointer, jobject canvas, jboolean priority3D, jint gpuIndex) {
        return bgfx_initGeneral(env, clzz, windowPointer, priority3D, gpuIndex, 1, canvas);
    }

    JNIEXPORT jboolean JNICALL Java_tech_lib_bgfx_jni_Bgfx_initForGlfw(JNIEnv* env, jclass clzz, jlong windowPointer, jboolean priority3D, jint gpuIndex) {
        return bgfx_initGeneral(env, clzz, windowPointer, priority3D, gpuIndex, 2, nullptr);
    }

    /**
     * windowType: 1 is AWT, 2 is GLFW
     */
    jboolean bgfx_initGeneral(JNIEnv* env, jclass clzz, jlong windowPointer, jboolean priority3D, jint gpuIndex, int8_t windowType, jobject canvas) {
        try {
            bgfx::Init init;
            bgfx::PlatformData pd{};

            #ifdef __linux__
                init.type = bgfx::RendererType::Vulkan;
                jniLog(env, "ERROR", "bgfx_jni.cpp#initGeneral", "BGFX using Vulkan as backend!");
                if (windowType == 1 && canvas != null) {
                    pd = setupLinuxPlatformDataForAwt(env, canvas);
                } else if (windowType == 2) {
                    GLFWwindow* window = reinterpret_cast<GLFWwindow*>(windowPointer);
                    pd = setupLinuxPlatformDataForGlfw(env, window);
                }
            #else
                pd.nwh = reinterpret_cast<void*>(windowPointer);

                if (pd.nwh == nullptr) {
                    jniLog(env, "ERROR", "bgfx_jni.cpp#initForGlfw", "❌ No window handler set!");
                    return JNI_FALSE;
                } else {
                    std::stringstream ss;
                    ss << "Native window handle = " << pd.nwh;
                    jniLog(env, "DEBUG", "bgfx_jni.cpp#initForGlfw", ss.str().c_str());
                }

                pd.ndt = nullptr;
                pd.context = nullptr;
                pd.backBuffer = nullptr;
                pd.backBufferDS = nullptr;

                #ifdef __APPLE__
                    init.type = bgfx::RendererType::Metal;
                    jniLog(env, "ERROR", "bgfx_jni.cpp#initGeneral", "BGFX using Metal as backend!");
                #else
                    if (priority3D) {
                        init.type = bgfx::RendererType::Direct3D12;
                        jniLog(env, "ERROR", "bgfx_jni.cpp#initGeneral", "BGFX using Direct3D12 as backend!");
                    } else {
                        init.type = bgfx::RendererType::Direct3D11;
                        jniLog(env, "ERROR", "bgfx_jni.cpp#initGeneral", "BGFX using Direct3D11 as backend!");
                    }
                #endif
            #endif

            bgfx::setPlatformData(pd);
            init.platformData = pd;

            if (gpuIndex >= 0) {
                std::stringstream ss;
                ss << "Set custom GPU index: " << gpuIndex << std::endl;
                jniLog(env, "ERROR", "bgfx_jni.cpp#initGeneral", ss.str().c_str());
                init.deviceId = gpuIndex;
            }

            jniLog(env, "DEBUG", "bgfx_jni.cpp#initGeneral", "Starting BGFX initialize...");
            bool success = bgfx::init(init);
            if (!success) {
                jniLog(env, "ERROR", "bgfx_jni.cpp#initGeneral", "BGFX init failed!");
                return JNI_FALSE;
            } else {
                jniLog(env, "DEBUG", "bgfx_jni.cpp#initGeneral", "✅ BGFX initialize completed!");

                bgfx::RendererType::Enum renderer = bgfx::getRendererType();
                std::stringstream ss;
                ss << "Renderer type:  " << getRendererTypeName(renderer) << std::endl;
                jniLog(env, "ERROR", "bgfx_jni.cpp#initGeneral", ss.str().c_str());

                return JNI_TRUE;
            }
        } catch (const std::exception& e) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), e.what());
            return JNI_FALSE;
        } catch (...) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknown native exception");
            return JNI_FALSE;
        }
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_shutdown(JNIEnv* env, jclass) {
        jniLog(env, "DEBUG", "bgfx_jni.cpp#shutdown", "Shutting down BGFX...");
        try {
            bgfx::shutdown();
            jniLog(env, "DEBUG", "bgfx_jni.cpp#shutdown", "🔥 Shutdown!!");
        } catch (const std::exception& e) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), e.what());
        } catch (...) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknown native exception");
        }
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_frame(JNIEnv* env, jclass) {
        jniLog(env, "DEBUG", "bgfx_jni.cpp", "Calling BGFX frame...");
        try {
            bgfx::frame();
        } catch (const std::exception& e) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), e.what());
        } catch (...) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Unknown native exception");
        }
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_getMacOSNativeHandlerFromCanvas(JNIEnv* env, jclass, jobject canvas) {
        #ifdef __APPLE__
            return reinterpret_cast<jlong> (getNSViewFromCanvas(env, canvas));
        #endif
        return 0;
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_reset(JNIEnv* env, jclass, jint width, jint height, jint flag) {
        bgfx::reset(width, height, flag);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_touch(JNIEnv* env, jclass, jint viewId) {
        bgfx::touch(viewId);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setViewRect(JNIEnv* env, jclass, jint viewId, jint x, jint y, jint width, jint height) {
        bgfx::setViewRect(viewId, x, y, width, height);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_drawQuad(JNIEnv* env, jclass, jfloat x, jfloat y, jfloat width, jfloat height, jint abgrColor) {
    
        PosColorVertex::init();

        static PosColorVertex quadVertices[] = {
            { 0,    0,     0.0f, 0 },
            { 0,    100,   0.0f, 0 },
            { 100,  0,     0.0f, 0 },
            { 100,  100,   0.0f, 0 }
        };
    
        static const uint16_t quadIndices[] = { 0, 1, 2, 1, 3, 2 };
    
        for (int i = 0; i < 4; i++) {
            quadVertices[i].x = x + (i % 2 == 0 ? 0 : width);
            quadVertices[i].y = y + (i < 2 ? 0 : height);
            quadVertices[i].abgr = abgrColor;
        }
    
        bgfx::TransientVertexBuffer tvb;
        bgfx::TransientIndexBuffer tib;
    
        bgfx::allocTransientVertexBuffer(&tvb, 4, PosColorVertex::ms_layout);
        bgfx::allocTransientIndexBuffer(&tib, 6);
    
        memcpy(tvb.data, quadVertices, sizeof(quadVertices));
        memcpy(tib.data, quadIndices, sizeof(quadIndices));
    
        bgfx::setVertexBuffer(0, &tvb);
        bgfx::setIndexBuffer(&tib);
        bgfx::setState(BGFX_STATE_DEFAULT);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setViewClear(JNIEnv* env, jclass, jint viewId, jint clearFlags,
            jint rgba, jfloat depth, jbyte stencil) {
        bgfx::setViewClear(
            static_cast<bgfx::ViewId>(viewId),
            static_cast<uint16_t>(clearFlags),
            static_cast<uint32_t>(rgba),
            depth,
            static_cast<uint8_t>(stencil)
        );
    }

    // Load binary shader file and create bgfx shader
    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_loadShader (JNIEnv* env, jclass, jstring jpath) {
        const char* pathCStr = env->GetStringUTFChars(jpath, nullptr);
        std::string path(pathCStr);
        env->ReleaseStringUTFChars(jpath, pathCStr);

        std::ifstream file(path, std::ios::binary | std::ios::ate);
        if (!file.is_open()) {
            std::string cwd;
            try {
                cwd = std::filesystem::current_path().string();
            } catch (...) {
                cwd = "<unable to get current path>";
            }
            std::ostringstream oss;
            oss << "Fail to open file path: " << path
                << "\nCurrent working directory: " << cwd
                << "\nerrno: " << errno << " (" << std::strerror(errno) << ")";

             env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), oss.str().c_str());
            return 0; // Failed to open file
        }

        std::streamsize size = file.tellg();
        file.seekg(0, std::ios::beg);

        const bgfx::Memory* mem = bgfx::alloc(static_cast<uint32_t>(size));
        if (!file.read(reinterpret_cast<char*>(mem->data), size)) {
            env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Failt to read file content!");
            return 0; // Failed to read entire file
        }

        bgfx::ShaderHandle handle = bgfx::createShader(mem);
        return static_cast<jlong>(handle.idx);
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_loadShaderFromMemory(JNIEnv* env, jclass, jobject shaderData) {
        void* data = env->GetDirectBufferAddress(shaderData);
        jlong size = env->GetDirectBufferCapacity(shaderData);

        if (!data || size <= 0)
            return -1;

        const bgfx::Memory* mem = bgfx::copy(data, (uint32_t)size);
        bgfx::ShaderHandle handle = bgfx::createShader(mem);
        return (jlong) handle.idx;
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_createProgram(JNIEnv*, jclass, jlong vsHandle, jlong fsHandle, jboolean destroyShaders) {
        bgfx::ProgramHandle handle = bgfx::createProgram(
            { static_cast<uint16_t>(vsHandle) },
            { static_cast<uint16_t>(fsHandle) },
            destroyShaders == JNI_TRUE
        );
        return handle.idx;
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_createVertexBuffer(JNIEnv* env, jclass, jobject jbuffer, jlong layoutPtr) {
        void* data = env->GetDirectBufferAddress(jbuffer);
        jlong size = env->GetDirectBufferCapacity(jbuffer);

        const bgfx::Memory* mem = bgfx::copy(data, (uint32_t)size);
        bgfx::VertexLayout* layout = reinterpret_cast<bgfx::VertexLayout*>(layoutPtr);
        jniLog(env, "DEBUG", "bgfx_jni.cpp#createVertexBuffer", "Retrieved vertex layout!!");
        jniLog(env, "DEBUG", "bgfx_jni.cpp#createVertexBuffer", "Creating vertex buffer handle...");
        bgfx::VertexBufferHandle handle = bgfx::createVertexBuffer(mem, *layout);
        jniLog(env, "DEBUG", "bgfx_jni.cpp#createVertexBuffer", "Return!");
        return handle.idx;
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_createIndexBuffer(JNIEnv* env, jclass, jobject jbuffer) {
        void* data = env->GetDirectBufferAddress(jbuffer);
        jlong size = env->GetDirectBufferCapacity(jbuffer);

        const bgfx::Memory* mem = bgfx::copy(data, (uint32_t)size);
        bgfx::IndexBufferHandle handle = bgfx::createIndexBuffer(mem);
        return handle.idx;
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setViewTransform(JNIEnv* env, jclass clazz, jint viewId, jfloatArray jView, jfloatArray jProj)
    {
        jfloat* view = env->GetFloatArrayElements(jView, 0);
        jfloat* proj = env->GetFloatArrayElements(jProj, 0);

        // bgfx_set_view_transform expects 4x4 matrices (16 floats)
        bgfx::setViewTransform((uint16_t) viewId, view, proj);

        env->ReleaseFloatArrayElements(jView, view, 0);
        env->ReleaseFloatArrayElements(jProj, proj, 0);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setTransform(JNIEnv* env, jclass, jfloatArray matrixArray) {
        jfloat* matrix = env->GetFloatArrayElements(matrixArray, nullptr);
        bgfx::setTransform(matrix);
        env->ReleaseFloatArrayElements(matrixArray, matrix, JNI_ABORT);
    }
    
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setVertexBuffer(JNIEnv*, jclass, jint stream, jlong vbh) {
        bgfx::VertexBufferHandle handle = { (uint16_t)vbh };
        bgfx::setVertexBuffer((uint8_t)stream, handle);
    }
    
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setIndexBuffer(JNIEnv*, jclass, jlong ibh) {
        bgfx::IndexBufferHandle handle = { (uint16_t)ibh };
        bgfx::setIndexBuffer(handle);
    }
    
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setState(JNIEnv*, jclass, jlong state) {
        bgfx::setState((uint64_t)state);
    }
    
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_submit(JNIEnv*, jclass, jint viewId, jlong program) {
        bgfx::submit((uint8_t)viewId, { (uint16_t)program });
    }
    
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_destroyVertexBuffer(JNIEnv*, jclass, jlong vbh) {
        bgfx::destroy(bgfx::VertexBufferHandle{ (uint16_t)vbh });
    }
    
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_destroyIndexBuffer(JNIEnv*, jclass, jlong ibh) {
        bgfx::destroy(bgfx::IndexBufferHandle{ (uint16_t)ibh });
    }
    
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_destroyProgram(JNIEnv*, jclass, jlong program) {
        bgfx::destroy(bgfx::ProgramHandle{ (uint16_t)program });
    }

    JNIEXPORT jobject JNICALL Java_tech_lib_bgfx_jni_Bgfx_getRendererType(JNIEnv* env, jclass) {
        bgfx::RendererType::Enum type = bgfx::getRendererType();

        // Get RendererType enum class
        jclass enumClass = env->FindClass("tech/lib/bgfx/enu/RendererType");

        // Get static values() method
        jmethodID valuesMethod = env->GetStaticMethodID(enumClass, "values", "()[Ltech/lib/bgfx/enu/RendererType;");
        jobjectArray enumValues = (jobjectArray)env->CallStaticObjectMethod(enumClass, valuesMethod);

        return env->GetObjectArrayElement(enumValues, static_cast<jint>(type));
    }

    JNIEXPORT jshort JNICALL Java_tech_lib_bgfx_jni_Bgfx_createUniform(JNIEnv* env, jclass, jstring jname, jobject jtype) {
        const char* name = env->GetStringUTFChars(jname, nullptr);

        // Get UniformType.ordinal()
        jclass enumClass = env->GetObjectClass(jtype);
        jmethodID ordinalMethod = env->GetMethodID(enumClass, "ordinal", "()I");
        jint typeOrdinal = env->CallIntMethod(jtype, ordinalMethod);

        bgfx::UniformHandle handle = bgfx::createUniform(name, (bgfx::UniformType::Enum)typeOrdinal);

        env->ReleaseStringUTFChars(jname, name);

        return (jshort)handle.idx;
    }


    // JNI method to create a texture
    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_createNativeTexture2D(JNIEnv* env, jclass,
        jshort width, jshort height, jboolean hasMips, jbyte numLayers, jobject format, jint flags, jobject buffer) {

        // Get the raw data from the ByteBuffer
        void* data = env->GetDirectBufferAddress(buffer);

        // Convert the Java TextureFormat enum to bgfx TextureFormat
        bgfx::TextureFormat::Enum bgfxFormat = javaToBgfxTextureFormat(env, format);

        // Create the texture using bgfx
        bgfx::TextureHandle textureHandle = bgfx::createTexture2D(
            width, height, hasMips, numLayers, bgfxFormat, flags, bgfx::copy(data, width * height * 4)
        );

        // Return the texture handle (as a jlong)
        return (jlong) textureHandle.idx;
    }

    JNIEXPORT jobject JNICALL Java_tech_lib_bgfx_jni_Bgfx_copy(JNIEnv* env, jclass, jobject data, jint size) {
        // Get direct buffer address (native memory) from the ByteBuffer
        void* bufferData = env->GetDirectBufferAddress(data);

        // Create a bgfx::Memory object and assign the data and size
        const bgfx::Memory* mem = bgfx::alloc(size);
        memcpy(mem->data, bufferData, size);

        // Wrap the native memory (mem->data) in a direct ByteBuffer
        jobject byteBuffer = env->NewDirectByteBuffer(mem->data, size);

        // Optional: Store mem pointer somewhere (e.g., return it in another JNI method or track in a map)
        // You must manage mem's lifecycle manually if not passing to bgfx::createTexture2D or similar

        return byteBuffer;
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_destroyTexture(JNIEnv*, jclass, jlong handle) {
        bgfx::destroy(bgfx::TextureHandle{ (uint16_t)handle });
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_destroyUniform(JNIEnv*, jclass, jshort handleIdx) {
        bgfx::UniformHandle handle = { static_cast<uint16_t>(handleIdx) };
        bgfx::destroy(handle);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setViewName(JNIEnv* env, jclass, jint viewId, jstring jname) {
        const char* name = env->GetStringUTFChars(jname, nullptr);
        bgfx::setViewName((bgfx::ViewId)viewId, name);
        env->ReleaseStringUTFChars(jname, name);
    }

    // Java signature: void setViewMode(int viewId, ViewMode mode)
    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setViewMode(JNIEnv* env, jclass, jint viewId, jobject modeEnum) {
        // Get the class and method to extract the int value from the enum
        jclass enumClass = env->GetObjectClass(modeEnum);
        jmethodID getValueMethod = env->GetMethodID(enumClass, "getValue", "()I");

        jint modeValue = env->CallIntMethod(modeEnum, getValueMethod);

        bgfx::ViewMode::Enum mode;

        switch (modeValue) {
            case 0:
                mode = bgfx::ViewMode::Default;
                break;
            case 1:
                mode = bgfx::ViewMode::Sequential;
                break;
            case 2:
                mode = bgfx::ViewMode::DepthAscending;
                break;
            case 3:
                mode = bgfx::ViewMode::DepthDescending;
                break;
            default:
                mode = bgfx::ViewMode::Default;
                break;
        }

        bgfx::setViewMode(static_cast<bgfx::ViewId> (viewId), mode);
    }

    JNIEXPORT jobject JNICALL Java_tech_lib_bgfx_jni_Bgfx_getCaps(JNIEnv* env, jclass cls) {
        const bgfx::Caps* caps = bgfx::getCaps();

        // Get Java class references
        jclass capsClass = env->FindClass("BgfxCaps");
        jclass limitsClass = env->FindClass("BgfxLimits");
        jclass rendererEnum = env->FindClass("RendererType");

        // Get constructors
        jmethodID capsConstructor = env->GetMethodID(capsClass, "<init>", "()V");
        jmethodID limitsConstructor = env->GetMethodID(limitsClass, "<init>", "()V");

        // Create Java objects
        jobject capsObj = env->NewObject(capsClass, capsConstructor);
        jobject limitsObj = env->NewObject(limitsClass, limitsConstructor);

        // Convert rendererType
        jmethodID fromIntMethod = env->GetStaticMethodID(rendererEnum, "fromInt", "(I)LRendererType;");
        jobject rendererObj = env->CallStaticObjectMethod(rendererEnum, fromIntMethod, caps->rendererType);

        // Set fields in BgfxCaps
        env->SetObjectField(capsObj, env->GetFieldID(capsClass, "rendererType", "LRendererType;"), rendererObj);
        env->SetLongField(capsObj, env->GetFieldID(capsClass, "supported", "J"), static_cast<jlong>(caps->supported));
        env->SetIntField(capsObj, env->GetFieldID(capsClass, "vendorId", "I"), static_cast<jint>(caps->vendorId));
        env->SetIntField(capsObj, env->GetFieldID(capsClass, "deviceId", "I"), static_cast<jint>(caps->deviceId));
        env->SetBooleanField(capsObj, env->GetFieldID(capsClass, "homogeneousDepth", "Z"), static_cast<jboolean>(caps->homogeneousDepth));
        env->SetBooleanField(capsObj, env->GetFieldID(capsClass, "originBottomLeft", "Z"), static_cast<jboolean>(caps->originBottomLeft));
        env->SetIntField(capsObj, env->GetFieldID(capsClass, "numGPUs", "I"), static_cast<jint>(caps->numGPUs));

        // Populate BgfxLimits fields
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxDrawCalls", "I"), static_cast<jint>(caps->limits.maxDrawCalls));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxBlits", "I"), static_cast<jint>(caps->limits.maxBlits));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxTextureSize", "I"), static_cast<jint>(caps->limits.maxTextureSize));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxTextureLayers", "I"), static_cast<jint>(caps->limits.maxTextureLayers));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxViews", "I"), static_cast<jint>(caps->limits.maxViews));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxFrameBuffers", "I"), static_cast<jint>(caps->limits.maxFrameBuffers));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxFBAttachments", "I"), static_cast<jint>(caps->limits.maxFBAttachments));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxPrograms", "I"), static_cast<jint>(caps->limits.maxPrograms));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxShaders", "I"), static_cast<jint>(caps->limits.maxShaders));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxTextures", "I"), static_cast<jint>(caps->limits.maxTextures));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxTextureSamplers", "I"), static_cast<jint>(caps->limits.maxTextureSamplers));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxComputeBindings", "I"), static_cast<jint>(caps->limits.maxComputeBindings));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxVertexLayouts", "I"), static_cast<jint>(caps->limits.maxVertexLayouts));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxVertexStreams", "I"), static_cast<jint>(caps->limits.maxVertexStreams));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxIndexBuffers", "I"), static_cast<jint>(caps->limits.maxIndexBuffers));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxVertexBuffers", "I"), static_cast<jint>(caps->limits.maxVertexBuffers));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxDynamicIndexBuffers", "I"), static_cast<jint>(caps->limits.maxDynamicIndexBuffers));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxDynamicVertexBuffers", "I"), static_cast<jint>(caps->limits.maxDynamicVertexBuffers));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxUniforms", "I"), static_cast<jint>(caps->limits.maxUniforms));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxOcclusionQueries", "I"), static_cast<jint>(caps->limits.maxOcclusionQueries));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "maxEncoders", "I"), static_cast<jint>(caps->limits.maxEncoders));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "minResourceCbSize", "I"), static_cast<jint>(caps->limits.minResourceCbSize));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "transientVbSize", "I"), static_cast<jint>(caps->limits.transientVbSize));
        env->SetIntField(limitsObj, env->GetFieldID(limitsClass, "transientIbSize", "I"), static_cast<jint>(caps->limits.transientIbSize));

        return capsObj;
    }

    JNIEXPORT jint JNICALL Java_tech_lib_bgfx_jni_Bgfx_getAvailTransientVertexBuffer(JNIEnv* env, jclass clazz, jint num, jlong vertexLayoutPtr) {
        const bgfx::VertexLayout* layout = reinterpret_cast<const bgfx::VertexLayout*>(vertexLayoutPtr);
        return bgfx::getAvailTransientVertexBuffer(static_cast<uint32_t>(num), *layout);
    }

    JNIEXPORT jint JNICALL Java_tech_lib_bgfx_jni_Bgfx_getAvailTransientIndexBuffer (JNIEnv* env, jclass clazz, jint num) {
        return bgfx::getAvailTransientIndexBuffer(static_cast<uint32_t>(num));
    }

    JNIEXPORT jobject JNICALL Java_tech_lib_bgfx_jni_Bgfx_allocTransientVertexBuffer(JNIEnv* env, jclass clazz, jint num, jlong layoutPtr) {
        auto* layout = reinterpret_cast<bgfx::VertexLayout*>(layoutPtr);
        auto* tvb = new bgfx::TransientVertexBuffer();

        bgfx::allocTransientVertexBuffer(tvb, static_cast<uint32_t>(num), *layout);

        jclass cls = env->FindClass("tech/lib/bgfx/jni/TransientVertexBuffer");
        jmethodID ctor = env->GetMethodID(cls, "<init>", "(J)V");
        return env->NewObject(cls, ctor, reinterpret_cast<jlong>(tvb));
    }

    JNIEXPORT jobject JNICALL Java_tech_lib_bgfx_jni_Bgfx_allocTransientIndexBuffer(JNIEnv* env, jclass clazz, jint num, jboolean index32) {
        auto* tib = new bgfx::TransientIndexBuffer();

        bgfx::allocTransientIndexBuffer(tib, static_cast<uint32_t>(num), index32 == JNI_TRUE);

        jclass cls = env->FindClass("tech/lib/bgfx/jni/TransientIndexBuffer");
        jmethodID ctor = env->GetMethodID(cls, "<init>", "(J)V");
        return env->NewObject(cls, ctor, reinterpret_cast<jlong>(tib));
    }

    JNIEXPORT jobject JNICALL Java_tech_lib_bgfx_jni_Bgfx_begin(JNIEnv* env, jclass clazz) {
        bgfx::Encoder* encoder = bgfx::begin();

        if (!encoder) {
            return nullptr;
        }

        // Find BgfxEncoder class and constructor
        jclass encoderClass = env->FindClass("tech/lib/bgfx/util/BgfxEncoder");
        jmethodID constructor = env->GetMethodID(encoderClass, "<init>", "(J)V");

        // Create and return the Java BgfxEncoder object wrapping the native pointer
        return env->NewObject(encoderClass, constructor, (jlong) encoder);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_util_BgfxEncoder_end(JNIEnv* env, jobject obj) {
        jclass cls = env->GetObjectClass(obj);
        jmethodID mid = env->GetMethodID(cls, "getPtr", "()J");
        jlong ptr = env->CallLongMethod(obj, mid);

        bgfx::end((bgfx::Encoder*) ptr);
    }

    JNIEXPORT jlong JNICALL Java_tech_lib_bgfx_jni_Bgfx_STATE_1BLEND_1FUNC(JNIEnv* env, jclass, jlong src, jlong dst) {
        return BGFX_STATE_BLEND_FUNC(src, dst);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_setUniform(JNIEnv* env, jclass clazz, jint uniformHandle, jfloatArray lodEnabled) {

        // Get the array length
        jsize length = env->GetArrayLength(lodEnabled);

        // Create a float array to hold the values
        jfloat* lodEnabledElements = env->GetFloatArrayElements(lodEnabled, JNI_FALSE);

        // Pass the array to bgfx::setUniform
        bgfx::UniformHandle handle = { static_cast<uint16_t>(uniformHandle) };
        bgfx::setUniform(handle, lodEnabledElements,  static_cast<uint16_t>(length));

        // Release the array elements when done
        env->ReleaseFloatArrayElements(lodEnabled, lodEnabledElements, 0);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_mtxLookAt(JNIEnv* env, jclass clazz, jfloatArray jView, jobject jEye, jobject jAt) {
        jclass vec3Class = env->GetObjectClass(jEye);
        jfieldID xField = env->GetFieldID(vec3Class, "x", "F");
        jfieldID yField = env->GetFieldID(vec3Class, "y", "F");
        jfieldID zField = env->GetFieldID(vec3Class, "z", "F");

        bx::Vec3 eyeVec = {
            env->GetFloatField(jEye, xField),
            env->GetFloatField(jEye, yField),
            env->GetFloatField(jEye, zField),
        };

        bx::Vec3 atVec = {
            env->GetFloatField(jAt, xField),
            env->GetFloatField(jAt, yField),
            env->GetFloatField(jAt, zField),
        };

        float view[16];
        bx::mtxLookAt(view, eyeVec, atVec);

        env->SetFloatArrayRegion(jView, 0, 16, view);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_mtxProj
      (JNIEnv* env, jclass clazz, jfloatArray jProj, jfloat fovy, jfloat aspect, jfloat znear, jfloat zfar, jboolean homogeneousDepth)
    {
        jfloat proj[16];

        bx::mtxProj(proj, fovy, aspect, znear, zfar, homogeneousDepth == JNI_TRUE);

        env->SetFloatArrayRegion(jProj, 0, 16, proj);
    }

    JNIEXPORT void JNICALL Java_tech_lib_bgfx_jni_Bgfx_mtxRotateXY(JNIEnv* env, jclass cls, jfloatArray jResultMatrix, jfloat angleX, jfloat angleY) {
        if (env->GetArrayLength(jResultMatrix) < 16) {
            // Matrix must be 4x4
            return;
        }

        jfloat* resultPtr = env->GetFloatArrayElements(jResultMatrix, nullptr);
        if (!resultPtr) return;

        // Call bgfx
        bx::mtxRotateXY(resultPtr, angleX, angleY);

        // Commit changes back to Java
        env->ReleaseFloatArrayElements(jResultMatrix, resultPtr, 0);
    }

}