package tech.lib.bgfx.data;

import lombok.Data;

@Data
public class Vertex {
    private float x, y, z;
    private FloatColor color;

    public Vertex(float x, float y, float z, float r, float g, float b, float a) {
        this(x, y, y, new FloatColor(r, g, b, a));
    }

    public Vertex(float x, float y, float z, int rgba) {
        this(x, y, y, FloatColor.fromARGB(rgba));
    }

    public Vertex(float x, float y, float z, FloatColor color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }
}
