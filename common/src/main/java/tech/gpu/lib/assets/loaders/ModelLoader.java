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
import tech.gpu.lib.graphics.Texture;
import tech.gpu.lib.graphics.TextureFilter;
import tech.gpu.lib.graphics.TextureWrap;
import tech.gpu.lib.graphics.g3d.Model;
import tech.gpu.lib.graphics.g3d.model.data.ModelData;
import tech.gpu.lib.graphics.g3d.model.data.ModelMaterial;
import tech.gpu.lib.graphics.g3d.model.data.ModelTexture;
import tech.gpu.lib.graphics.g3d.utils.TextureProvider;
import tech.gpu.lib.utils.Array;
import tech.gpu.lib.utils.Disposable;
import tech.gpu.lib.utils.ObjectMap;

import java.util.Iterator;

public abstract class ModelLoader<P extends ModelLoader.ModelParameters> extends AsynchronousAssetLoader<Model, P> {
    public ModelLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    protected Array<ObjectMap.Entry<String, ModelData>> items = new Array<ObjectMap.Entry<String, ModelData>>();
    protected ModelParameters defaultParameters = new ModelParameters();

    /**
     * Directly load the raw model data on the calling thread.
     */
    public abstract ModelData loadModelData(final FileHandle fileHandle, P parameters);

    /**
     * Directly load the raw model data on the calling thread.
     */
    public ModelData loadModelData(final FileHandle fileHandle) {
        return loadModelData(fileHandle, null);
    }

    /**
     * Directly load the model on the calling thread. The model with not be managed by an {@link AssetManager}.
     */
    public Model loadModel(final FileHandle fileHandle, TextureProvider textureProvider, P parameters) {
        final ModelData data = loadModelData(fileHandle, parameters);
        return data == null ? null : new Model(data, textureProvider);
    }

    /**
     * Directly load the model on the calling thread. The model with not be managed by an {@link AssetManager}.
     */
    public Model loadModel(final FileHandle fileHandle, P parameters) {
        return loadModel(fileHandle, new TextureProvider.FileTextureProvider(), parameters);
    }

    /**
     * Directly load the model on the calling thread. The model with not be managed by an {@link AssetManager}.
     */
    public Model loadModel(final FileHandle fileHandle, TextureProvider textureProvider) {
        return loadModel(fileHandle, textureProvider, null);
    }

    /**
     * Directly load the model on the calling thread. The model with not be managed by an {@link AssetManager}.
     */
    public Model loadModel(final FileHandle fileHandle) {
        return loadModel(fileHandle, new TextureProvider.FileTextureProvider(), null);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, P parameters) {
        final Array<AssetDescriptor> deps = new Array();
        ModelData data = loadModelData(file, parameters);
        if (data == null) return deps;

        ObjectMap.Entry<String, ModelData> item = new ObjectMap.Entry<String, ModelData>();
        item.key = fileName;
        item.value = data;
        synchronized (items) {
            items.add(item);
        }

        TextureLoader.TextureParameter textureParameter = (parameters != null) ? parameters.textureParameter
                : defaultParameters.textureParameter;

        for (final ModelMaterial modelMaterial : data.materials) {
            if (modelMaterial.textures != null) {
                for (final ModelTexture modelTexture : modelMaterial.textures)
                    deps.add(new AssetDescriptor(modelTexture.fileName, Texture.class, textureParameter));
            }
        }
        return deps;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, P parameters) {
    }

    @Override
    public Model loadSync(AssetManager manager, String fileName, FileHandle file, P parameters) {
        ModelData data = null;
        synchronized (items) {
            for (int i = 0; i < items.size; i++) {
                if (items.get(i).key.equals(fileName)) {
                    data = items.get(i).value;
                    items.removeIndex(i);
                }
            }
        }
        if (data == null) return null;
        final Model result = new Model(data, new TextureProvider.AssetTextureProvider(manager));
        // need to remove the textures from the managed disposables, or else ref counting
        // doesn't work!
        Iterator<Disposable> disposables = result.getManagedDisposables().iterator();
        while (disposables.hasNext()) {
            Disposable disposable = disposables.next();
            if (disposable instanceof Texture) {
                disposables.remove();
            }
        }
        return result;
    }

    static public class ModelParameters extends AssetLoaderParameters<Model> {
        public TextureLoader.TextureParameter textureParameter;

        public ModelParameters() {
            textureParameter = new TextureLoader.TextureParameter();
            textureParameter.minFilter = textureParameter.magFilter = TextureFilter.Linear;
            textureParameter.wrapU = textureParameter.wrapV = TextureWrap.Repeat;
        }
    }
}
