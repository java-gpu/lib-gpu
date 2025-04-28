#import "tech_gpu_lib_metal_jni_MetalRenderer.h"
#import <Metal/Metal.h>
#import <Foundation/Foundation.h>
#import <QuartzCore/CAMetalLayer.h>
#import <AppKit/AppKit.h>
// #import <UIKit/UIKit.h>  // For image loading

@interface MetalRendererImpl : NSObject
@property (nonatomic, strong) id<MTLDevice> device;
@property (nonatomic, strong) id<MTLCommandQueue> commandQueue;
@property (nonatomic, strong) id<MTLRenderPipelineState> pipelineState;
@property (nonatomic, strong) id<MTLBuffer> vertexBuffer;
@property (nonatomic, strong) id<MTLBuffer> indexBuffer;
@property (nonatomic, strong) id<MTLTexture> spriteTexture;
@property (nonatomic, strong) id<MTLSamplerState> samplerState;
@end

@implementation MetalRendererImpl

- (instancetype)initWithGPUIndex:(int)gpuIndex {
    self = [super init];
    if (self) {
        NSArray<id<MTLDevice>> *devices = MTLCopyAllDevices();
        if (gpuIndex < 0 || gpuIndex >= (int)devices.count) {
            NSLog(@"[MetalRenderer] Invalid GPU index: %d. Falling back to default device.", gpuIndex);
            _device = MTLCreateSystemDefaultDevice();
        } else {
            _device = devices[gpuIndex];
        }

        if (!_device) {
            NSLog(@"[MetalRenderer] Failed to acquire Metal device.");
            return nil;
        }

        _commandQueue = [_device newCommandQueue];
    }
    return self;
}

- (void)draw {
    id<MTLCommandBuffer> buffer = [_commandQueue commandBuffer];
    [buffer commit];
    [buffer waitUntilCompleted];
}

@end

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_tech_gpu_lib_metal_jni_MetalRenderer_init
  (JNIEnv *env, jobject obj, jint gpuIndex) {
    MetalRendererImpl* impl = [[MetalRendererImpl alloc] initWithGPUIndex:gpuIndex];
    return (jlong)CFBridgingRetain(impl);
}

JNIEXPORT void JNICALL Java_tech_gpu_lib_metal_jni_MetalRenderer_draw
  (JNIEnv *env, jobject obj) {
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID fid = (*env)->GetFieldID(env, cls, "nativeHandle", "J");
    jlong ptr = (*env)->GetLongField(env, obj, fid);
    MetalRendererImpl* impl = (__bridge MetalRendererImpl*)(void*)ptr;
    [impl draw];
}

JNIEXPORT void JNICALL Java_tech_gpu_lib_metal_jni_MetalRenderer_release
  (JNIEnv *env, jobject obj) {
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID fid = (*env)->GetFieldID(env, cls, "nativeHandle", "J");
    jlong ptr = (*env)->GetLongField(env, obj, fid);
    MetalRendererImpl* impl = (__bridge MetalRendererImpl*)(void*)ptr;
    impl = nil;
}

#ifdef __cplusplus
}
#endif
