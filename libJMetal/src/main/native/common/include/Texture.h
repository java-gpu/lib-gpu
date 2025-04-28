#ifndef TEXTURE_H
#define TEXTURE_H

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

    typedef struct {
        uint32_t width;
        uint32_t height;
        uint32_t depth;
    } TextureSize;

    void* loadTextureFromFile(void* devicePtr, const char* path, bool useMipmaps, int pixelFormat);
    void* loadTextureFromStream(void* devicePtr, const unsigned char* data, size_t length, bool useMipmaps, int pixelFormat);
    void* createEmptyTexture(void* devicePtr, int width, int height, int pixelFormat);
    void releaseTexture(void* texturePtr);

    TextureSize get_mtl_texture_size(void* texturePtr);

#ifdef __cplusplus
}
#endif

#endif /* TEXTURE_H */
