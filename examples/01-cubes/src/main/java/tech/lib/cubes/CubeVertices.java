package tech.lib.cubes;

import tech.lib.bgfx.data.Vertex;

public class CubeVertices {
    public static Vertex[] vertices = new Vertex[]{
            new Vertex(-1.0f, 1.0f, 1.0f, 0xff000000),
            new Vertex(1.0f, 1.0f, 1.0f, 0xff0000ff),
            new Vertex(-1.0f, -1.0f, 1.0f, 0xff00ff00),
            new Vertex(1.0f, -1.0f, 1.0f, 0xff00ffff),
            new Vertex(-1.0f, 1.0f, -1.0f, 0xffff0000),
            new Vertex(1.0f, 1.0f, -1.0f, 0xffff00ff),
            new Vertex(-1.0f, -1.0f, -1.0f, 0xffffff00),
            new Vertex(1.0f, -1.0f, -1.0f, 0xffffffff),
    };

    public static short[] indices = {
            0, 1, 2, // 0
            1, 3, 2,
            4, 6, 5, // 2
            5, 6, 7,
            0, 2, 4, // 4
            4, 2, 6,
            1, 5, 3, // 6
            5, 7, 3,
            0, 4, 1, // 8
            4, 5, 1,
            2, 3, 6, // 10
            6, 3, 7,
    };
}
