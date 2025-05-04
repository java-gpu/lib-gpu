package tech.lib.bgfx.data;

import lombok.Data;

@Data
public class FloatColor {
    /**
     * Red.
     */
    private float r;
    /**
     * Green.
     */
    private float g;
    /**
     * Glue.
     */
    private float b;
    /**
     * Alpha.
     */
    private float a;

    public FloatColor() {
        this(0f, 0f, 0f, 1f);
    }

    public FloatColor(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    public FloatColor(float r, float g, float b, float a) {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
        this.a = clamp(a);
    }

    // Clamp method to ensure values stay between 0 and 1
    private float clamp(float value) {
        return Math.max(0f, Math.min(1f, value));
    }

    // Copy method
    public FloatColor copy() {
        return new FloatColor(r, g, b, a);
    }

    // To string
    @Override
    public String toString() {
        return String.format("FloatColor(r=%.2f, g=%.2f, b=%.2f, a=%.2f)", r, g, b, a);
    }

    // Converts the float color to packed ARGB int (0xAARRGGBB)
    public int toARGB() {
        int ir = (int) (clamp(r) * 255.0f);
        int ig = (int) (clamp(g) * 255.0f);
        int ib = (int) (clamp(b) * 255.0f);
        int ia = (int) (clamp(a) * 255.0f);

        return ((ia & 0xFF) << 24) |
                ((ir & 0xFF) << 16) |
                ((ig & 0xFF) << 8) |
                (ib & 0xFF);
    }

    // Creates a Color instance from an ARGB int (0xAARRGGBB)
    public static FloatColor fromARGB(int argb) {
        float a = ((argb >> 24) & 0xFF) / 255.0f;
        float r = ((argb >> 16) & 0xFF) / 255.0f;
        float g = ((argb >> 8) & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;
        return new FloatColor(r, g, b, a);
    }
}
