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

package tech.gpu.lib.assets;

import tech.gpu.lib.files.FileHandle;

/**
 * Describes an asset to be loaded by its filename, type and {@link AssetLoaderParameters}. Instances of this are used in
 * {@link AssetLoadingTask} to load the actual asset.
 *
 * @author mzechner
 */
public class AssetDescriptor<T> {
    public final String fileName;
    public final Class<T> type;
    public final AssetLoaderParameters params;
    /**
     * The resolved file. May be if the fileName has not been resolved yet.
     */
    public FileHandle file;

    public AssetDescriptor(String fileName, Class<T> assetType) {
        this(fileName, assetType, null);
    }

    /**
     * Creates an AssetDescriptor with an already resolved name.
     */
    public AssetDescriptor(FileHandle file, Class<T> assetType) {
        this(file, assetType, null);
    }

    public AssetDescriptor(String fileName, Class<T> assetType, AssetLoaderParameters<T> params) {
        this.fileName = fileName;
        this.type = assetType;
        this.params = params;
    }

    /**
     * Creates an AssetDescriptor with an already resolved name.
     */
    public AssetDescriptor(FileHandle file, Class<T> assetType, AssetLoaderParameters<T> params) {
        this.fileName = file.path();
        this.file = file;
        this.type = assetType;
        this.params = params;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(fileName);
        sb.append(", ");
        sb.append(type.getName());
        return sb.toString();
    }
}
