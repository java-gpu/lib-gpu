/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <br/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <br/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <br/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package tech.gpu.lib;

import tech.gpu.lib.utils.Disposable;

/**
 * <p>
 * Represents one of many application screens, such as a main menu, a settings menu, the game screen and so on.
 * </p>
 * <p>
 * Note that {@link #dispose()} is not called automatically.
 * </p>
 *
 * @see Game
 */
public interface Screen extends Disposable {

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    void show();

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    void render(float delta);

    /**
     * @see ApplicationListener#resize(int, int)
     */
    void resize(int width, int height);

    /**
     * @see ApplicationListener#pause()
     */
    void pause();

    /**
     * @see ApplicationListener#resume()
     */
    void resume();

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    void hide();

    /**
     * Called when this screen should release all resources.
     */
    void dispose();
}
