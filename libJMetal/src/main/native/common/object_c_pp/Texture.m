#import <Foundation/Foundation.h>
#import <Cocoa/Cocoa.h>
#import <Metal/Metal.h>
#include "Texture.h"

void* loadTextureFromFile(void* devicePtr, const char* path, bool useMipmaps, int pixelFormat) {
    id<MTLDevice> device = (__bridge id<MTLDevice>)devicePtr;
    NSString* nsPath = [NSString stringWithUTF8String:path];

    NSImage* image = [[NSImage alloc] initWithContentsOfFile:nsPath];
    if (!image) return NULL;

    NSBitmapImageRep* rep = [[NSBitmapImageRep alloc] initWithData:[image TIFFRepresentation]];
    if (!rep) return NULL;

    NSInteger width = rep.pixelsWide;
    NSInteger height = rep.pixelsHigh;

    NSGraphicsContext* context = [NSGraphicsContext graphicsContextWithBitmapImageRep:rep];
    [NSGraphicsContext saveGraphicsState];
    [NSGraphicsContext setCurrentContext:context];
    [image drawInRect:NSMakeRect(0, 0, width, height)
             fromRect:NSZeroRect
            operation:NSCompositingOperationSourceOver
             fraction:1.0];
    [NSGraphicsContext restoreGraphicsState];

    MTLTextureDescriptor* desc = [MTLTextureDescriptor texture2DDescriptorWithPixelFormat:(MTLPixelFormat)pixelFormat
                                                                                     width:width
                                                                                    height:height
                                                                                 mipmapped:useMipmaps];

    id<MTLTexture> texture = [device newTextureWithDescriptor:desc];
    [texture replaceRegion:MTLRegionMake2D(0, 0, width, height)
               mipmapLevel:0
                 withBytes:[rep bitmapData]
               bytesPerRow:rep.bytesPerRow];

    if (useMipmaps) {
        id<MTLCommandQueue> queue = [device newCommandQueue];
        id<MTLCommandBuffer> buffer = [queue commandBuffer];
        id<MTLBlitCommandEncoder> blit = [buffer blitCommandEncoder];
        [blit generateMipmapsForTexture:texture];
        [blit endEncoding];
        [buffer commit];
        [buffer waitUntilCompleted];
    }

    return (__bridge void*)texture;
}

void* loadTextureFromStream(void* devicePtr, const unsigned char* data, size_t length, bool useMipmaps, int pixelFormat) {
    id<MTLDevice> device = (__bridge id<MTLDevice>)devicePtr;

    // Convert byte array to NSData
    NSData* imageData = [NSData dataWithBytes:data length:length];
    NSImage* image = [[NSImage alloc] initWithData:imageData];
    if (!image) return NULL;

    NSBitmapImageRep* rep = [[NSBitmapImageRep alloc] initWithData:[image TIFFRepresentation]];
    if (!rep) return NULL;

    NSInteger width = rep.pixelsWide;
    NSInteger height = rep.pixelsHigh;

    NSGraphicsContext* context = [NSGraphicsContext graphicsContextWithBitmapImageRep:rep];
    [NSGraphicsContext saveGraphicsState];
    [NSGraphicsContext setCurrentContext:context];
    [image drawInRect:NSMakeRect(0, 0, width, height)
             fromRect:NSZeroRect
            operation:NSCompositingOperationSourceOver
             fraction:1.0];
    [NSGraphicsContext restoreGraphicsState];

    MTLTextureDescriptor* desc = [MTLTextureDescriptor texture2DDescriptorWithPixelFormat:(MTLPixelFormat)pixelFormat
                                                                                     width:width
                                                                                    height:height
                                                                                 mipmapped:useMipmaps];

    id<MTLTexture> texture = [device newTextureWithDescriptor:desc];
    [texture replaceRegion:MTLRegionMake2D(0, 0, width, height)
               mipmapLevel:0
                 withBytes:[rep bitmapData]
               bytesPerRow:rep.bytesPerRow];

    if (useMipmaps) {
        id<MTLCommandQueue> queue = [device newCommandQueue];
        id<MTLCommandBuffer> buffer = [queue commandBuffer];
        id<MTLBlitCommandEncoder> blit = [buffer blitCommandEncoder];
        [blit generateMipmapsForTexture:texture];
        [blit endEncoding];
        [buffer commit];
        [buffer waitUntilCompleted];
    }

    return (__bridge void*)texture;
}

void* createEmptyTexture(void* devicePtr, int width, int height, int pixelFormat) {
    id<MTLDevice> device = (__bridge id<MTLDevice>)devicePtr;

    MTLTextureDescriptor* desc = [MTLTextureDescriptor texture2DDescriptorWithPixelFormat:(MTLPixelFormat)pixelFormat
                                                                                     width:width
                                                                                    height:height
                                                                                 mipmapped:NO];

    id<MTLTexture> texture = [device newTextureWithDescriptor:desc];
    return (__bridge void*)texture;
}

void releaseTexture(void* texturePtr) {
    id<MTLTexture> texture = (id<MTLTexture>) texturePtr;
    texture = nil;
}
