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

package tech.gpu.lib.graphics;

import tech.gpu.lib.ApplicationEnvironment;
import tech.gpu.lib.Graphics;
import tech.gpu.lib.Input;
import tech.gpu.lib.graphics.g2d.Batch;
import tech.gpu.lib.math.Frustum;
import tech.gpu.lib.math.Matrix4;
import tech.gpu.lib.math.Quaternion;
import tech.gpu.lib.math.Vector3;
import tech.gpu.lib.math.collision.Ray;

/**
 * Base class for {@link OrthographicCamera} and {@link PerspectiveCamera}.
 *
 * @author mzechner
 */
public abstract class Camera {
    /**
     * the position of the camera
     **/
    public final Vector3 position = new Vector3();
    /**
     * the unit length direction vector of the camera
     **/
    public final Vector3 direction = new Vector3(0, 0, -1);
    /**
     * the unit length up vector of the camera
     **/
    public final Vector3 up = new Vector3(0, 1, 0);

    /**
     * the projection matrix
     **/
    public final Matrix4 projection = new Matrix4();
    /**
     * the view matrix
     **/
    public final Matrix4 view = new Matrix4();
    /**
     * the combined projection and view matrix
     **/
    public final Matrix4 combined = new Matrix4();
    /**
     * the inverse combined projection and view matrix
     **/
    public final Matrix4 invProjectionView = new Matrix4();

    /**
     * the near clipping plane distance, has to be positive
     **/
    public float near = 1;
    /**
     * the far clipping plane distance, has to be positive
     **/
    public float far = 100;

    /**
     * the viewport width
     **/
    public float viewportWidth = 0;
    /**
     * the viewport height
     **/
    public float viewportHeight = 0;

    /**
     * the frustum
     **/
    public final Frustum frustum = new Frustum();

    private final Vector3 tmpVec = new Vector3();
    private final Ray ray = new Ray(new Vector3(), new Vector3());

    /**
     * Recalculates the projection and view matrix of this camera and the {@link Frustum} planes. Use this after you've
     * manipulated any of the attributes of the camera.
     */
    public abstract void update();

    /**
     * Recalculates the projection and view matrix of this camera and the {@link Frustum} planes if <code>updateFrustum</code> is
     * true. Use this after you've manipulated any of the attributes of the camera.
     */
    public abstract void update(boolean updateFrustum);

    /**
     * Recalculates the direction of the camera to look at the point (x, y, z). This function assumes the up vector is normalized.
     *
     * @param x the x-coordinate of the point to look at
     * @param y the y-coordinate of the point to look at
     * @param z the z-coordinate of the point to look at
     */
    public void lookAt(float x, float y, float z) {
        tmpVec.set(x, y, z).sub(position).nor();
        if (!tmpVec.isZero()) {
            float dot = tmpVec.dot(up); // up and direction must ALWAYS be orthonormal vectors
            if (Math.abs(dot - 1) < 0.000000001f) {
                // Collinear
                up.set(direction).scl(-1);
            } else if (Math.abs(dot + 1) < 0.000000001f) {
                // Collinear opposite
                up.set(direction);
            }
            direction.set(tmpVec);
            normalizeUp();
        }
    }

    /**
     * Recalculates the direction of the camera to look at the point (x, y, z).
     *
     * @param target the point to look at
     */
    public void lookAt(Vector3 target) {
        lookAt(target.x, target.y, target.z);
    }

    /**
     * Normalizes the up vector by first calculating the right vector via a cross product between direction and up, and then
     * recalculating the up vector via a cross product between right and direction.
     */
    public void normalizeUp() {
        tmpVec.set(direction).crs(up);
        up.set(tmpVec).crs(direction).nor();
    }

    /**
     * Rotates the direction and up vector of this camera by the given angle around the given axis. The direction and up vector
     * will not be orthogonalized.
     *
     * @param angle the angle
     * @param axisX the x-component of the axis
     * @param axisY the y-component of the axis
     * @param axisZ the z-component of the axis
     */
    public void rotate(float angle, float axisX, float axisY, float axisZ) {
        direction.rotate(angle, axisX, axisY, axisZ);
        up.rotate(angle, axisX, axisY, axisZ);
    }

