/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
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

package tech.gpu.lib.scenes.scene2d.ui;


import tech.gpu.lib.ApplicationEnvironment;
import tech.gpu.lib.math.Vector2;
import tech.gpu.lib.scenes.scene2d.*;


/**
 * A listener that shows a tooltip actor when the mouse is over another actor.
 *
 * @author Nathan Sweet
 */
public class Tooltip<T extends Actor> extends InputListener {
    static Vector2 tmp = new Vector2();

    private final TooltipManager manager;
    final Container<T> container;
    boolean instant, always, touchIndependent;
    Actor targetActor;

    /**
     * @param contents May be null.
     */
    public Tooltip(T contents) {
        this(contents, TooltipManager.getInstance());
    }

    /**
     * @param contents May be null.
     */
    public Tooltip(T contents, TooltipManager manager) {
        this.manager = manager;

        container = new Container(contents) {
            public void act(float delta) {
                super.act(delta);
                if (targetActor != null && targetActor.getStage() == null) remove();
            }
        };
        container.setTouchable(Touchable.disabled);
    }

    public TooltipManager getManager() {
        return manager;
    }

    public Container<T> getContainer() {
        return container;
    }

    public void setActor(T contents) {
        container.setActor(contents);
    }

    public T getActor() {
        return container.getActor();
    }

    /**
     * If true, this tooltip is shown without delay when hovered.
     */
    public void setInstant(boolean instant) {
        this.instant = instant;
    }

    /**
     * If true, this tooltip is shown even when tooltips are not {@link TooltipManager#enabled}.
     */
    public void setAlways(boolean always) {
        this.always = always;
    }

    /**
     * If true, this tooltip will be shown even when screen is touched simultaneously with entering tooltip's targetActor
     */
    public void setTouchIndependent(boolean touchIndependent) {
        this.touchIndependent = touchIndependent;
    }

    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (instant) {
            container.toFront();
            return false;
        }
        manager.touchDown(this);
        return false;
    }

    public boolean mouseMoved(InputEvent event, float x, float y) {
        if (container.hasParent()) return false;
        setContainerPosition(event.getListenerActor(), x, y);
        return true;
    }

    private void setContainerPosition(Actor actor, float x, float y) {
        this.targetActor = actor;
        Stage stage = actor.getStage();
        if (stage == null) return;

        container.setSize(manager.maxWidth, Integer.MAX_VALUE);
        container.validate();
        container.width(container.getActor().getWidth());
        container.pack();

        float offsetX = manager.offsetX, offsetY = manager.offsetY, dist = manager.edgeDistance;
        Vector2 point = actor.localToStageCoordinates(tmp.set(x + offsetX, y - offsetY - container.getHeight()));
        if (point.y < dist) point = actor.localToStageCoordinates(tmp.set(x + offsetX, y + offsetY));
        if (point.x < dist) point.x = dist;
        if (point.x + container.getWidth() > stage.getWidth() - dist)
            point.x = stage.getWidth() - dist - container.getWidth();
        if (point.y + container.getHeight() > stage.getHeight() - dist)
            point.y = stage.getHeight() - dist - container.getHeight();
        container.setPosition(point.x, point.y);

        point = actor.localToStageCoordinates(tmp.set(actor.getWidth() / 2, actor.getHeight() / 2));
        point.sub(container.getX(), container.getY());
        container.setOrigin(point.x, point.y);
    }

    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (pointer != -1) return;
        if (touchIndependent && ApplicationEnvironment.input.isTouched()) return;
        Actor actor = event.getListenerActor();
        if (fromActor != null && fromActor.isDescendantOf(actor)) return;
        setContainerPosition(actor, x, y);
        manager.enter(this);
    }

    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (toActor != null && toActor.isDescendantOf(event.getListenerActor())) return;
        hide();
    }

    public void hide() {
        manager.hide(this);
    }
}
