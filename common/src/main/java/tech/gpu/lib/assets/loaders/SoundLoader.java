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
import tech.gpu.lib.audio.Sound;
import tech.gpu.lib.files.FileHandle;
import tech.gpu.lib.utils.Array;

/**
 * {@link AssetLoader} to load {@link Sound} instances.
 *
 * @author mzechner
 */
public class SoundLoader extends AsynchronousAssetLoader<Sound, SoundLoader.SoundParameter> {

    private Sound sound;

    public SoundLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    /**
     * Returns the {@link Sound} instance currently loaded by this {@link SoundLoader}.
     *
     * @return the currently loaded {@link Sound}, otherwise {@code null} if no {@link Sound} has been loaded yet.
     */
    protected Sound getLoadedSound() {
        return sound;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SoundParameter parameter) {
        sound = ApplicationEnvironment.audio.newSound(file.file());
    }

    @Override
    public Sound loadSync(AssetManager manager, String fileName, FileHandle file, SoundParameter parameter) {
        Sound sound = this.sound;
        this.sound = null;
        return sound;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SoundParameter parameter) {
        return null;
    }

    static public class SoundParameter extends AssetLoaderParameters<Sound> {
    }

}