    /**
     * Rotates the direction and up vector of this camera by the given angle around the given axis. The direction and up vector
     * will not be orthogonalized.
     *
     * @param axis  the axis to rotate around
     * @param angle the angle, in degrees
     */
    public void rotate(Vector3 axis, float angle) {
        direction.rotate(axis, angle);
        up.rotate(axis, angle);
    }

    /**
     * Rotates the direction and up vector of this camera by the given rotation matrix. The direction and up vector will not be
     * orthogonalized.
     *
     * @param transform The rotation matrix
     */
    public void rotate(final Matrix4 transform) {
        direction.rot(transform);
        up.rot(transform);
    }

    /**
     * Rotates the direction and up vector of this camera by the given {@link Quaternion}. The direction and up vector will not be
     * orthogonalized.
     *
     * @param quat The quaternion
     */
    public void rotate(final Quaternion quat) {
        quat.transform(direction);
        quat.transform(up);
    }

    /**
     * Rotates the direction and up vector of this camera by the given angle around the given axis, with the axis attached to
     * given point. The direction and up vector will not be orthogonalized.
     *
     * @param point the point to attach the axis to
     * @param axis  the axis to rotate around
     * @param angle the angle, in degrees
     */
    public void rotateAround(Vector3 point, Vector3 axis, float angle) {
        tmpVec.set(point);
        tmpVec.sub(position);
        translate(tmpVec);
        rotate(axis, angle);
        tmpVec.rotate(axis, angle);
        translate(-tmpVec.x, -tmpVec.y, -tmpVec.z);
    }

    /**
     * Transform the position, direction and up vector by the given matrix
     *
     * @param transform The transform matrix
     */
    public void transform(final Matrix4 transform) {
        position.mul(transform);
        rotate(transform);
    }

    /**
     * Moves the camera by the given amount on each axis.
     *
     * @param x the displacement on the x-axis
     * @param y the displacement on the y-axis
     * @param z the displacement on the z-axis
     */
    public void translate(float x, float y, float z) {
        position.add(x, y, z);
    }

    /**
     * Moves the camera by the given vector.
     *
     * @param vec the displacement vector
     */
    public void translate(Vector3 vec) {
        position.add(vec);
    }

    /**
     * Function to translate a point given in touch coordinates to world space. It's the same as GLU gluUnProject, but does not
     * rely on OpenGL. The x- and y-coordinate of vec are assumed to be in touch coordinates (origin is the top left corner, y
     * pointing down, x pointing to the right) as reported by the touch methods in {@link Input}. A z-coordinate of 0 will return a
     * point on the near plane, a z-coordinate of 1 will return a point on the far plane. This method allows you to specify the
     * viewport position and dimensions in the coordinate system, with the
     * origin in the bottom left corner of the screen.
     *
     * @param touchCoords    the point in touch coordinates (origin top left)
     * @param viewportX      the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportY      the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportWidth  the width of the viewport in pixels
     * @param viewportHeight the height of the viewport in pixels
     * @return the mutated and unprojected touchCoords {@link Vector3}
     */
    public Vector3 unproject(Vector3 touchCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        float x = touchCoords.x - viewportX, y = ApplicationEnvironment.graphics.getHeight() - touchCoords.y - viewportY;
        touchCoords.x = (2 * x) / viewportWidth - 1;
        touchCoords.y = (2 * y) / viewportHeight - 1;
        touchCoords.z = 2 * touchCoords.z - 1;
        touchCoords.prj(invProjectionView);
        return touchCoords;
    }

