/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package tech.gpu.lib.assets.loaders;

import tech.gpu.lib.ApplicationEnvironment;
import tech.gpu.lib.assets.AssetDescriptor;
import tech.gpu.lib.assets.AssetLoaderParameters;
import tech.gpu.lib.assets.AssetManager;
import tech.gpu.lib.files.FileHandle;
import tech.gpu.lib.graphics.*;
import tech.gpu.lib.jni.GpuManager;
import tech.gpu.lib.jni.TextureNative;
import tech.gpu.lib.utils.Array;

/**
 * {@link AssetLoader} for {@link Texture} instances. The pixel data is loaded asynchronously. The texture is then created on the
 * rendering thread, synchronously. Passing a {@link TextureParameter} to
 * {@link AssetManager#load(String, Class, AssetLoaderParameters)} allows one to specify parameters as can be passed to the
 * various Texture constructors, e.g. filtering, whether to generate mipmaps and so on.
 *
 * @author mzechner
 */
public class TextureLoader extends AsynchronousAssetLoader<Texture, TextureLoader.TextureParameter> {
    static public class TextureLoaderInfo {
        String filename;
        TextureInfo data;
        Texture texture;
    }

    ;

    TextureLoaderInfo info = new TextureLoaderInfo();

    public TextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureParameter parameter) {
        info.filename = fileName;
        if (parameter == null || parameter.textureInfo == null) {
            PixelFormat format = null;
            boolean genMipMaps = false;
            info.texture = null;

            if (parameter != null) {
                format = parameter.format;
                genMipMaps = parameter.genMipMaps;
                info.texture = parameter.texture;
            }
            var app = ApplicationEnvironment.app;
            info.data = TextureNative.loadTexture(GpuManager.gpuSelected.getPointer(), file.file().getAbsolutePath(), genMipMaps,
                    app.getGraphics().getPixelFormatConverter().toNativeFormat(format));
        } else {
            info.data = parameter.textureInfo;
            info.texture = parameter.texture;
        }
    }

    @Override
    public Texture loadSync(AssetManager manager, String fileName, FileHandle file, TextureParameter parameter) {
        if (info == null) return null;
        Texture texture = info.texture;
        if (texture != null) {
            texture.readFromTextureInfo(info.data);
        } else {
            texture = new Texture(ApplicationEnvironment.app, info.data);
        }
        if (parameter != null) {
            texture.setFilter(parameter.minFilter, parameter.magFilter);
            texture.setWrap(parameter.wrapU, parameter.wrapV);
        }
        return texture;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureParameter parameter) {
        return null;
    }

    static public class TextureParameter extends AssetLoaderParameters<Texture> {
        /**
         * the format of the final Texture. Uses the source images format if null
         **/
        public PixelFormat format = null;
        /**
         * whether to generate mipmaps
         **/
        public boolean genMipMaps = false;
        /**
         * The texture to put the {@link TextureData} in, optional.
         **/
        public Texture texture = null;
        /**
         * TextureData for textures created on the fly, optional. When set, all format and genMipMaps are ignored
         */
        public TextureInfo textureInfo = null;
        public TextureFilter minFilter = TextureFilter.Nearest;
        public TextureFilter magFilter = TextureFilter.Nearest;
        public TextureWrap wrapU = TextureWrap.ClampToEdge;
        public TextureWrap wrapV = TextureWrap.ClampToEdge;
    }
}
