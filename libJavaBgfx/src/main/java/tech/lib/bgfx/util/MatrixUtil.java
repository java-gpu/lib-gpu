package tech.lib.bgfx.util;

import lombok.Getter;

public class MatrixUtil {

    @Getter
    private static final MatrixUtil instance = new MatrixUtil();

    private MatrixUtil() {
        // Singleton
    }

    /**
     * Creates a 4x4 orthographic projection matrix.
     *
     * @param left             horizontal bounds
     * @param right            horizontal bounds
     * @param bottom           vertical bounds
     * @param top              vertical bounds
     * @param near             depth range
     * @param far              depth range
     * @param offset           optional z-offset
     * @param homogeneousDepth determines depth mapping for Vulkan-style (0..1) or OpenGL-style (-1..1)
     * @return 4x4 orthographic projection matrix
     */
    public float[] mtxOrtho(float left, float right, float bottom, float top, float near, float far, float offset,
            boolean homogeneousDepth) {
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        float zz = homogeneousDepth ? (1.0f / depth) : (2.0f / depth);
        float ww = homogeneousDepth ? (near + offset) / depth : ((far + near + 2.0f * offset) / depth);

        float[] out = new float[16];
        // Column-major order (same as bx::mtxOrtho)
        out[0] = 2.0f / width;
        out[1] = 0.0f;
        out[2] = 0.0f;
        out[3] = 0.0f;

        out[4] = 0.0f;
        out[5] = 2.0f / height;
        out[6] = 0.0f;
        out[7] = 0.0f;

        out[8] = 0.0f;
        out[9] = 0.0f;
        out[10] = -zz;
        out[11] = 0.0f;

        out[12] = -(right + left) / width;
        out[13] = -(top + bottom) / height;
        out[14] = -ww;
        out[15] = 1.0f;

        // Return
        return out;
    }
}
