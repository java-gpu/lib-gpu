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

import lombok.Data;
import tech.gpu.lib.Application;
import tech.gpu.lib.ApplicationEnvironment;
import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.files.FileHandle;
import tech.gpu.lib.jni.GpuManager;
import tech.gpu.lib.jni.TextureNative;
import tech.gpu.lib.math.MathUtils;
import tech.gpu.lib.utils.BufferUtils;
import tech.gpu.lib.utils.CommonNativeLoader;
import tech.gpu.lib.utils.Disposable;

import java.io.File;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Texture wraps for native APIs.
 */
@Data
public class Texture implements Disposable {

    static {
        CommonNativeLoader.load();
    }

    public final static Map<Application, List<Texture>> managedTextures = new HashMap<>();

    private long texturePointer;

    private boolean useMipMaps;
    private int mipMapLevelCount;
    private PixelFormat format;

    protected Application app;
    private int width;
    private int height;
    private int depth;

    protected TextureFilter minFilter = TextureFilter.Nearest;
    protected TextureFilter magFilter = TextureFilter.Nearest;
    protected TextureWrap wrapU = TextureWrap.ClampToEdge;
    protected TextureWrap wrapV = TextureWrap.ClampToEdge;
    protected TextureWrap wrapW = TextureWrap.ClampToEdge;
    protected float anisotropicFilterLevel = 1.0f;
    private static float maxAnisotropicFilterLevel = 0;

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
        var textureInfo = createEmptyTexture(width, height, pixelFormatConverter.toNativeFormat(pixelFormat));
        readFromTextureInfo(textureInfo);
    }

    public Texture(Application app, long texturePointer) {
        this.app = app;
        this.texturePointer = texturePointer;
        readFromTextureInfo(TextureNative.getTextureInfo(this.texturePointer));
    }

    public Texture(Application app, TextureInfo textureInfo) {
        this.app = app;
        readFromTextureInfo(textureInfo);
    }

    public Texture(Application app, String filePath, PixelFormat format, boolean useMipMaps) throws PixelFormatNotSupportedException {
        this.app = app;
        this.format = format;
        this.useMipMaps = useMipMaps;
        TextureInfo textureInfo;
        if (format == null) {
            textureInfo = loadTexture(filePath, useMipMaps);
        } else {
            textureInfo = loadTexture(filePath, useMipMaps, app.getGraphics().getPixelFormatConverter().toNativeFormat(format));
        }
        readFromTextureInfo(textureInfo);
    }

    public Texture(FileHandle textureFile, boolean generateMipMaps) {
        this(ApplicationEnvironment.app, textureFile.file(), generateMipMaps);
    }

    public void readFromTextureInfo(TextureInfo textureInfo) {
        this.texturePointer = textureInfo.getTexturePointer();
        this.width = textureInfo.getWidth();
        this.height = textureInfo.getHeight();
        this.depth = textureInfo.getDepth();
    }

    // Create texture
    public TextureInfo createEmptyTexture(int width, int height, int pixelFormat) {
        return TextureNative.createEmptyTexture(GpuManager.gpuSelected.getPointer(), width, height, pixelFormat);
    }

    public Texture(Application app, File file, PixelFormat format, boolean useMipMaps) throws PixelFormatNotSupportedException {
        this(app, file.getAbsolutePath(), format, useMipMaps);
    }

    public Texture(Application app, InputStream ins, boolean useMipMaps) throws PixelFormatNotSupportedException {
        this(app, ins, null, useMipMaps);
    }

    public Texture(Application app, InputStream ins, PixelFormat format, boolean useMipMaps) throws PixelFormatNotSupportedException {
        this.app = app;
        this.format = format;
        this.useMipMaps = useMipMaps;
        TextureInfo textureInfo;
        if (format == null) {
            textureInfo = loadTextureFromStream(ins, useMipMaps);
        } else {
            textureInfo = loadTextureFromStream(ins, useMipMaps, app.getGraphics().getPixelFormatConverter().toNativeFormat(format));
        }
        readFromTextureInfo(textureInfo);
    }

    // Native method to load the texture
    public TextureInfo loadTexture(String fullPath, boolean useMipMaps) throws PixelFormatNotSupportedException {
        var converter = app.getGraphics().getPixelFormatConverter();
        return TextureNative.loadTexture(GpuManager.gpuSelected.getPointer(), fullPath, useMipMaps, converter.toNativeFormat(converter.getDefaultFormat()));
    }

    public TextureInfo loadTextureFromStream(InputStream imageStream, boolean useMipMaps) throws PixelFormatNotSupportedException {
        var converter = app.getGraphics().getPixelFormatConverter();
        return TextureNative.loadTextureFromStream(GpuManager.gpuSelected.getPointer(), imageStream, useMipMaps, converter.toNativeFormat(converter.getDefaultFormat()));
    }

    public TextureInfo loadTexture(String fullPath, boolean useMipMaps, int pixelFormat) {
        return TextureNative.loadTexture(GpuManager.gpuSelected.getPointer(), fullPath, useMipMaps, pixelFormat);
    }

    public TextureInfo loadTextureFromStream(InputStream imageStream, boolean useMipMaps, int pixelFormat) {
        return TextureNative.loadTextureFromStream(GpuManager.gpuSelected.getPointer(), imageStream, useMipMaps, pixelFormat);
    }

    public void release() {
        TextureNative.release(texturePointer);
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

    /**
     * Sets the {@link TextureWrap} for this texture on the u and v axis. Assumes the texture is bound and active!
     *
     * @param u the u wrap
     * @param v the v wrap
     */
    public void unsafeSetWrap(TextureWrap u, TextureWrap v) {
        unsafeSetWrap(u, v, false);
    }

    /**
     * Sets the {@link TextureWrap} for this texture on the u and v axis. Assumes the texture is bound and active!
     *
     * @param u     the u wrap
     * @param v     the v wrap
     * @param force True to always set the values, even if they are the same as the current values.
     */
    public void unsafeSetWrap(TextureWrap u, TextureWrap v, boolean force) {
//        if (u != null && (force || uWrap != u)) {
//            ApplicationEnvironment.gl.glTexParameteri(glTarget, GL20.GL_TEXTURE_WRAP_S, u.getGLEnum());
//            uWrap = u;
//        }
//        if (v != null && (force || vWrap != v)) {
//            ApplicationEnvironment.gl.glTexParameteri(glTarget, GL20.GL_TEXTURE_WRAP_T, v.getGLEnum());
//            vWrap = v;
//        }
        // TODO
    }

    /**
     * Sets the {@link TextureWrap} for this texture on the u and v axis. This will bind this texture!
     *
     * @param u the u wrap
     * @param v the v wrap
     */
    public void setWrap(TextureWrap u, TextureWrap v) {
        this.wrapU = u;
        this.wrapV = v;
//        bind();
//        ApplicationEnvironment.gl.glTexParameteri(glTarget, GL20.GL_TEXTURE_WRAP_S, u.getGLEnum());
//        ApplicationEnvironment.gl.glTexParameteri(glTarget, GL20.GL_TEXTURE_WRAP_T, v.getGLEnum());
        // TODO
    }

    /**
     * Sets the {@link TextureFilter} for this texture for minification and magnification. Assumes the texture is bound and
     * active!
     *
     * @param minFilter the minification filter
     * @param magFilter the magnification filter
     */
    public void unsafeSetFilter(TextureFilter minFilter, TextureFilter magFilter) {
        unsafeSetFilter(minFilter, magFilter, false);
    }

    /**
     * Sets the {@link TextureFilter} for this texture for minification and magnification. Assumes the texture is bound and
     * active!
     *
     * @param minFilter the minification filter
     * @param magFilter the magnification filter
     * @param force     True to always set the values, even if they are the same as the current values.
     */
    public void unsafeSetFilter(TextureFilter minFilter, TextureFilter magFilter, boolean force) {
//        if (minFilter != null && (force || this.minFilter != minFilter)) {
//            ApplicationEnvironment.gl.glTexParameteri(glTarget, GL20.GL_TEXTURE_MIN_FILTER, minFilter.getGLEnum());
//            this.minFilter = minFilter;
//        }
//        if (magFilter != null && (force || this.magFilter != magFilter)) {
//            ApplicationEnvironment.gl.glTexParameteri(glTarget, GL20.GL_TEXTURE_MAG_FILTER, magFilter.getGLEnum());
//            this.magFilter = magFilter;
//        }
        // TODO
    }

    /**
     * Sets the {@link TextureFilter} for this texture for minification and magnification. This will bind this texture!
     *
     * @param minFilter the minification filter
     * @param magFilter the magnification filter
     */
    public void setFilter(TextureFilter minFilter, TextureFilter magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;
//        bind();
//        ApplicationEnvironment.gl.glTexParameteri(glTarget, GL20.GL_TEXTURE_MIN_FILTER, minFilter.getGLEnum());
//        ApplicationEnvironment.gl.glTexParameteri(glTarget, GL20.GL_TEXTURE_MAG_FILTER, magFilter.getGLEnum());
        // TODO
    }

    /**
     * Sets the anisotropic filter level for the texture. Assumes the texture is bound and active!
     *
     * @param level The desired level of filtering. The maximum level supported by the device up to this value will be used.
     * @return The actual level set, which may be lower than the provided value due to device limitations.
     */
    public float unsafeSetAnisotropicFilter(float level) {
        return unsafeSetAnisotropicFilter(level, false);
    }

    /**
     * Sets the anisotropic filter level for the texture. Assumes the texture is bound and active!
     *
     * @param level The desired level of filtering. The maximum level supported by the device up to this value will be used.
     * @param force True to always set the value, even if it is the same as the current values.
     * @return The actual level set, which may be lower than the provided value due to device limitations.
     */
    public float unsafeSetAnisotropicFilter(float level, boolean force) {
        float max = getMaxAnisotropicFilterLevel();
        if (max == 1f) return 1f;
//        level = Math.min(level, max);
//        if (!force && MathUtils.isEqual(level, anisotropicFilterLevel, 0.1f)) return anisotropicFilterLevel;
//        ApplicationEnvironment.gl20.glTexParameterf(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAX_ANISOTROPY_EXT, level);
//        return anisotropicFilterLevel = level;
        // TODO
        return 1f;
    }

    /**
     * Sets the anisotropic filter level for the texture. This will bind the texture!
     *
     * @param level The desired level of filtering. The maximum level supported by the device up to this value will be used.
     * @return The actual level set, which may be lower than the provided value due to device limitations.
     */
    public float setAnisotropicFilter(float level) {
        float max = getMaxAnisotropicFilterLevel();
        if (max == 1f) {
            return 1f;
        }
        level = Math.min(level, max);
        if (MathUtils.isEqual(level, anisotropicFilterLevel, 0.1f)) return level;
        return anisotropicFilterLevel = level;
    }

    /**
     * @return The maximum supported anisotropic filtering level supported by the device.
     */
    public static float getMaxAnisotropicFilterLevel() {
        if (maxAnisotropicFilterLevel > 0) {
            return maxAnisotropicFilterLevel;
        }
        if (ApplicationEnvironment.graphics.supportsExtension("GL_EXT_texture_filter_anisotropic")) {
            FloatBuffer buffer = BufferUtils.newFloatBuffer(16);
            ((Buffer) buffer).position(0);
            ((Buffer) buffer).limit(buffer.capacity());
            // TODO
//            ApplicationEnvironment.gl20.glGetFloatv(GL20.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, buffer);
            return maxAnisotropicFilterLevel = buffer.get(0);
        }
        return maxAnisotropicFilterLevel = 1f;
    }
}
