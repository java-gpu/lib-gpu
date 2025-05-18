package tech.lib.bgfx.jni;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.enu.RendererType;

@Data
@Slf4j
public class VertexLayout {

    private long layoutPtr;
    private RendererType rendererType;

    public VertexLayout() {
        this(Bgfx.getRendererType());
    }

    public VertexLayout(RendererType rendererType) {
        this.rendererType = rendererType;
        log.debug("Creating vertex layout with renderer type [{}]", rendererType.name());
        this.layoutPtr = begin(rendererType);
        log.debug("Layout pointer: {}", layoutPtr);
    }

    // Now these are instance-bound native methods
    private native long begin(RendererType rendererType);

    public VertexLayout add(Attrib attrib, int num, AttribType attribType) {
        return add(attrib, num, attribType, false);
    }

    public VertexLayout add(Attrib attrib, int num, AttribType attribType, boolean normalized) {
        return add(attrib, num, attribType, normalized, false);
    }

    public VertexLayout add(Attrib attrib, int num, AttribType attribType, boolean normalized, boolean asInt) {
        add(layoutPtr, attrib.value, num, attribType.value, normalized, asInt);
        return this;
    }

    private native void add(long layoutPtr, int attrib, int num, int attribType, boolean normalized, boolean asInt);

    public void end() {
        end(layoutPtr);
    }

    private native void end(long layoutPtr);

    public void dispose() {
        destroy(layoutPtr);
    }

    private native void destroy(long layoutPtr);
}
