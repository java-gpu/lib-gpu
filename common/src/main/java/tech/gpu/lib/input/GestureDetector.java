/*******************************************************************************
 * Copyright 2025 See AUTHORS file.
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

package tech.gpu.lib.input;

import lombok.Getter;
import lombok.Setter;
import tech.gpu.lib.Application;
import tech.gpu.lib.InputProcessor;
import tech.gpu.lib.math.Vector2;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@link InputProcessor} implementation that detects gestures (tap, long press, fling, pan, zoom, pinch) and hands them to a
 * {@link GestureListener}.
 *
 * @author mzechner
 */
public class GestureDetector implements InputProcessor {

    protected Application app;

    final GestureListener listener;
    private float tapRectangleWidth;
    private float tapRectangleHeight;
    private long tapCountInterval;
    @Setter
    private long longPressMilliSeconds;
    @Setter
    private long maxFlingDelay;

    private boolean inTapRectangle;
    private int tapCount;
    private long lastTapTime;
    private float lastTapX, lastTapY;
    private int lastTapButton, lastTapPointer;
    boolean longPressFired;
    private boolean pinching;
    @Getter
    private boolean panning;

    private final VelocityTracker tracker = new VelocityTracker();
    private float tapRectangleCenterX, tapRectangleCenterY;
    private long touchDownTime;
    Point pointer1 = new Point();
    private final Point pointer2 = new Point();
    private final Point initialPointer1 = new Point();
    private final Point initialPointer2 = new Point();

    private final Timer longPressTimer = new Timer();
    private boolean isLongPressScheduled = false;
    private final TimerTask longPressTask = new TimerTask() {
        @Override
        public void run() {
            if (!longPressFired) {
                longPressFired = listener.longPress(pointer1.x, pointer1.y);
            }
        }
    };

    /**
     * Creates a new GestureDetector with default values: halfTapSquareSize=20, tapCountInterval=0.4f, longPressDuration=1.1f,
     * maxFlingDelay=Integer.MAX_VALUE.
     */
    public GestureDetector(Application app, GestureListener listener) {
        this(app, 20, 0.4f, 1100, Integer.MAX_VALUE, listener);
    }

    /**
     * @param halfTapSquareSize half width in pixels of the square around an initial touch event, see
     *                          {@link GestureListener#tap(float, float, int, int)}.
     * @param tapCountInterval  time in seconds that must pass for two touch down/up sequences to be detected as consecutive taps.
     * @param longPressDuration time in seconds that must pass for the detector to fire a
     *                          {@link GestureListener#longPress(float, float)} event.
     * @param maxFlingDelay     no fling event is fired when the time in seconds the finger was dragged is larger than this, see
     *                          {@link GestureListener#fling(float, float, int)}
     */
    public GestureDetector(Application app, float halfTapSquareSize, float tapCountInterval, int longPressDuration, float maxFlingDelay,
                           GestureListener listener) {
        this(app, halfTapSquareSize, halfTapSquareSize, tapCountInterval, longPressDuration, maxFlingDelay, listener);
    }

    /**
     * @param halfTapRectangleWidth  half width in pixels of the rectangle around an initial touch event, see
     *                               {@link GestureListener#tap(float, float, int, int)}.
     * @param halfTapRectangleHeight half height in pixels of the rectangle around an initial touch event, see
     *                               {@link GestureListener#tap(float, float, int, int)}.
     * @param tapCountInterval       time in seconds that must pass for two touch down/up sequences to be detected as consecutive taps.
     * @param longPressDuration      time in seconds that must pass for the detector to fire a
     *                               {@link GestureListener#longPress(float, float)} event.
     * @param maxFlingDelay          no fling event is fired when the time in seconds the finger was dragged is larger than this, see
     *                               {@link GestureListener#fling(float, float, int)}
     */
    public GestureDetector(Application app, float halfTapRectangleWidth, float halfTapRectangleHeight, float tapCountInterval,
                           long longPressDuration, float maxFlingDelay, GestureListener listener) {
        this.app = app;
        if (listener == null) throw new IllegalArgumentException("listener cannot be null.");
        this.tapRectangleWidth = halfTapRectangleWidth;
        this.tapRectangleHeight = halfTapRectangleHeight;
        this.tapCountInterval = (long) (tapCountInterval * 1000000000L);
        this.longPressMilliSeconds = longPressDuration;
        this.maxFlingDelay = (long) (maxFlingDelay * 1000000000L);
        this.listener = listener;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return touchDown((float) x, (float) y, pointer, button);
    }

