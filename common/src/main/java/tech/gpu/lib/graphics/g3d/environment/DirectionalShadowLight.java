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


import tech.gpu.lib.graphics.Camera;
import tech.gpu.lib.graphics.OrthographicCamera;
import tech.gpu.lib.graphics.TextureFilter;
import tech.gpu.lib.graphics.TextureWrap;
import tech.gpu.lib.graphics.g3d.utils.TextureDescriptor;
import tech.gpu.lib.math.Matrix4;
import tech.gpu.lib.math.Vector3;
import tech.gpu.lib.utils.Disposable;

/**
 * @author Xoppa
 */
public class DirectionalShadowLight extends DirectionalLight implements ShadowMap, Disposable {
    // TODO
//    protected FrameBuffer fbo;
    protected Camera cam;
    protected float halfDepth;
    protected float halfHeight;
    protected final Vector3 tmpV = new Vector3();
    protected final TextureDescriptor textureDesc;

    public DirectionalShadowLight(int shadowMapWidth, int shadowMapHeight, float shadowViewportWidth, float shadowViewportHeight,
                                  float shadowNear, float shadowFar) {
        // TODO
//        fbo = new FrameBuffer(Format.RGBA8888, shadowMapWidth, shadowMapHeight, true);
        cam = new OrthographicCamera(shadowViewportWidth, shadowViewportHeight);
        cam.near = shadowNear;
        cam.far = shadowFar;
        halfHeight = shadowViewportHeight * 0.5f;
        halfDepth = shadowNear + 0.5f * (shadowFar - shadowNear);
        textureDesc = new TextureDescriptor();
        textureDesc.minFilter = textureDesc.magFilter = TextureFilter.Nearest;
        textureDesc.uWrap = textureDesc.vWrap = TextureWrap.ClampToEdge;
    }

    public void update(final Camera camera) {
        update(tmpV.set(camera.direction).scl(halfHeight), camera.direction);
    }

    public void update(final Vector3 center, final Vector3 forward) {
        cam.position.set(direction).scl(-halfDepth).add(center);
        cam.direction.set(direction).nor();
        cam.normalizeUp();
        cam.update();
    }

    public void begin(final Camera camera) {
        update(camera);
        begin();
    }

    public void begin(final Vector3 center, final Vector3 forward) {
        update(center, forward);
        begin();
    }

    public void begin() {
        // TODO
//        final int w = fbo.getWidth();
//        final int h = fbo.getHeight();
//        fbo.begin();
//        ApplicationEnvironment.gl.glViewport(0, 0, w, h);
//        ApplicationEnvironment.gl.glClearColor(1, 1, 1, 1);
//        ApplicationEnvironment.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//        ApplicationEnvironment.gl.glEnable(GL20.GL_SCISSOR_TEST);
//        ApplicationEnvironment.gl.glScissor(1, 1, w - 2, h - 2);
    }

    public void end() {
        // TODO
//        ApplicationEnvironment.gl.glDisable(GL20.GL_SCISSOR_TEST);
//        fbo.end();
    }

    // TODO
//    public FrameBuffer getFrameBuffer() {
//        return fbo;
//    }

    public Camera getCamera() {
        return cam;
    }

    @Override
    public Matrix4 getProjViewTrans() {
        return cam.combined;
    }

    @Override
    public TextureDescriptor getDepthMap() {
        // TODO
//        textureDesc.texture = fbo.getColorBufferTexture();
        return textureDesc;
    }

    @Override
    public void dispose() {
        // TODO
//        if (fbo != null) fbo.dispose();
//        fbo = null;
    }
}
