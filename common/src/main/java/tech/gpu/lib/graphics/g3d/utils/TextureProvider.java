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

package tech.gpu.lib.graphics.g3d.utils;


import tech.gpu.lib.ApplicationEnvironment;
import tech.gpu.lib.assets.AssetManager;
import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.graphics.Texture;
import tech.gpu.lib.graphics.TextureFilter;
import tech.gpu.lib.graphics.TextureWrap;
import tech.gpu.lib.graphics.g3d.Model;
import tech.gpu.lib.graphics.g3d.model.data.ModelData;

/**
 * Used by {@link Model} to load textures from {@link ModelData}.
 *
 * @author badlogic
 */
public interface TextureProvider {
    public Texture load(String fileName) throws PixelFormatNotSupportedException;

    public static class FileTextureProvider implements TextureProvider {
        private TextureFilter minFilter, magFilter;
        private TextureWrap uWrap, vWrap;
        private boolean useMipMaps;

        public FileTextureProvider() {
            minFilter = magFilter = TextureFilter.Linear;
            uWrap = vWrap = TextureWrap.Repeat;
            useMipMaps = false;
        }

        public FileTextureProvider(TextureFilter minFilter, TextureFilter magFilter, TextureWrap uWrap,
                                   TextureWrap vWrap, boolean useMipMaps) {
            this.minFilter = minFilter;
            this.magFilter = magFilter;
            this.uWrap = uWrap;
            this.vWrap = vWrap;
            this.useMipMaps = useMipMaps;
        }

        @Override
        public Texture load(String fileName) throws PixelFormatNotSupportedException {
            Texture result = new Texture(ApplicationEnvironment.app, ApplicationEnvironment.files.internal(fileName).file(), useMipMaps);
            result.setFilter(minFilter, magFilter);
            result.setWrap(uWrap, vWrap);
            return result;
        }
    }

    public static class AssetTextureProvider implements TextureProvider {
        public final AssetManager assetManager;

        public AssetTextureProvider(final AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        @Override
        public Texture load(String fileName) {
            return assetManager.get(fileName, Texture.class);
        }
    }
}
