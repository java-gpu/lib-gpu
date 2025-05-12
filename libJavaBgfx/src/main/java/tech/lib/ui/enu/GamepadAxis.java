package tech.lib.ui.enu;

public enum GamepadAxis {
    LeftX,
    LeftY,
    LeftZ,
    RightX,
    RightY,
    RightZ,
    AxisNone;

    public static GamepadAxis fromId(int axisId) {
        return switch (axisId) {
            case 0 -> LeftX;
            case 1 -> LeftY;
            case 2 -> LeftZ;
            case 3 -> RightX;
            case 4 -> RightY;
            case 5 -> RightZ;
            default -> AxisNone;
        };
    }
}
