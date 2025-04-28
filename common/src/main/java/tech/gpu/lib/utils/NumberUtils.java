package tech.gpu.lib.utils;

import java.awt.*;

public class NumberUtils {

    public static int floatToRawIntBits(float value) {
        return Float.floatToRawIntBits(value);
    }

    /**
     * Converts the color from a float ABGR encoding to an int ABGR encoding. The alpha is expanded from 0-254 in the float
     * encoding (see {@link #intToFloatColor(int)}) to 0-255, which means converting from int to float and back to int can be
     * lossy.
     */
    public static int floatToIntColor(float value) {
        int intBits = Float.floatToRawIntBits(value);
        intBits |= (int) ((intBits >>> 24) * (255f / 254f)) << 24;
        return intBits;
    }

    /**
     * Packs the color components into a 32-bit integer with the format ABGR and then converts it to a float. Alpha is compressed
     * from 0-255 to use only even numbers between 0-254 to avoid using float bits in the NaN range (see
     * {@link NumberUtils#intToFloatColor(int)}). Converting a color to a float and back can be lossy for alpha.
     *
     * @return the packed color as a 32-bit float
     */
    public static float toFloatBits(Color color) {
        int colorVal = ((255 * color.getAlpha()) << 24) | ((255 * color.getBlue()) << 16) | ((255 * color.getGreen()) << 8) | ((255 * color.getRed()));
        return NumberUtils.intToFloatColor(colorVal);
    }

    public static float toFloatBits(tech.gpu.lib.graphics.Color color) {
        int colorVal = ((int) (255 * color.a) << 24) | ((int) (255 * color.b) << 16) | ((int) (255 * color.g) << 8) | ((int) (255 * color.r));
        return NumberUtils.intToFloatColor(colorVal);
    }

    /**
     * Packs the color components into a 32-bit integer with the format ABGR.
     *
     * @return the packed color as a 32-bit int.
     */
    public static int toIntBits(Color color) {
        return ((int) (255 * color.getAlpha()) << 24) | ((int) (255 * color.getBlue()) << 16) |
                ((int) (255 * color.getGreen()) << 8) | ((int) (255 * color.getRed()));
    }

    /**
     * Encodes the ABGR int color as a float. The alpha is compressed to use only even numbers between 0-254 to avoid using bits
     * in the NaN range (see {@link Float#intBitsToFloat(int)} javadocs). Rendering which uses colors encoded as floats should
     * expand the 0-254 back to 0-255, else colors cannot be fully opaque.
     */
    public static float intToFloatColor(int value) {
        return Float.intBitsToFloat(value & 0xfeffffff);
    }

    public static float intBitsToFloat(int value) {
        return Float.intBitsToFloat(value);
    }
}
