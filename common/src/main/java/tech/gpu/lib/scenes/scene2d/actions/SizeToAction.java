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

package tech.gpu.lib.scenes.scene2d.actions;

/**
 * Moves an actor from its current size to a specific size.
 *
 * @author Nathan Sweet
 */
public class SizeToAction extends TemporalAction {
    private float startWidth, startHeight;
    private float endWidth, endHeight;

    protected void begin() {
        startWidth = target.getWidth();
        startHeight = target.getHeight();
    }

    protected void update(float percent) {
        float width, height;
        if (percent == 0) {
            width = startWidth;
            height = startHeight;
        } else if (percent == 1) {
            width = endWidth;
            height = endHeight;
        } else {
            width = startWidth + (endWidth - startWidth) * percent;
            height = startHeight + (endHeight - startHeight) * percent;
        }
        target.setSize(width, height);
    }

    public void setSize(float width, float height) {
        endWidth = width;
        endHeight = height;
    }

    public float getWidth() {
        return endWidth;
    }

    public void setWidth(float width) {
        endWidth = width;
    }

    public float getHeight() {
        return endHeight;
    }

    public void setHeight(float height) {
        endHeight = height;
    }
}
