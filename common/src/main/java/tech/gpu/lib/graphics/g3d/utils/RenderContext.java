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

/**
 * Manages OpenGL state and tries to reduce state changes. Uses a {@link TextureBinder} to reduce texture binds as well. Call
 * {@link #begin()} to setup the context, call {@link #end()} to undo all state changes. Use the setters to change state, use
 * {@link #textureBinder} to bind textures.
 *
 * @author badlogic, Xoppa
 */
public class RenderContext {
    /**
     * used to bind textures
     **/
    public final TextureBinder textureBinder;
    private boolean blending;
    private int blendSFactor;
    private int blendDFactor;
    private int depthFunc;
    private float depthRangeNear;
    private float depthRangeFar;
    private boolean depthMask;
    private int cullFace;

    public RenderContext(TextureBinder textures) {
        this.textureBinder = textures;
    }

    /**
     * Sets up the render context, must be matched with a call to {@link #end()}.
     */
    public void begin() {
        // TODO
//        ApplicationEnvironment.gl.glDisable(GL20.GL_DEPTH_TEST);
        depthFunc = 0;
//        ApplicationEnvironment.gl.glDepthMask(true);
        depthMask = true;
//        ApplicationEnvironment.gl.glDisable(GL20.GL_BLEND);
        blending = false;
//        ApplicationEnvironment.gl.glDisable(GL20.GL_CULL_FACE);
        cullFace = blendSFactor = blendDFactor = 0;
        textureBinder.begin();
    }

    /**
     * Resets all changed OpenGL states to their defaults.
     */
    public void end() {
        // TODO
//        if (depthFunc != 0) ApplicationEnvironment.gl.glDisable(GL20.GL_DEPTH_TEST);
//        if (!depthMask) ApplicationEnvironment.gl.glDepthMask(true);
//        if (blending) ApplicationEnvironment.gl.glDisable(GL20.GL_BLEND);
//        if (cullFace > 0) ApplicationEnvironment.gl.glDisable(GL20.GL_CULL_FACE);
        textureBinder.end();
    }

    public void setDepthMask(final boolean depthMask) {
        // TODO
//        if (this.depthMask != depthMask) ApplicationEnvironment.gl.glDepthMask(this.depthMask = depthMask);
    }

    public void setDepthTest(final int depthFunction) {
        setDepthTest(depthFunction, 0f, 1f);
    }

    public void setDepthTest(final int depthFunction, final float depthRangeNear, final float depthRangeFar) {
        final boolean wasEnabled = depthFunc != 0;
        final boolean enabled = depthFunction != 0;
        if (depthFunc != depthFunction) {
            depthFunc = depthFunction;
//            if (enabled) {
//                ApplicationEnvironment.gl.glEnable(GL20.GL_DEPTH_TEST);
//                ApplicationEnvironment.gl.glDepthFunc(depthFunction);
//            } else
//                ApplicationEnvironment.gl.glDisable(GL20.GL_DEPTH_TEST);
            // TODO
        }
//        if (enabled) {
        // TODO
//            if (!wasEnabled || depthFunc != depthFunction) ApplicationEnvironment.gl.glDepthFunc(depthFunc = depthFunction);
//            if (!wasEnabled || this.depthRangeNear != depthRangeNear || this.depthRangeFar != depthRangeFar)
//                ApplicationEnvironment.gl.glDepthRangef(this.depthRangeNear = depthRangeNear, this.depthRangeFar = depthRangeFar);
//        }
    }

    public void setBlending(final boolean enabled, final int sFactor, final int dFactor) {
//        if (enabled != blending) {
//            blending = enabled;
//            if (enabled)
//                ApplicationEnvironment.gl.glEnable(GL20.GL_BLEND);
//            else
//                ApplicationEnvironment.gl.glDisable(GL20.GL_BLEND);
//        }
//        if (enabled && (blendSFactor != sFactor || blendDFactor != dFactor)) {
//            ApplicationEnvironment.gl.glBlendFunc(sFactor, dFactor);
//            blendSFactor = sFactor;
//            blendDFactor = dFactor;
//        }
        // TODO
    }

    public void setCullFace(final int face) {
//        if (face != cullFace) {
//            cullFace = face;
//            if ((face == GL20.GL_FRONT) || (face == GL20.GL_BACK) || (face == GL20.GL_FRONT_AND_BACK)) {
//                ApplicationEnvironment.gl.glEnable(GL20.GL_CULL_FACE);
//                ApplicationEnvironment.gl.glCullFace(face);
//            } else
//                ApplicationEnvironment.gl.glDisable(GL20.GL_CULL_FACE);
//        }
        // TODO
    }
}
