package tech.gpu.lib.utils;

import java.awt.*;

public class ColorUtil {
    public static Color abgr8888ToColor(int value) {
        float a = ((value >> 24) & 0xff) / 255f;
        float b = ((value >> 16) & 0xff) / 255f;
        float g = ((value >> 8) & 0xff) / 255f;
        float r = (value & 0xff) / 255f;
        return new Color(r, g, b, a);
    }

    /**
     * Multiplies this color and the given color
     *
     * @param color1 the color 1
     * @param color2 the color 2
     * @return New mul color.
     */
    public static Color mul(Color color1, Color color2) {
        return clamp(color1.getRed() * color2.getRed(), color1.getGreen() * color2.getGreen(),
                color1.getBlue() * color2.getBlue(), color1.getAlpha() * color2.getAlpha());
    }

    public static tech.gpu.lib.graphics.Color mul(tech.gpu.lib.graphics.Color color1, Color color2) {
        return clampFloat(color1.r * color2.getRed(), color1.g * color2.getGreen(),
                color1.b * color2.getBlue(), color1.a * color2.getAlpha());
    }

    /**
     * Clamps this Color's components to a valid range [0 - 1]
     *
     * @return New Color for chaining
     */
    public static Color clamp(int r, int g, int b, int a) {
        if (r < 0) {
            r = 0;
        } else if (r > 1) {
            r = 1;
        }

        if (g < 0) {
            g = 0;
        } else if (g > 1) {
            g = 1;
        }

        if (b < 0) {
            b = 0;
        } else if (b > 1) {
            b = 1;
        }

        if (a < 0) {
            a = 0;
        } else if (a > 1) {
            a = 1;
        }
        return new Color(r, g, b, a);
    }

    /**
     * Clamps this Color's components to a valid range [0 - 1]
     *
     * @return New Color for chaining
     */
    public static tech.gpu.lib.graphics.Color clampFloat(float r, float g, float b, float a) {
        if (r < 0) {
            r = 0;
        } else if (r > 1) {
            r = 1;
        }

        if (g < 0) {
            g = 0;
        } else if (g > 1) {
            g = 1;
        }

        if (b < 0) {
            b = 0;
        } else if (b > 1) {
            b = 1;
        }

        if (a < 0) {
            a = 0;
        } else if (a > 1) {
            a = 1;
        }
        return new tech.gpu.lib.graphics.Color(r, g, b, a);
    }
}
