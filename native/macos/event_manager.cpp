#include <IOKit/hid/IOHIDManager.h>
#include <CoreFoundation/CoreFoundation.h>
#include <jni.h>
#include "jni_utils.h"
#include "event_manager_jni.h"
#include <map>
#include <mutex>

std::map<IOHIDDeviceRef, int> deviceToGamepadId;
std::mutex deviceMapMutex;
int nextGamepadId = 0;

void axisCallback(void* context, IOReturn result, void* sender, IOHIDValueRef value) {
    IOHIDElementRef elem = IOHIDValueGetElement(value);
    uint32_t usagePage = IOHIDElementGetUsagePage(elem);
    uint32_t usage = IOHIDElementGetUsage(elem);

    if (usagePage == kHIDPage_GenericDesktop) {
        CFIndex intValue = IOHIDValueGetIntegerValue(value);
        float normalized = intValue / 255.0f;

        IOHIDDeviceRef device = (IOHIDDeviceRef)sender;
        
        // Retrieve the gamepad ID from the map
        std::lock_guard<std::mutex> lock(deviceMapMutex);
        auto it = deviceToGamepadId.find(device);
        if (it == deviceToGamepadId.end()) return;  // Device not found
        
        int32_t gamepadId = static_cast<int32_t>(it->second);

        JNIEnv* env;
        if (jvm->AttachCurrentThread((void**)&env, nullptr) != 0) return;
        sendAxisEvent(env, gamepadId, usage, normalized);
    }
}

void deviceConnected(void* context, IOReturn result, void* sender, IOHIDDeviceRef device) {
    std::lock_guard<std::mutex> lock(deviceMapMutex);

    // Assign a new ID to this device
    int gamepadId = nextGamepadId++;
    deviceToGamepadId[device] = gamepadId;

    JNIEnv* env;
    if (jvm->AttachCurrentThread((void**)&env, nullptr) != 0) return;
    sendGamepadConnectedEvent(env, gamepadId);
}

void deviceDisconnected(void* context, IOReturn result, void* sender, IOHIDDeviceRef device) {
    std::lock_guard<std::mutex> lock(deviceMapMutex);

    auto it = deviceToGamepadId.find(device);
    if (it == deviceToGamepadId.end()) return;

    int gamepadId = it->second;
    deviceToGamepadId.erase(it);

    JNIEnv* env;
    if (jvm->AttachCurrentThread((void**)&env, nullptr) != 0) return;
    sendGamepadDisconnectedEvent(env, gamepadId);
}

 
void pollGamepads(JNIEnv* env) {
    IOHIDManagerRef manager = IOHIDManagerCreate(kCFAllocatorDefault, kIOHIDOptionsTypeNone);
    
    CFMutableDictionaryRef criteria = CFDictionaryCreateMutable(kCFAllocatorDefault,
        0, &kCFTypeDictionaryKeyCallBacks, &kCFTypeDictionaryValueCallBacks);

    int usagePage = kHIDPage_GenericDesktop;
    int usage = kHIDUsage_GD_GamePad;

    CFNumberRef pageRef = CFNumberCreate(kCFAllocatorDefault, kCFNumberIntType, &usagePage);
    CFNumberRef usageRef = CFNumberCreate(kCFAllocatorDefault, kCFNumberIntType, &usage);

    CFDictionarySetValue(criteria, CFSTR(kIOHIDDeviceUsagePageKey), pageRef);
    CFDictionarySetValue(criteria, CFSTR(kIOHIDDeviceUsageKey), usageRef);

    CFRelease(pageRef);
    CFRelease(usageRef);

    IOHIDManagerSetDeviceMatching(manager, criteria);

    IOHIDManagerRegisterInputValueCallback(manager, axisCallback, nullptr);
    IOHIDManagerRegisterDeviceMatchingCallback(manager, deviceConnected, nullptr);
    IOHIDManagerRegisterDeviceRemovalCallback(manager, deviceDisconnected, nullptr);

    IOHIDManagerScheduleWithRunLoop(manager, CFRunLoopGetCurrent(), kCFRunLoopDefaultMode);
    IOHIDManagerOpen(manager, kIOHIDOptionsTypeNone);

    CFRunLoopRun(); // Blocks forever, runs on current thread
}