    public boolean touchDown(float x, float y, int pointer, int button) {
        if (pointer > 1) return false;

        if (pointer == 0) {
            pointer1.setLocation(x, y);
            touchDownTime = app.getInput().getCurrentEventTime();
            tracker.start(x, y, touchDownTime);
            if (app.getInput().isTouched(1)) {
                // Start pinch.
                inTapRectangle = false;
                pinching = true;
                initialPointer1.setLocation(pointer1);
                initialPointer2.setLocation(pointer2);
                longPressTask.cancel();
                isLongPressScheduled = false;
            } else {
                // Normal touch down.
                inTapRectangle = true;
                pinching = false;
                longPressFired = false;
                tapRectangleCenterX = x;
                tapRectangleCenterY = y;
                if (!isLongPressScheduled) {
                    longPressTimer.schedule(longPressTask, longPressMilliSeconds);
                    isLongPressScheduled = true;
                }
            }
        } else {
            // Start pinch.
            pointer2.setLocation(x, y);
            inTapRectangle = false;
            pinching = true;
            initialPointer1.setLocation(pointer1);
            initialPointer2.setLocation(pointer2);
            longPressTask.cancel();
            isLongPressScheduled = false;
        }
        return listener.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return touchDragged((float) x, (float) y, pointer);
    }

    public boolean touchDragged(float x, float y, int pointer) {
        if (pointer > 1) return false;
        if (longPressFired) return false;

        if (pointer == 0)
            pointer1.setLocation(x, y);
        else
            pointer2.setLocation(x, y);

        // handle pinch zoom
        if (pinching) {
            boolean result = listener.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
            return listener.zoom(initialPointer1.distance(initialPointer2), pointer1.distance(pointer2)) || result;
        }

        // update tracker
        tracker.update(x, y, app.getInput().getCurrentEventTime());

        // check if we are still tapping.
        if (inTapRectangle && isNotWithinTapRectangle(x, y, tapRectangleCenterX, tapRectangleCenterY)) {
            longPressTask.cancel();
            inTapRectangle = false;
        }

        // if we have left the tap square, we are panning
        if (!inTapRectangle) {
            panning = true;
            return listener.pan(x, y, tracker.deltaX, tracker.deltaY);
        }

        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return touchUp((float) x, (float) y, pointer, button);
    }

    public boolean touchUp(float x, float y, int pointer, int button) {
        if (pointer > 1) return false;

        // check if we are still tapping.
        if (inTapRectangle && isNotWithinTapRectangle(x, y, tapRectangleCenterX, tapRectangleCenterY))
            inTapRectangle = false;

        boolean wasPanning = panning;
        panning = false;

        longPressTask.cancel();
        if (longPressFired) return false;

        if (inTapRectangle) {
            // handle taps
            if (lastTapButton != button || lastTapPointer != pointer || System.nanoTime() - lastTapTime > tapCountInterval
                    || isNotWithinTapRectangle(x, y, lastTapX, lastTapY)) tapCount = 0;
            tapCount++;
            lastTapTime = System.nanoTime();
            lastTapX = x;
            lastTapY = y;
            lastTapButton = button;
            lastTapPointer = pointer;
            touchDownTime = 0;
            return listener.tap(x, y, tapCount, button);
        }

        if (pinching) {
            // handle pinch end
            pinching = false;
            listener.pinchStop();
            panning = true;
            // we are in pan mode again, reset velocity tracker
            if (pointer == 0) {
                // first pointer has lifted off, set up panning to use the second pointer...
                tracker.start(pointer2.x, pointer2.y, app.getInput().getCurrentEventTime());
            } else {
                // second pointer has lifted off, set up panning to use the first pointer...
                tracker.start(pointer1.x, pointer1.y, app.getInput().getCurrentEventTime());
            }
            return false;
        }

        // handle no longer panning
        boolean handled = false;
        if (wasPanning && !panning) handled = listener.panStop(x, y, pointer, button);

        // handle fling
        long time = app.getInput().getCurrentEventTime();
        if (time - touchDownTime <= maxFlingDelay) {
            tracker.update(x, y, time);
            handled = listener.fling(tracker.getVelocityX(), tracker.getVelocityY(), button) || handled;
        }
        touchDownTime = 0;
        return handled;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        cancel();
        return false;
    }

