package tech.lib.ui.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.lib.ui.enu.MouseButton;
import tech.lib.ui.enu.UiEventType;

@Setter
@Getter
@ToString(callSuper = true)
public class MouseEvent extends UiEvent {
    private int mx;
    private int my;
    private int mz;
    MouseButton button;
    boolean down;
    boolean move;

    public MouseEvent(int mx, int my, int mz, MouseButton button, boolean down, boolean move, long windowHandler) {
        super(UiEventType.Mouse, windowHandler);
        this.mx = mx;
        this.my = my;
        this.mz = mz;
        this.button = button;
        this.down = down;
        this.move = move;
    }

    public MouseEvent(int mx, int my, int mz, MouseButton button, boolean down, boolean move) {
        this(mx, my, mz, button, down, move, Long.MAX_VALUE);
    }

}
