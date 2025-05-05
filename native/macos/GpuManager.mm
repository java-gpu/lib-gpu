#import <Foundation/Foundation.h>
#import <Metal/Metal.h>
#include "GpuManager.h"
#include <jni.h>
#include "jni_utils.h"

const char* getSystemDefaultGPUName(JNIEnv* env) {
    @autoreleasepool {
        static char gpuName[256]; // large enough buffer
        id<MTLDevice> device = MTLCreateSystemDefaultDevice();
        if (device) {
            NSString* name = [device name];
            strncpy(gpuName, [name UTF8String], sizeof(gpuName));
            gpuName[sizeof(gpuName) - 1] = '\0'; // null-terminate just in case
            return gpuName;
        } else {
            return "Metal not supported";
        }
    }
}

size_t getGPUCount(JNIEnv* env) {
    @autoreleasepool {
        NSArray<id<MTLDevice>>* devices = MTLCopyAllDevices();
        return devices.count;
    }
}

// Retrieve the GPU name from a device pointer
const char* getGPUNameByPointer(JNIEnv* env, void* devicePtr) {
    id<MTLDevice> device = (__bridge id<MTLDevice>)devicePtr;
    if (device) {
        NSString* gpuName = device.name;
        return [gpuName UTF8String];  // Return the name as a C-string
    }
    return "Unknown GPU";  // Default return if device is NULL
}

// Release the GPU device pointer
void releaseGpu(JNIEnv* env, void* devicePtr) {
    if (devicePtr) {
        // No explicit release required for the MTLDevice pointer in Objective-C (handled by ARC)
        // But we can manually release it if needed in certain cases:
        // The pointer is transferred to Java and we use __bridge to ensure proper memory management
        id<MTLDevice> device = (id<MTLDevice>)devicePtr;
        device = nil;
        // Device will be released automatically when it goes out of scope (ARC).
    }
}

void* getGPUPointerAtIndex(JNIEnv* env, size_t index) {
    NSArray<id<MTLDevice>>* devices = MTLCopyAllDevices();
    if (index >= devices.count) {
       jniLog(env, "ERROR", "GpuManager.mm", "Index out of bounds");
       env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Index out of bounds");
       return nil;
    }
    id<MTLDevice> device = [devices objectAtIndex:index];
    return (__bridge void*) device;
}

const char* getGPUNameAtIndex(JNIEnv* env, size_t index) {
    NSArray<id<MTLDevice>>* devices = MTLCopyAllDevices();
    if (index >= devices.count) {
       jniLog(env, "ERROR", "GpuManager.mm", "Index out of bounds");
       env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Index out of bounds");
       return nil;
    }
    id<MTLDevice> device = [devices objectAtIndex:index];
    if (device) {
        NSString* gpuName = device.name;
        return [gpuName UTF8String];  // Return the name as a C-string
    }
    jniLog(env, "ERROR", "GpuManager.mm", "Cannot retrieve name!");
    env->ThrowNew(env->FindClass("tech/lib/bgfx/ex/JniRuntimeException"), "Cannot retrieve name!");
    return nil;
}