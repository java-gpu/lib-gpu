package tech.lib.bgfx.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vec3 {
    protected float x;
    protected float y;
    protected float z;

    public Vec3 subtract(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public Vec3 cross(Vec3 other) {
        return new Vec3(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    public float dot(Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vec3 normalize() {
        float len = (float) Math.sqrt(x * x + y * y + z * z);
        return new Vec3(x / len, y / len, z / len);
    }
}
