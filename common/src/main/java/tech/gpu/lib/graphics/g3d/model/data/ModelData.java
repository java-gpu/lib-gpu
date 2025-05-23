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

package tech.gpu.lib.graphics.g3d.model.data;

import tech.gpu.lib.assets.loaders.ModelLoader;
import tech.gpu.lib.utils.Array;
import tech.gpu.lib.ex.GpuRuntimeException;

/**
 * Returned by a {@link ModelLoader}, contains meshes, materials, nodes and animations. OpenGL resources like textures or vertex
 * buffer objects are not stored. Instead, a ModelData instance needs to be converted to a Model first.
 *
 * @author badlogic
 */
public class ModelData {
    public String id;
    public final short version[] = new short[2];
    public final Array<ModelMesh> meshes = new Array<ModelMesh>();
    public final Array<ModelMaterial> materials = new Array<ModelMaterial>();
    public final Array<ModelNode> nodes = new Array<ModelNode>();
    public final Array<ModelAnimation> animations = new Array<ModelAnimation>();

    public void addMesh(ModelMesh mesh) {
        for (ModelMesh other : meshes) {
            if (other.id.equals(mesh.id)) {
                throw new GpuRuntimeException("Mesh with id '" + other.id + "' already in model");
            }
        }
        meshes.add(mesh);
    }
}
