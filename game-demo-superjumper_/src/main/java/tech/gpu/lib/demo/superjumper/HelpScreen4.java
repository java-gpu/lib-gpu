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

package tech.gpu.lib.demo.superjumper;


import tech.gpu.lib.ScreenAdapter;

import tech.gpu.lib.graphics.OrthographicCamera;
import tech.gpu.lib.graphics.Texture;
import tech.gpu.lib.graphics.g2d.TextureRegion;
import tech.gpu.lib.math.Rectangle;
import tech.gpu.lib.math.Vector3;

public class HelpScreen4 extends ScreenAdapter {
    SuperJumper game;

    OrthographicCamera guiCam;
    Rectangle nextBounds;
    Vector3 touchPoint;
    Texture helpImage;
    TextureRegion helpRegion;

    public HelpScreen4(SuperJumper game) {
        this.game = game;

        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        nextBounds = new Rectangle(320 - 64, 0, 64, 64);
        touchPoint = new Vector3();
        helpImage = Assets.loadTexture("data/help4.png");
        helpRegion = new TextureRegion(helpImage, 0, 0, 320, 480);
    }

    public void update() {
        if (ApplicationEnvironment.input.justTouched()) {
            guiCam.unproject(touchPoint.set(ApplicationEnvironment.input.getX(), ApplicationEnvironment.input.getY(), 0));

            if (nextBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new HelpScreen5(game));
            }
        }
    }

    public void draw() {
        GL20 gl = ApplicationEnvironment.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();

        game.batcher.setProjectionMatrix(guiCam.combined);
        game.batcher.disableBlending();
        game.batcher.begin();
        game.batcher.draw(helpRegion, 0, 0, 320, 480);
        game.batcher.end();

        game.batcher.enableBlending();
        game.batcher.begin();
        game.batcher.draw(Assets.arrow, 320, 0, -64, 64);
        game.batcher.end();

        gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void render(float delta) {
        draw();
        update();
    }

    @Override
    public void hide() {
        helpImage.dispose();
    }
}