    /**
     * No further gesture events will be triggered for the current touch, if any.
     */
    public void cancel() {
        longPressTask.cancel();
        longPressFired = true;
    }

    /**
     * @return whether the user touched the screen long enough to trigger a long press event.
     */
    public boolean isLongPressed() {
        return isLongPressed(longPressMilliSeconds);
    }

    /**
     * @param duration duration in millisecond
     * @return whether the user touched the screen for as much or more than the given duration.
     */
    public boolean isLongPressed(long duration) {
        if (touchDownTime == 0) return false;
        return System.nanoTime() - touchDownTime > (duration * 1000000L);
    }

    public void reset() {
        longPressTask.cancel();
        touchDownTime = 0;
        panning = false;
        inTapRectangle = false;
        tracker.lastTime = 0;
    }

    private boolean isNotWithinTapRectangle(float x, float y, float centerX, float centerY) {
        return !(Math.abs(x - centerX) < tapRectangleWidth) || !(Math.abs(y - centerY) < tapRectangleHeight);
    }

    /**
     * The tap square will no longer be used for the current touch.
     */
    public void invalidateTapSquare() {
        inTapRectangle = false;
    }

    public void setTapSquareSize(float halfTapSquareSize) {
        setTapRectangleSize(halfTapSquareSize, halfTapSquareSize);
    }

    public void setTapRectangleSize(float halfTapRectangleWidth, float halfTapRectangleHeight) {
        this.tapRectangleWidth = halfTapRectangleWidth;
        this.tapRectangleHeight = halfTapRectangleHeight;
    }

    /**
     * @param tapCountInterval time in seconds that must pass for two touch down/up sequences to be detected as consecutive
     *                         taps.
     */
    public void setTapCountInterval(float tapCountInterval) {
        this.tapCountInterval = (long) (tapCountInterval * 1000000000L);
    }

    /**
     * Register an instance of this class with a {@link GestureDetector} to receive gestures such as taps, long presses, flings,
     * panning or pinch zooming. Each method returns a boolean indicating if the event should be handed to the next listener (false
     * to hand it to the next listener, true otherwise).
     *
     * @author mzechner
     */
    public static interface GestureListener {
        /**
         * @see InputProcessor#touchDown(int, int, int, int)
         */
        public boolean touchDown(float x, float y, int pointer, int button);

        /**
         * Called when a tap occured. A tap happens if a touch went down on the screen and was lifted again without moving outside
         * of the tap square. The tap square is a rectangular area around the initial touch position as specified on construction
         * time of the {@link GestureDetector}.
         *
         * @param count the number of taps.
         */
        public boolean tap(float x, float y, int count, int button);

        public boolean longPress(float x, float y);

        /**
         * Called when the user dragged a finger over the screen and lifted it. Reports the last known velocity of the finger in
         * pixels per second.
         *
         * @param velocityX velocity on x in seconds
         * @param velocityY velocity on y in seconds
         */
        public boolean fling(float velocityX, float velocityY, int button);

