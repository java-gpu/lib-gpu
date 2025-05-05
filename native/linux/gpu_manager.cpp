#include "GpuManager.h"
#include <jni.h>
#include "jni_utils.h"

#include <vulkan/vulkan.h>
#include <vector>
#include <string>
#include <iostream>

static VkInstance g_instance = VK_NULL_HANDLE;
static std::vector<VkPhysicalDevice> g_devices;
static std::vector<std::string> g_deviceNames;

static void initializeVulkanDevices() {
    if (!g_devices.empty()) return;

    VkApplicationInfo appInfo{};
    appInfo.sType = VK_STRUCTURE_TYPE_APPLICATION_INFO;
    appInfo.pApplicationName = "GPUInfo";
    appInfo.applicationVersion = VK_MAKE_VERSION(1, 0, 0);
    appInfo.pEngineName = "None";
    appInfo.engineVersion = VK_MAKE_VERSION(1, 0, 0);
    appInfo.apiVersion = VK_API_VERSION_1_0;

    VkInstanceCreateInfo createInfo{};
    createInfo.sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
    createInfo.pApplicationInfo = &appInfo;

    if (vkCreateInstance(&createInfo, nullptr, &g_instance) != VK_SUCCESS) {
        std::cerr << "Failed to create Vulkan instance." << std::endl;
        return;
    }

    uint32_t deviceCount = 0;
    vkEnumeratePhysicalDevices(g_instance, &deviceCount, nullptr);
    if (deviceCount == 0) {
        std::cerr << "No Vulkan-compatible GPUs found." << std::endl;
        return;
    }

    g_devices.resize(deviceCount);
    vkEnumeratePhysicalDevices(g_instance, &deviceCount, g_devices.data());

    for (VkPhysicalDevice dev : g_devices) {
        VkPhysicalDeviceProperties props;
        vkGetPhysicalDeviceProperties(dev, &props);
        g_deviceNames.emplace_back(props.deviceName);
    }
}

const char* getSystemDefaultGPUName(JNIEnv* env) {
    initializeVulkanDevices();
    if (!g_deviceNames.empty())
        return g_deviceNames[0].c_str();
    return nullptr;
}

size_t getGPUCount(JNIEnv* env) {
    initializeVulkanDevices();
    return g_devices.size();
}

void* getGPUPointerAtIndex(JNIEnv* env, size_t index) {
    initializeVulkanDevices();
    if (index >= g_devices.size()) return nullptr;
    return g_devices[index];
}

const char* getGPUNameAtIndex(JNIEnv* env, size_t index) {
    initializeVulkanDevices();
    if (index >= g_deviceNames.size()) return nullptr;
    return g_deviceNames[index].c_str();
}

const char* getGPUNameByPointer(JNIEnv* env, void* devicePtr) {
    if (!devicePtr) return nullptr;

    VkPhysicalDevice dev = reinterpret_cast<VkPhysicalDevice>(devicePtr);
    VkPhysicalDeviceProperties props;
    vkGetPhysicalDeviceProperties(dev, &props);

    static thread_local std::string name;
    name = props.deviceName;
    return name.c_str();
}

void releaseGpu(JNIEnv* env, void* devicePtr) {
    // Vulkan physical devices are not manually released
    (void)devicePtr;
}
