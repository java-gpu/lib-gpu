package tech.lib.bgfx.jni;

import lombok.Data;
import tech.lib.bgfx.enu.RendererType;
import tech.lib.bgfx.util.PlatformInfo;

@Data
public class VertexLayout {

    private long layoutPtr;
    private RendererType rendererType;

    public VertexLayout() {
        this(getDefaultRendererType());
    }

    public VertexLayout(RendererType rendererType) {
        this.rendererType = rendererType;
        this.layoutPtr = begin(rendererType);
    }

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

    public void end() {
        end(layoutPtr);
    }

    public void dispose() {
        destroy(layoutPtr);
    }

    // Now these are instance-bound native methods
    private native long begin(RendererType rendererType);

    private native void add(long layoutPtr, int attrib, int num, int attribType, boolean normalized, boolean asInt);

    private native void end(long layoutPtr);

    private native void destroy(long layoutPtr);

    // Default
    private static RendererType getDefaultRendererType() {
        var platformInfo = PlatformInfo.getInstance();
        if (platformInfo.getPlatformType() == PlatformInfo.PlatformType.MACOS) {
            return RendererType.METAL;
        } else if (platformInfo.getPlatformType() == PlatformInfo.PlatformType.WINDOWS) {
            return RendererType.DIRECT3D11;
        } else if (platformInfo.getPlatformType() == PlatformInfo.PlatformType.LINUX) {
            return RendererType.VULKAN;
        } else {
            return RendererType.AUTO;
        }
    }
}
