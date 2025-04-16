#ifndef TEXTURE_H
#define TEXTURE_H

#ifdef __cplusplus
extern "C" {
#endif

    void* loadTextureFromFile(void* devicePtr, const char* path, bool useMipmaps, int pixelFormat);
    void* loadTextureFromStream(void* devicePtr, const unsigned char* data, size_t length, bool useMipmaps, int pixelFormat);
    void* createEmptyTexture(void* devicePtr, int width, int height, int pixelFormat);
    void releaseTexture(void* texturePtr);

#ifdef __cplusplus
}
#endif

#endif /* TEXTURE_H */
