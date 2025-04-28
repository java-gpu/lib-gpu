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

import tech.gpu.lib.assets.AssetDescriptor;
import tech.gpu.lib.assets.AssetLoaderParameters;
import tech.gpu.lib.assets.AssetManager;
import tech.gpu.lib.files.FileHandle;
import tech.gpu.lib.graphics.*;
import tech.gpu.lib.utils.Array;

/**
 * {@link AssetLoader} for {@link Cubemap} instances. The pixel data is loaded asynchronously. The texture is then created on the
 * rendering thread, synchronously. Passing a {@link CubemapParameter} to
 * {@link AssetManager#load(String, Class, AssetLoaderParameters)} allows one to specify parameters as can be passed to the
 * various Cubemap constructors, e.g. filtering and so on.
 *
 * @author mzechner, Vincent Bousquet
 */
public class CubemapLoader extends AsynchronousAssetLoader<Cubemap, CubemapLoader.CubemapParameter> {
    static public class CubemapLoaderInfo {
        String filename;
        //        CubemapData data;
        Cubemap cubemap;
    }

    ;

    CubemapLoaderInfo info = new CubemapLoaderInfo();

    public CubemapLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, CubemapParameter parameter) {
        info.filename = fileName;
        // TODO
//        if (parameter == null || parameter.cubemapData == null) {
//            Format format = null;
//            boolean genMipMaps = false;
//            info.cubemap = null;
//
//            if (parameter != null) {
//                format = parameter.format;
//                info.cubemap = parameter.cubemap;
//            }
//
//            if (fileName.contains(".ktx") || fileName.contains(".zktx")) {
//                info.data = new KTXTextureData(file, genMipMaps);
//            }
//        } else {
//            info.data = parameter.cubemapData;
//            info.cubemap = parameter.cubemap;
//        }
//        if (!info.data.isPrepared()) info.data.prepare();
    }

    @Override
    public Cubemap loadSync(AssetManager manager, String fileName, FileHandle file, CubemapParameter parameter) {
//        if (info == null) return null;
//        Cubemap cubemap = info.cubemap;
//        if (cubemap != null) {
//            cubemap.load(info.data);
//        } else {
//            cubemap = new Cubemap(info.data);
//        }
//        if (parameter != null) {
//            cubemap.setFilter(parameter.minFilter, parameter.magFilter);
//            cubemap.setWrap(parameter.wrapU, parameter.wrapV);
//        }
//        return cubemap;
        // TODO
        return null;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CubemapParameter parameter) {
        return null;
    }

    static public class CubemapParameter extends AssetLoaderParameters<Cubemap> {
        /**
         * the format of the final Texture. Uses the source images format if null
         **/
        public PixelFormat format = null;
        /**
         * The texture to put the {@link TextureData} in, optional.
         **/
        public Cubemap cubemap = null;
        /**
         * CubemapData for textures created on the fly, optional. When set, all format and genMipMaps are ignored
         */
        // TODO
//        public CubemapData cubemapData = null;
        public TextureFilter minFilter = TextureFilter.Nearest;
        public TextureFilter magFilter = TextureFilter.Nearest;
        public TextureWrap wrapU = TextureWrap.ClampToEdge;
        public TextureWrap wrapV = TextureWrap.ClampToEdge;
    }
}
