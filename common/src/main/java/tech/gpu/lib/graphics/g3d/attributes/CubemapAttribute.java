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

package tech.gpu.lib.graphics.g3d.attributes;

import tech.gpu.lib.graphics.Cubemap;
import tech.gpu.lib.graphics.g3d.Attribute;
import tech.gpu.lib.graphics.g3d.utils.TextureDescriptor;
import tech.gpu.lib.ex.GpuRuntimeException;

public class CubemapAttribute extends Attribute {
    public final static String EnvironmentMapAlias = "environmentCubemap";
    public final static long EnvironmentMap = register(EnvironmentMapAlias);

    protected static long Mask = EnvironmentMap;

    public final static boolean is(final long mask) {
        return (mask & Mask) != 0;
    }

    public final TextureDescriptor<Cubemap> textureDescription;

    public CubemapAttribute(final long type) {
        super(type);
        if (!is(type)) throw new GpuRuntimeException("Invalid type specified");
        textureDescription = new TextureDescriptor<Cubemap>();
    }

    public <T extends Cubemap> CubemapAttribute(final long type, final TextureDescriptor<T> textureDescription) {
        this(type);
        this.textureDescription.set(textureDescription);
    }

    public CubemapAttribute(final long type, final Cubemap texture) {
        this(type);
        textureDescription.texture = texture;
    }

    public CubemapAttribute(final CubemapAttribute copyFrom) {
        this(copyFrom.type, copyFrom.textureDescription);
    }

    @Override
    public Attribute copy() {
        return new CubemapAttribute(this);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 967 * result + textureDescription.hashCode();
        return result;
    }

    @Override
    public int compareTo(Attribute o) {
        if (type != o.type) return (int) (type - o.type);
        return textureDescription.compareTo(((CubemapAttribute) o).textureDescription);
    }
}
