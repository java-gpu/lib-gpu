package tech.lib.bgfx.util;

import lombok.Getter;
import tech.lib.bgfx.data.Vertex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VertexUtil {
    @Getter
    private static final VertexUtil instance = new VertexUtil();

    private VertexUtil() {
        // Singleton.
    }

    public ByteBuffer convertVertexToByteBuffer(Vertex[] vertices) {
        // Each vertex: 3 floats (12 bytes) + 1 int (4 bytes) = 16 bytes
        int vertexSize = 3 * Float.BYTES + Integer.BYTES;
        ByteBuffer buffer = ByteBuffer.allocateDirect(vertices.length * vertexSize)
                .order(ByteOrder.nativeOrder());

        for (Vertex v : vertices) {
            buffer.putFloat(v.getX());
            buffer.putFloat(v.getY());
            buffer.putFloat(v.getZ());
            buffer.putInt(v.getColor().toARGB());
        }
        // Prepare for reading
        buffer.flip();
        return buffer;
    }

    public ByteBuffer convertFloatToByteBuffer(short[] indices) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(indices.length * Short.BYTES)
                .order(ByteOrder.nativeOrder());

        for (short index : indices) {
            buffer.putShort(index);
        }

        // Prepare for reading
        buffer.flip();
        return buffer;
    }
}
