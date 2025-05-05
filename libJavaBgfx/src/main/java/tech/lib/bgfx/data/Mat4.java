package tech.lib.bgfx.data;

public class Mat4 {
    public float[] m = new float[16];

    public Mat4() {
        for (int i = 0; i < 16; i++) m[i] = 0;
    }

    public static float[] lookAt(Vec3 eye, Vec3 center, Vec3 up) {
        Vec3 f = center.subtract(eye).normalize();
        Vec3 s = f.cross(up).normalize();
        Vec3 u = s.cross(f);

        float[] result = new float[16];
        result[0] = s.x;
        result[4] = s.y;
        result[8] = s.z;
        result[1] = u.x;
        result[5] = u.y;
        result[9] = u.z;
        result[2] = -f.x;
        result[6] = -f.y;
        result[10] = -f.z;
        result[12] = -s.dot(eye);
        result[13] = -u.dot(eye);
        result[14] = f.dot(eye);
        result[15] = 1.0f;

        return result;
    }

    public static float[] perspective(float fovYDegrees, float aspect, float near, float far) {
        float fovYRadians = (float) Math.toRadians(fovYDegrees);
        float f = 1.0f / (float) Math.tan(fovYRadians / 2.0f);

        float[] result = new float[16];
        result[0] = f / aspect;
        result[5] = f;
        result[10] = (far + near) / (near - far);
        result[11] = -1.0f;
        result[14] = (2 * far * near) / (near - far);
        result[15] = 0.0f;

        return result;
    }

    /**
     * The 4×4 rotation matrix for Y-axis. This produces a 4×4 matrix in column-major order.
     *
     * @param radians radians
     * @return 4×4 matrix
     */
    public static float[] rotateY(float radians) {
        float c = (float) Math.cos(radians);
        float s = (float) Math.sin(radians);

        float[] m = new float[16];

        m[0] = c;
        m[4] = 0;
        m[8] = -s;
        m[12] = 0;
        m[1] = 0;
        m[5] = 1;
        m[9] = 0;
        m[13] = 0;
        m[2] = s;
        m[6] = 0;
        m[10] = c;
        m[14] = 0;
        m[3] = 0;
        m[7] = 0;
        m[11] = 0;
        m[15] = 1;

        return m;
    }
}