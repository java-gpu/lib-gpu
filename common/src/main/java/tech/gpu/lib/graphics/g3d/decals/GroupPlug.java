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

package tech.gpu.lib.graphics.g3d.decals;

import tech.gpu.lib.utils.Array;

/**
 * Handles a single group's pre and post render arrangements. Can be plugged into {@link PluggableGroupStrategy} to build modular
 * {@link GroupStrategy GroupStrategies}.
 */
public interface GroupPlug {
    public void beforeGroup(Array<Decal> contents);

    public void afterGroup();
}
