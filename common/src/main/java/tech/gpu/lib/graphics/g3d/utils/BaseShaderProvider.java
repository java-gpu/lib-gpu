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

import tech.gpu.lib.graphics.g3d.Renderable;
import tech.gpu.lib.graphics.g3d.Shader;
import tech.gpu.lib.utils.Array;
import tech.gpu.lib.ex.GpuRuntimeException;

public abstract class BaseShaderProvider implements ShaderProvider {
    protected Array<Shader> shaders = new Array<Shader>();

    @Override
    public Shader getShader(Renderable renderable) {
        Shader suggestedShader = renderable.shader;
        if (suggestedShader != null && suggestedShader.canRender(renderable)) return suggestedShader;
        for (Shader shader : shaders) {
            if (shader.canRender(renderable)) return shader;
        }
        final Shader shader = createShader(renderable);
        if (!shader.canRender(renderable))
            throw new GpuRuntimeException("unable to provide a shader for this renderable");
        shader.init();
        shaders.add(shader);
        return shader;
    }

    protected abstract Shader createShader(final Renderable renderable);

    @Override
    public void dispose() {
        for (Shader shader : shaders) {
            shader.dispose();
        }
        shaders.clear();
    }
}
