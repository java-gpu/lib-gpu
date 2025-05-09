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

package tech.gpu.lib.scenes.scene2d.utils;

import tech.gpu.lib.input.GestureDetector;
import tech.gpu.lib.math.Vector2;
import tech.gpu.lib.scenes.scene2d.Actor;
import tech.gpu.lib.scenes.scene2d.Event;
import tech.gpu.lib.scenes.scene2d.EventListener;
import tech.gpu.lib.scenes.scene2d.InputEvent;

/**
 * Detects tap, long press, fling, pan, zoom, and pinch gestures on an actor. If there is only a need to detect tap, use
 * {@link ClickListener}.
 *
 * @author Nathan Sweet
 * @see GestureDetector
 */
public class ActorGestureListener implements EventListener {
    static final Vector2 tmpCoords = new Vector2(), tmpCoords2 = new Vector2();

    private final GestureDetector detector;
    InputEvent event;
    Actor actor, touchDownTarget;

    /**
     * @see GestureDetector#GestureDetector(tech.gpu.lib.input.GestureDetector.GestureListener)
     */
    public ActorGestureListener() {
        this(20, 0.4f, 1.1f, Integer.MAX_VALUE);
    }

    /**
     * @see GestureDetector#GestureDetector(float, float, float, float, tech.gpu.lib.input.GestureDetector.GestureListener)
     */
    public ActorGestureListener(float halfTapSquareSize, float tapCountInterval, float longPressDuration, float maxFlingDelay) {
//        detector = new GestureDetector(halfTapSquareSize, tapCountInterval, longPressDuration, maxFlingDelay, new GestureAdapter() {
//            private final Vector2 initialPointer1 = new Vector2(), initialPointer2 = new Vector2();
//            private final Vector2 pointer1 = new Vector2(), pointer2 = new Vector2();
//
//            public boolean tap(float stageX, float stageY, int count, int button) {
//                actor.stageToLocalCoordinates(tmpCoords.set(stageX, stageY));
//                ActorGestureListener.this.tap(event, tmpCoords.x, tmpCoords.y, count, button);
//                return true;
//            }
//
//            public boolean longPress(float stageX, float stageY) {
//                actor.stageToLocalCoordinates(tmpCoords.set(stageX, stageY));
//                return ActorGestureListener.this.longPress(actor, tmpCoords.x, tmpCoords.y);
//            }
//
//            public boolean fling(float velocityX, float velocityY, int button) {
//                stageToLocalAmount(tmpCoords.set(velocityX, velocityY));
//                ActorGestureListener.this.fling(event, tmpCoords.x, tmpCoords.y, button);
//                return true;
//            }
//
//            public boolean pan(float stageX, float stageY, float deltaX, float deltaY) {
//                stageToLocalAmount(tmpCoords.set(deltaX, deltaY));
//                deltaX = tmpCoords.x;
//                deltaY = tmpCoords.y;
//                actor.stageToLocalCoordinates(tmpCoords.set(stageX, stageY));
//                ActorGestureListener.this.pan(event, tmpCoords.x, tmpCoords.y, deltaX, deltaY);
//                return true;
//            }
//
//            public boolean panStop(float stageX, float stageY, int pointer, int button) {
//                actor.stageToLocalCoordinates(tmpCoords.set(stageX, stageY));
//                ActorGestureListener.this.panStop(event, tmpCoords.x, tmpCoords.y, pointer, button);
//                return true;
//            }
//
//            public boolean zoom(float initialDistance, float distance) {
//                ActorGestureListener.this.zoom(event, initialDistance, distance);
//                return true;
//            }
//
//            public boolean pinch(Vector2 stageInitialPointer1, Vector2 stageInitialPointer2, Vector2 stagePointer1,
//                                 Vector2 stagePointer2) {
//                actor.stageToLocalCoordinates(initialPointer1.set(stageInitialPointer1));
//                actor.stageToLocalCoordinates(initialPointer2.set(stageInitialPointer2));
//                actor.stageToLocalCoordinates(pointer1.set(stagePointer1));
//                actor.stageToLocalCoordinates(pointer2.set(stagePointer2));
//                ActorGestureListener.this.pinch(event, initialPointer1, initialPointer2, pointer1, pointer2);
//                return true;
//            }
//
//            private void stageToLocalAmount(Vector2 amount) {
//                actor.stageToLocalCoordinates(amount);
//                amount.sub(actor.stageToLocalCoordinates(tmpCoords2.set(0, 0)));
//            }
//        });
        // TODO
        detector = null;
    }

    public boolean handle(Event e) {
        if (!(e instanceof InputEvent)) return false;
        InputEvent event = (InputEvent) e;

        switch (event.getType()) {
            case touchDown:
                actor = event.getListenerActor();
                touchDownTarget = event.getTarget();
                detector.touchDown(event.getStageX(), event.getStageY(), event.getPointer(), event.getButton());
                actor.stageToLocalCoordinates(tmpCoords.set(event.getStageX(), event.getStageY()));
                touchDown(event, tmpCoords.x, tmpCoords.y, event.getPointer(), event.getButton());
                if (event.getTouchFocus())
                    event.getStage().addTouchFocus(this, event.getListenerActor(), event.getTarget(),
                            event.getPointer(), event.getButton());
                return true;
            case touchUp:
                boolean touchFocusCancel = event.isTouchFocusCancel();
                if (touchFocusCancel)
                    detector.reset();
                else {
                    this.event = event;
                    actor = event.getListenerActor();
                    detector.touchUp(event.getStageX(), event.getStageY(), event.getPointer(), event.getButton());
                    actor.stageToLocalCoordinates(tmpCoords.set(event.getStageX(), event.getStageY()));
                    touchUp(event, tmpCoords.x, tmpCoords.y, event.getPointer(), event.getButton());
                }
                this.event = null;
                actor = null;
                touchDownTarget = null;
                return !touchFocusCancel;
            case touchDragged:
                this.event = event;
                actor = event.getListenerActor();
                detector.touchDragged(event.getStageX(), event.getStageY(), event.getPointer());
                return true;
        }
        return false;
    }

    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
    }

    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    }

    public void tap(InputEvent event, float x, float y, int count, int button) {
    }

    /**
     * If true is returned, additional gestures will not be triggered. No event is provided because this event is triggered by
     * time passing, not by an InputEvent.
     */
    public boolean longPress(Actor actor, float x, float y) {
        return false;
    }

    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
    }

    /**
     * The delta is the difference in stage coordinates since the last pan.
     */
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
    }

    public void panStop(InputEvent event, float x, float y, int pointer, int button) {
    }

    public void zoom(InputEvent event, float initialDistance, float distance) {
    }

    public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
    }

    public GestureDetector getGestureDetector() {
        return detector;
    }

    public Actor getTouchDownTarget() {
        return touchDownTarget;
    }
}
