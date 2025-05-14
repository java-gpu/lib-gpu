package tech.lib.bgfx.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.lib.bgfx.data.TransientIndexBuffer;
import tech.lib.bgfx.data.TransientVertexBuffer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BgfxEncoder {
    // native bgfx::Encoder*
    private long ptr;

    public native void end();

    public native void setScissor(int x, int y, int width, int height);

    public native void setState(long state);

    public native void setTexture(int stage, short sampler, int texture);

    public native void setVertexBuffer(int stream, TransientVertexBuffer handle, int startVertex, int numVertices);

    public native void setIndexBuffer(TransientIndexBuffer handle, int firstIndex, int numIndices);

    public native void submit(int viewId, long shaderProgramHandle);
}
