package tech.gpu.lib.viewport;

import tech.gpu.lib.graphics.Camera;
import tech.gpu.lib.graphics.OrthographicCamera;
import tech.gpu.lib.utils.Scaling;

/**
 * A ScalingViewport that uses {@link Scaling#fit} so it keeps the aspect ratio by scaling the world up to fit the screen, adding
 * black bars (letterboxing) for the remaining space.
 *
 * @author Daniel Holderbaum
 * @author Nathan Sweet
 */
public class FitViewport extends ScalingViewport {
    /**
     * Creates a new viewport using a new {@link OrthographicCamera}.
     */
    public FitViewport(float worldWidth, float worldHeight) {
        super(Scaling.fit, worldWidth, worldHeight);
    }

    public FitViewport(float worldWidth, float worldHeight, Camera camera) {
        super(Scaling.fit, worldWidth, worldHeight, camera);
    }
}