        /**
         * Called when the user drags a finger over the screen.
         *
         * @param deltaX the difference in pixels to the last drag event on x.
         * @param deltaY the difference in pixels to the last drag event on y.
         */
        public boolean pan(float x, float y, float deltaX, float deltaY);

        /**
         * Called when no longer panning.
         */
        public boolean panStop(float x, float y, int pointer, int button);

        /**
         * Called when the user performs a pinch zoom gesture. The original distance is the distance in pixels when the gesture
         * started.
         *
         * @param initialDistance distance between fingers when the gesture started.
         * @param distance        current distance between fingers.
         */
        public boolean zoom(double initialDistance, double distance);

        default boolean zoom(float initialDistance, float distance) {
            return false;
        }

        /**
         * Called when a user performs a pinch zoom gesture. Reports the initial positions of the two involved fingers and their
         * current positions.
         *
         * @param initialPointer1 initialPointer1
         * @param initialPointer2 initialPointer2
         * @param pointer1        pointer1
         * @param pointer2        pointer2
         */
        default boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        default boolean pinch(Point initialPointer1, Point initialPointer2, Point pointer1, Point pointer2) {
            return false;
        }

        /**
         * Called when no longer pinching.
         */
        public void pinchStop();
    }

    /**
     * Derrive from this if you only want to implement a subset of {@link GestureListener}.
     *
     * @author mzechner
     */
    public static class GestureAdapter implements GestureListener {
        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(double initialDistance, double distance) {
            return false;
        }

        @Override
        public boolean pinch(Point initialPointer1, Point initialPointer2, Point pointer1, Point pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {
        }
    }

    static class VelocityTracker {
        int sampleSize = 10;
        float lastX, lastY;
        float deltaX, deltaY;
        long lastTime;
        int numSamples;
        float[] meanX = new float[sampleSize];
        float[] meanY = new float[sampleSize];
        long[] meanTime = new long[sampleSize];

        public void start(float x, float y, long timeStamp) {
            lastX = x;
            lastY = y;
            deltaX = 0;
            deltaY = 0;
            numSamples = 0;
            for (int i = 0; i < sampleSize; i++) {
                meanX[i] = 0;
                meanY[i] = 0;
                meanTime[i] = 0;
            }
            lastTime = timeStamp;
        }

        public void update(float x, float y, long currTime) {
            deltaX = x - lastX;
            deltaY = y - lastY;
            lastX = x;
            lastY = y;
            long deltaTime = currTime - lastTime;
            lastTime = currTime;
            int index = numSamples % sampleSize;
            meanX[index] = deltaX;
            meanY[index] = deltaY;
            meanTime[index] = deltaTime;
            numSamples++;
        }

        public float getVelocityX() {
            float meanX = getAverage(this.meanX, numSamples);
            float meanTime = getAverage(this.meanTime, numSamples) / 1000000000.0f;
            if (meanTime == 0) return 0;
            return meanX / meanTime;
        }

        public float getVelocityY() {
            float meanY = getAverage(this.meanY, numSamples);
            float meanTime = getAverage(this.meanTime, numSamples) / 1000000000.0f;
            if (meanTime == 0) return 0;
            return meanY / meanTime;
        }

        private float getAverage(float[] values, int numSamples) {
            numSamples = Math.min(sampleSize, numSamples);
            float sum = 0;
            for (int i = 0; i < numSamples; i++) {
                sum += values[i];
            }
            return sum / numSamples;
        }

        private long getAverage(long[] values, int numSamples) {
            numSamples = Math.min(sampleSize, numSamples);
            long sum = 0;
            for (int i = 0; i < numSamples; i++) {
                sum += values[i];
            }
            if (numSamples == 0) return 0;
            return sum / numSamples;
        }

        private float getSum(float[] values, int numSamples) {
            numSamples = Math.min(sampleSize, numSamples);
            float sum = 0;
            for (int i = 0; i < numSamples; i++) {
                sum += values[i];
            }
            if (numSamples == 0) return 0;
            return sum;
        }
    }
}
