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

import tech.gpu.lib.graphics.Color;
import tech.gpu.lib.scenes.scene2d.Actor;


/**
 * Sets the alpha for an actor's color (or a specified color), from the current alpha to the new alpha. Note this action
 * transitions from the alpha at the time the action starts to the specified alpha.
 *
 * @author Nathan Sweet
 */
public class AlphaAction extends TemporalAction {
    private float start, end;
    private Color color;

    protected void begin() {
        if (color == null) color = target.getColor();
        start = color.a;
    }

    protected void update(float percent) {
        if (percent == 0)
            color.a = start;
        else if (percent == 1)
            color.a = end;
        else
            color.a = start + (end - start) * percent;
    }

    public void reset() {
        super.reset();
        color = null;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Sets the color to modify. If (the default), the {@link #getActor() actor's} {@link Actor#getColor() color} will be
     * used.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public float getAlpha() {
        return end;
    }

    public void setAlpha(float alpha) {
        this.end = alpha;
    }
}