    /**
     * Function to translate a point given in touch coordinates to world space. It's the same as GLU gluUnProject but does not
     * rely on OpenGL. The viewport is assumed to span the whole screen and is fetched from {@link Graphics#getWidth()} and
     * {@link Graphics#getHeight()}. The x- and y-coordinate of vec are assumed to be in touch coordinates (origin is the top left
     * corner, y pointing down, x pointing to the right) as reported by the touch methods in {@link Input}. A z-coordinate of 0
     * will return a point on the near plane, a z-coordinate of 1 will return a point on the far plane.
     *
     * @param touchCoords the point in touch coordinates
     * @return the mutated and unprojected touchCoords {@link Vector3}
     */
    public Vector3 unproject(Vector3 touchCoords) {
        unproject(touchCoords, 0, 0, ApplicationEnvironment.graphics.getWidth(), ApplicationEnvironment.graphics.getHeight());
        return touchCoords;
    }

    /**
     * Projects the {@link Vector3} given in world space to screen coordinates. It's the same as GLU gluProject with one small
     * deviation: The viewport is assumed to span the whole screen. The screen coordinate system has its origin in the
     * <b>bottom</b> left, with the y-axis pointing <b>upwards</b> and the x-axis pointing to the right. This makes it easily
     * useable in conjunction with {@link Batch} and similar classes.
     *
     * @return the mutated and projected worldCoords {@link Vector3}
     */
    public Vector3 project(Vector3 worldCoords) {
        project(worldCoords, 0, 0, ApplicationEnvironment.graphics.getWidth(), ApplicationEnvironment.graphics.getHeight());
        return worldCoords;
    }

    /**
     * Projects the {@link Vector3} given in world space to screen coordinates. It's the same as GLU gluProject with one small
     * deviation: The viewport is assumed to span the whole screen. The screen coordinate system has its origin in the
     * <b>bottom</b> left, with the y-axis pointing <b>upwards</b> and the x-axis pointing to the right. This makes it easily
     * useable in conjunction with {@link Batch} and similar classes. This method allows you to specify the viewport position and
     * dimensions in the coordinate system, with the origin in the bottom
     * left corner of the screen.
     *
     * @param viewportX      the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportY      the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportWidth  the width of the viewport in pixels
     * @param viewportHeight the height of the viewport in pixels
     * @return the mutated and projected worldCoords {@link Vector3}
     */
    public Vector3 project(Vector3 worldCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
        worldCoords.prj(combined);
        worldCoords.x = viewportWidth * (worldCoords.x + 1) / 2 + viewportX;
        worldCoords.y = viewportHeight * (worldCoords.y + 1) / 2 + viewportY;
        worldCoords.z = (worldCoords.z + 1) / 2;
        return worldCoords;
    }

    /**
     * Creates a picking {@link Ray} from the coordinates given in touch coordinates. It is assumed that the viewport spans the
     * whole screen. The touch coordinates origin is assumed to be in the top left corner, its y-axis pointing down, the x-axis
     * pointing to the right. The returned instance is not a new instance but an internal member only accessible via this function.
     *
     * @param viewportX      the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportY      the coordinate of the bottom left corner of the viewport in glViewport coordinates.
     * @param viewportWidth  the width of the viewport in pixels
     * @param viewportHeight the height of the viewport in pixels
     * @return the picking Ray.
     */
    public Ray getPickRay(float touchX, float touchY, float viewportX, float viewportY, float viewportWidth,
                          float viewportHeight) {
        unproject(ray.origin.set(touchX, touchY, 0), viewportX, viewportY, viewportWidth, viewportHeight);
        unproject(ray.direction.set(touchX, touchY, 1), viewportX, viewportY, viewportWidth, viewportHeight);
        ray.direction.sub(ray.origin).nor();
        return ray;
    }

    /**
     * Creates a picking {@link Ray} from the coordinates given in touch coordinates. It is assumed that the viewport spans the
     * whole screen. The touch coordinates origin is assumed to be in the top left corner, its y-axis pointing down, the x-axis
     * pointing to the right. The returned instance is not a new instance but an internal member only accessible via this function.
     *
     * @return the picking Ray.
     */
    public Ray getPickRay(float touchX, float touchY) {
        return getPickRay(touchX, touchY, 0, 0, ApplicationEnvironment.graphics.getWidth(), ApplicationEnvironment.graphics.getHeight());
    }
}
