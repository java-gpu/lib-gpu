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

package tech.gpu.lib.graphics.g3d.environment;

import tech.gpu.lib.graphics.Color;

public abstract class BaseLight<T extends BaseLight<T>> {
    public final Color color = new Color(0, 0, 0, 1);

    public T setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
        return (T) this;
    }

    public T setColor(Color color) {
        this.color.set(color);
        return (T) this;
    }
}
