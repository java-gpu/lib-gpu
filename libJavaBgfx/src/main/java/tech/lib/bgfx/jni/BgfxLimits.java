package tech.lib.bgfx.jni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BgfxLimits {
    private int maxDrawCalls;
    private int maxBlits;
    private int maxTextureSize;
    private int maxTextureLayers;
    private int maxViews;
    private int maxFrameBuffers;
    private int maxFBAttachments;
    private int maxPrograms;
    private int maxShaders;
    private int maxTextures;
    private int maxTextureSamplers;
    private int maxComputeBindings;
    private int maxVertexLayouts;
    private int maxVertexStreams;
    private int maxIndexBuffers;
    private int maxVertexBuffers;
    private int maxDynamicIndexBuffers;
    private int maxDynamicVertexBuffers;
    private int maxUniforms;
    private int maxOcclusionQueries;
    private int maxEncoders;
    private int minResourceCbSize;
    private int transientVbSize;
    private int transientIbSize;
}
