#import <Foundation/Foundation.h>
#import <Cocoa/Cocoa.h>
#import <Metal/Metal.h>
#include "Texture.h"

void* loadTextureFromFile(void* devicePtr, const char* path, bool useMipmaps, int pixelFormat) {
    printf("Debug: Loading texture from file %s and useMipmaps %s for format %d\n", path, useMipmaps ? "true" : "false", pixelFormat);

    printf("DEBUG: Retrieving MTLDevice from Device Pointer....\n");
    id<MTLDevice> device = (__bridge id<MTLDevice>)devicePtr;

    NSString* nsPath = [NSString stringWithUTF8String:path];
    printf("DEBUG: System path parsed!\n");

    if ([[NSFileManager defaultManager] fileExistsAtPath:nsPath]) {
        printf("DEBUG: File exists!\n");
    } else {
        printf("ERROR: File does not exist at path: %s\n", path);
    }

    NSImage* image = [[NSImage alloc] initWithContentsOfFile:nsPath];
    printf("DEBUG: Loading NSImage....\n");
    if (!image) return NULL;
    printf("Debug: NSImage loaded successfully!\n");

    NSBitmapImageRep* rep = [[NSBitmapImageRep alloc] initWithData:[image TIFFRepresentation]];
    if (!rep) return NULL;
    printf("Debug: NSBitmapImageRep loaded successfully!\n");

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

    printf("Debug: Building MTLTextureDescriptor object....\n");
    MTLTextureDescriptor* desc = [MTLTextureDescriptor texture2DDescriptorWithPixelFormat:(MTLPixelFormat)pixelFormat
                                                                                     width:width
                                                                                    height:height
                                                                                 mipmapped:useMipmaps];
    printf("Debug: Creating MTLTexture object....\n");
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

    return (__bridge void*) texture;
}

void* loadTextureFromStream(void* devicePtr, const unsigned char* data, size_t length, bool useMipmaps, int pixelFormat) {
    id<MTLDevice> device = (__bridge id<MTLDevice>)devicePtr;

    printf("DEBUG: Converting to NSData....\n");
    // Convert byte array to NSData
    NSData* imageData = [NSData dataWithBytes:data length:length];
    printf("DEBUG: Creating NSData....\n");
    NSImage* image = [[NSImage alloc] initWithData:imageData];
    if (!image) return NULL;

    printf("DEBUG: Creating NSBitmapImageRep....\n");
    NSBitmapImageRep* rep = [[NSBitmapImageRep alloc] initWithData:[image TIFFRepresentation]];
    if (!rep) return NULL;

    NSInteger width = rep.pixelsWide;
    NSInteger height = rep.pixelsHigh;

    printf("DEBUG: Creating NSGraphicsContext....\n");
    NSGraphicsContext* context = [NSGraphicsContext graphicsContextWithBitmapImageRep:rep];
    [NSGraphicsContext saveGraphicsState];
    [NSGraphicsContext setCurrentContext:context];
    [image drawInRect:NSMakeRect(0, 0, width, height)
             fromRect:NSZeroRect
            operation:NSCompositingOperationSourceOver
             fraction:1.0];
    [NSGraphicsContext restoreGraphicsState];

    printf("DEBUG: Creating MTLTextureDescriptor....\n");
    MTLTextureDescriptor* desc = [MTLTextureDescriptor texture2DDescriptorWithPixelFormat:(MTLPixelFormat)pixelFormat
                                                                                     width:width
                                                                                    height:height
                                                                                 mipmapped:useMipmaps];

    printf("DEBUG: Creating MTLTexture....\n");
    id<MTLTexture> texture = [device newTextureWithDescriptor:desc];
    [texture replaceRegion:MTLRegionMake2D(0, 0, width, height)
               mipmapLevel:0
                 withBytes:[rep bitmapData]
               bytesPerRow:rep.bytesPerRow];

    if (useMipmaps) {
        printf("DEBUG: useMipmaps....\n");
        id<MTLCommandQueue> queue = [device newCommandQueue];
        id<MTLCommandBuffer> buffer = [queue commandBuffer];
        id<MTLBlitCommandEncoder> blit = [buffer blitCommandEncoder];
        [blit generateMipmapsForTexture:texture];
        [blit endEncoding];
        [buffer commit];
        [buffer waitUntilCompleted];
    }

    printf("DEBUG: return....\n");
    return (__bridge void*) texture;
}

void* createEmptyTexture(void* devicePtr, int width, int height, int pixelFormat) {
    id<MTLDevice> device = (__bridge id<MTLDevice>)devicePtr;

    MTLTextureDescriptor* desc = [MTLTextureDescriptor texture2DDescriptorWithPixelFormat:(MTLPixelFormat)pixelFormat
                                                                                     width:width
                                                                                    height:height
                                                                                 mipmapped:NO];
    printf("Debug: Creating an empty texture with %d and height %d\n", width, height);
    id<MTLTexture> texture = [device newTextureWithDescriptor:desc];

    return (__bridge void*) texture;
}

void releaseTexture(void* texturePtr) {
    id<MTLTexture> texture = (id<MTLTexture>) texturePtr;
    texture = nil;
}

TextureSize get_mtl_texture_size(void* texturePtr) {
    id<MTLTexture> texture = (__bridge id<MTLTexture>)texturePtr;

    TextureSize size;
    size.width = (uint32_t)[texture width];
    size.height = (uint32_t)[texture height];
    size.depth = (uint32_t)[texture depth];

    return size;
}
