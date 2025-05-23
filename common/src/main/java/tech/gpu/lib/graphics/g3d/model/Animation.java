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

package tech.gpu.lib.graphics.g3d.model;

import tech.gpu.lib.graphics.g3d.Model;
import tech.gpu.lib.utils.Array;

/**
 * An Animation has an id and a list of {@link NodeAnimation} instances. Each NodeAnimation animates a single {@link Node} in the
 * {@link Model}. Every {@link NodeAnimation} is assumed to have the same amount of keyframes, at the same timestamps, as all
 * other node animations for faster keyframe searches.
 *
 * @author badlogic
 */
public class Animation {
    /**
     * the unique id of the animation
     **/
    public String id;
    /**
     * the duration in seconds
     **/
    public float duration;
    /**
     * the animation curves for individual nodes
     **/
    public Array<NodeAnimation> nodeAnimations = new Array<NodeAnimation>();
}
