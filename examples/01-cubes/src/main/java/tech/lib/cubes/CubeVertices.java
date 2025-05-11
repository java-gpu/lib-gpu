package tech.lib.cubes;

import tech.lib.bgfx.data.Vertex;
import tech.lib.bgfx.enu.BgfxStateFlag;

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

    public static short[] triList = {
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

    public static short[] triStrip = {
            0, 1, 2,
            3,
            7,
            1,
            5,
            0,
            4,
            2,
            6,
            7,
            4,
            5,
    };

    public static short[] lineList = {
            0, 1,
            0, 2,
            0, 4,
            1, 3,
            1, 5,
            2, 3,
            2, 6,
            3, 7,
            4, 5,
            4, 6,
            5, 7,
            6, 7,
    };

    public static short[] lineStrip = {
            0, 2, 3, 1, 5, 7, 6, 4,
            0, 2, 6, 4, 5, 7, 3, 1,
            0,
    };

    public static short[] points = {
            0, 1, 2, 3, 4, 5, 6, 7
    };

    public static String[] ptNames = {
            "Triangle List",
            "Triangle Strip",
            "Lines",
            "Line Strip",
            "Points",
    };

    public static BgfxStateFlag[] ptStates = {
            BgfxStateFlag.NONE,
            BgfxStateFlag.PT_TRISTRIP,
            BgfxStateFlag.PT_LINES,
            BgfxStateFlag.PT_LINESTRIP,
            BgfxStateFlag.PT_POINTS
    };
}
