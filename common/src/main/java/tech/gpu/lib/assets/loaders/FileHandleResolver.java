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

import tech.gpu.lib.assets.AssetManager;
import tech.gpu.lib.files.FileHandle;

/**
 * Interface for classes the can map a file name to a {@link FileHandle}. Used to allow the {@link AssetManager} to load
 * resources from anywhere or implement caching strategies.
 *
 * @author mzechner
 */
public interface FileHandleResolver {
    public FileHandle resolve(String fileName);
}
