package tech.gpu.lib.viewport;

import tech.gpu.lib.graphics.Camera;
import tech.gpu.lib.graphics.OrthographicCamera;
import tech.gpu.lib.utils.Scaling;

/**
 * A ScalingViewport that uses {@link Scaling#fill} so it keeps the aspect ratio by scaling the world up to take the whole screen
 * (some of the world may be off screen).
 *
 * @author Daniel Holderbaum
 * @author Nathan Sweet
 */
public class FillViewport extends ScalingViewport {
    /**
     * Creates a new viewport using a new {@link OrthographicCamera}.
     */
    public FillViewport(float worldWidth, float worldHeight) {
        super(Scaling.fill, worldWidth, worldHeight);
    }

    public FillViewport(float worldWidth, float worldHeight, Camera camera) {
        super(Scaling.fill, worldWidth, worldHeight, camera);
    }
}
