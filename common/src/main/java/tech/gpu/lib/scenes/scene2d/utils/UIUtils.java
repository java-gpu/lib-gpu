package tech.gpu.lib.scenes.scene2d.utils;

import tech.gpu.lib.ApplicationEnvironment;
import tech.gpu.lib.Input.Buttons;
import tech.gpu.lib.Input.Keys;

public final class UIUtils {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    private UIUtils() {
    }

    //    static public boolean isAndroid = SharedLibraryLoader.os == Os.Android;
    static public boolean isMac = OS.contains("mac");
    static public boolean isWindows = OS.contains("win");
    static public boolean isLinux = OS.contains("nux") || OS.contains("nix");
//    static public boolean isIos = SharedLibraryLoader.os == Os.IOS;

    static public boolean left() {
        return ApplicationEnvironment.input.isButtonPressed(Buttons.LEFT);
    }

    static public boolean left(int button) {
        return button == Buttons.LEFT;
    }

    static public boolean right() {
        return ApplicationEnvironment.input.isButtonPressed(Buttons.RIGHT);
    }

    static public boolean right(int button) {
        return button == Buttons.RIGHT;
    }

    static public boolean middle() {
        return ApplicationEnvironment.input.isButtonPressed(Buttons.MIDDLE);
    }

    static public boolean middle(int button) {
        return button == Buttons.MIDDLE;
    }

    static public boolean shift() {
        return ApplicationEnvironment.input.isKeyPressed(Keys.SHIFT_LEFT) || ApplicationEnvironment.input.isKeyPressed(Keys.SHIFT_RIGHT);
    }

    static public boolean shift(int keycode) {
        return keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT;
    }

    static public boolean ctrl() {
        if (isMac)
            return ApplicationEnvironment.input.isKeyPressed(Keys.SYM);
        else
            return ApplicationEnvironment.input.isKeyPressed(Keys.CONTROL_LEFT) || ApplicationEnvironment.input.isKeyPressed(Keys.CONTROL_RIGHT);
    }

    static public boolean ctrl(int keycode) {
        if (isMac)
            return keycode == Keys.SYM;
        else
            return keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT;
    }

    static public boolean alt() {
        return ApplicationEnvironment.input.isKeyPressed(Keys.ALT_LEFT) || ApplicationEnvironment.input.isKeyPressed(Keys.ALT_RIGHT);
    }

    static public boolean alt(int keycode) {
        return keycode == Keys.ALT_LEFT || keycode == Keys.ALT_RIGHT;
    }
}
