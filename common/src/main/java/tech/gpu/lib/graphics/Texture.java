/*******************************************************************************
 * Copyright 2025 See AUTHORS file.
 * <br />
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <br />
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <br />
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package tech.gpu.lib.graphics;

import tech.gpu.lib.Application;
import tech.gpu.lib.GpuManager;
import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.util.CommonNativeLoader;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Texture wraps for native APIs.
 */
public class Texture {

    static {
        CommonNativeLoader.load();
    }

    public final static Map<Application, List<Texture>> managedTextures = new HashMap<>();

    private long texturePointer;

    private boolean useMipMaps;
    private int mipMapLevelCount;
    private PixelFormat format;

    protected Application app;

    public Texture(Application app, String internalPath) throws PixelFormatNotSupportedException {
        this(app, internalPath, null, false);
    }

    public Texture(Application app, File file) throws PixelFormatNotSupportedException {
        this(app, file, null, false);
    }

    public Texture(Application app, File file, boolean useMipMaps) throws PixelFormatNotSupportedException {
        this(app, file, null, useMipMaps);
    }

    public Texture(Application app, int width, int height, PixelFormat pixelFormat) throws PixelFormatNotSupportedException {
        this.app = app;
        PixelFormatConverter pixelFormatConverter = app.getGraphics().getPixelFormatConverter();
        if (pixelFormat == null) {
            pixelFormat = pixelFormatConverter.getDefaultFormat();
        }
        this.texturePointer = createEmptyTexture(width, height, pixelFormatConverter.toNativeFormat(pixelFormat));
    }

    public Texture(Application app, long texturePointer) {
        this.app = app;
        this.texturePointer = texturePointer;
    }

    public Texture(Application app, String filePath, PixelFormat format, boolean useMipMaps) throws PixelFormatNotSupportedException {
        this.app = app;
        this.format = format;
        this.useMipMaps = useMipMaps;
        if (format == null) {
            this.texturePointer = loadTexture(filePath, useMipMaps);
        } else {
            this.texturePointer = loadTexture(filePath, useMipMaps, app.getGraphics().getPixelFormatConverter().toNativeFormat(format));
        }
    }

    public Texture(Application app, File file, PixelFormat format, boolean useMipMaps) throws PixelFormatNotSupportedException {
        this(app, file.getAbsolutePath(), format, useMipMaps);
    }

    public Texture(Application app, InputStream ins, PixelFormat format, boolean useMipMaps) throws PixelFormatNotSupportedException {
        this.app = app;
        this.format = format;
        this.useMipMaps = useMipMaps;
        if (format == null) {
            this.texturePointer = loadTextureFromStream(ins, useMipMaps);
        } else {
            this.texturePointer = loadTextureFromStream(ins, useMipMaps, app.getGraphics().getPixelFormatConverter().toNativeFormat(format));
        }
    }

    // Native method to load the texture
    protected long loadTexture(String fullPath, boolean useMipMaps) throws PixelFormatNotSupportedException {
        var converter = app.getGraphics().getPixelFormatConverter();
        return loadTexture(GpuManager.gpuSelected.getPointer(), fullPath, useMipMaps, converter.toNativeFormat(converter.getDefaultFormat()));
    }

    protected long loadTexture(String fullPath, boolean useMipMaps, int pixelFormat) {
        return loadTexture(GpuManager.gpuSelected.getPointer(), fullPath, useMipMaps, pixelFormat);
    }

    protected native long loadTexture(long gpuPointer, String fullPath, boolean useMipMaps, int pixelFormat);

    protected long loadTextureFromStream(InputStream imageStream, boolean useMipMaps) throws PixelFormatNotSupportedException {
        var converter = app.getGraphics().getPixelFormatConverter();
        return loadTextureFromStream(GpuManager.gpuSelected.getPointer(), imageStream, useMipMaps, converter.toNativeFormat(converter.getDefaultFormat()));
    }

    protected long loadTextureFromStream(InputStream imageStream, boolean useMipMaps, int pixelFormat) {
        return loadTextureFromStream(GpuManager.gpuSelected.getPointer(), imageStream, useMipMaps, pixelFormat);
    }

    protected native long loadTextureFromStream(long gpuPointer, InputStream imageStream, boolean useMipMaps, int pixelFormat);

    // Create texture
    protected long createEmptyTexture(int width, int height, int pixelFormat) {
        return createEmptyTexture(GpuManager.gpuSelected.getPointer(), width, height, pixelFormat);
    }

    protected native long createEmptyTexture(long gpuPointer, int width, int height, int pixelFormat);

    // Release from memory
    public native void release(long texturePointer);

    public void release() {
        release(texturePointer);
    }

    /**
     * Disposes all resources associated with the texture
     */
    public void dispose() {
        managedTextures.get(app).remove(this);
        release();
    }

    /**
     * Clears all managed textures. This is an internal method. Do not use it!
     */
    public static void clearAllTextures(Application app) {
        managedTextures.remove(app);
    }
}
