#import <Foundation/Foundation.h>
#import <Metal/Metal.h>
#include "GpuInfo.h"

const char* getSystemDefaultGPUName() {
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

size_t getMetalGPUCount() {
    @autoreleasepool {
        NSArray<id<MTLDevice>>* devices = MTLCopyAllDevices();
        return devices.count;
    }
}

// Retrieve the GPU name from a device pointer
const char* getGPUNameByPointer(void* devicePtr) {
    id<MTLDevice> device = (__bridge id<MTLDevice>)devicePtr;
    if (device) {
        NSString* gpuName = device.name;
        return [gpuName UTF8String];  // Return the name as a C-string
    }
    return "Unknown GPU";  // Default return if device is NULL
}

// Release the GPU device pointer
void releaseGpu(void* devicePtr) {
    if (devicePtr) {
        // No explicit release required for the MTLDevice pointer in Objective-C (handled by ARC)
        // But we can manually release it if needed in certain cases:
        // The pointer is transferred to Java and we use __bridge to ensure proper memory management
        id<MTLDevice> device = (id<MTLDevice>)devicePtr;
        device = nil;
        // Device will be released automatically when it goes out of scope (ARC).
    }
}

void* getGPUPointerAtIndex(size_t index) {
    NSArray<id<MTLDevice>>* devices = MTLCopyAllDevices();
    if (index >= devices.count) return "Index out of bounds";
    id<MTLDevice> device = [devices objectAtIndex:index];
    return (__bridge void*) device;
}

const char* getMetalGPUNameAtIndex(size_t index) {
    NSArray<id<MTLDevice>>* devices = MTLCopyAllDevices();
    if (index >= devices.count) return "Index out of bounds";
    id<MTLDevice> device = [devices objectAtIndex:index];
    if (device) {
        NSString* gpuName = device.name;
        return [gpuName UTF8String];  // Return the name as a C-string
    }
    return "Null returned";  // Default return if device is NULL
}