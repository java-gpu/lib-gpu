package tech.lib.bgfx.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vertex extends Vec3 {
    private FloatColor color;

    public Vertex(float x, float y, float z, float r, float g, float b, float a) {
        this(x, y, y, new FloatColor(r, g, b, a));
    }

    public Vertex(float x, float y, float z, int rgba) {
        this(x, y, y, FloatColor.fromARGB(rgba));
    }

    public Vertex(float x, float y, float z, FloatColor color) {
        super(x, y, z);
        this.color = color;
    }
}
