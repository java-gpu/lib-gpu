#ifndef GPUINFO_H
#define GPUINFO_H

#ifdef __cplusplus
extern "C" {
#endif

    const char* getSystemDefaultGPUName();

    size_t getMetalGPUCount();

    const char* getGPUNameByPointer(void* devicePtr);

    void* getGPUPointerAtIndex(size_t index);

    void releaseGpu(void* devicePtr);

    const char* getMetalGPUNameAtIndex(size_t index);

#ifdef __cplusplus
}
#endif

#endif /* GPUINFO_H */
