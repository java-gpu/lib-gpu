package tech.lib.imgui.font;

import lombok.Data;

import java.nio.ByteBuffer;

@Data
public class FontRangeMerge {
    private ByteBuffer data;
    private int size;
    private short[] ranges;

    public FontRangeMerge(ByteBuffer data, int size, short[] ranges) {
        this.data = data;
        this.size = size;
        this.ranges = ranges;
    }

}
