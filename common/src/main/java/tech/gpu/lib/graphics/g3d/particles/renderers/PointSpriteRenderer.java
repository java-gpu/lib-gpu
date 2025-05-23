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

package tech.gpu.lib.graphics.g3d.particles.renderers;

import tech.gpu.lib.graphics.g3d.particles.ParticleChannels;
import tech.gpu.lib.graphics.g3d.particles.ParticleChannels.ColorInitializer;
import tech.gpu.lib.graphics.g3d.particles.ParticleChannels.Rotation2dInitializer;
import tech.gpu.lib.graphics.g3d.particles.ParticleChannels.ScaleInitializer;
import tech.gpu.lib.graphics.g3d.particles.ParticleChannels.TextureRegionInitializer;
import tech.gpu.lib.graphics.g3d.particles.ParticleControllerComponent;
import tech.gpu.lib.graphics.g3d.particles.batches.ParticleBatch;
import tech.gpu.lib.graphics.g3d.particles.batches.PointSpriteParticleBatch;

/**
 * A {@link ParticleControllerRenderer} which will render particles as point sprites to a {@link PointSpriteParticleBatch} .
 *
 * @author Inferno
 */
public class PointSpriteRenderer extends ParticleControllerRenderer<PointSpriteControllerRenderData, PointSpriteParticleBatch> {
    public PointSpriteRenderer() {
        super(new PointSpriteControllerRenderData());
    }

    public PointSpriteRenderer(PointSpriteParticleBatch batch) {
        this();
        setBatch(batch);
    }

    @Override
    public void allocateChannels() {
        renderData.positionChannel = controller.particles.addChannel(ParticleChannels.Position);
        renderData.regionChannel = controller.particles.addChannel(ParticleChannels.TextureRegion, TextureRegionInitializer.get());
        renderData.colorChannel = controller.particles.addChannel(ParticleChannels.Color, ColorInitializer.get());
        renderData.scaleChannel = controller.particles.addChannel(ParticleChannels.Scale, ScaleInitializer.get());
        renderData.rotationChannel = controller.particles.addChannel(ParticleChannels.Rotation2D, Rotation2dInitializer.get());
    }

    @Override
    public boolean isCompatible(ParticleBatch<?> batch) {
        return batch instanceof PointSpriteParticleBatch;
    }

    @Override
    public ParticleControllerComponent copy() {
        return new PointSpriteRenderer(batch);
    }

}
