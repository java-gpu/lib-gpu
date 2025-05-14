package tech.lib.bgfx.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class TransientVertexBuffer {
    private final long ptr;

    public native void copyFrom(ByteBuffer src);
}
