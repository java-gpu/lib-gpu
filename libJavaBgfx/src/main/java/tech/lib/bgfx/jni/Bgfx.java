package tech.lib.bgfx.jni;

import com.sun.jna.MyJFramePointer;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import tech.lib.bgfx.data.TransientIndexBuffer;
import tech.lib.bgfx.data.TransientVertexBuffer;
import tech.lib.bgfx.data.Vec3;
import tech.lib.bgfx.enu.*;
import tech.lib.bgfx.graphics.TextureHandle;
import tech.lib.bgfx.util.BgfxEncoder;
import tech.lib.bgfx.util.PlatformInfo;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

public class Bgfx {

    /**
     * Variable definition.
     */
    public static final int CLEAR_COLOR = 0x1;
    public static final int CLEAR_DEPTH = 0x2;
    public static final int CLEAR_STENCIL = 0x4;
    // Constants representing various state flags
    public static final int STATE_WRITE_RGB = 0x00000001;
    public static final int STATE_WRITE_A = 0x00000002;
    public static final int STATE_MSAA = 0x00000004;
    public static final long STATE_BLEND_SRC_ALPHA = 0x0000001000000000L;
    public static final long STATE_BLEND_INV_SRC_ALPHA = 0x0000002000000000L;

    // Method to combine the states using bitwise OR
    public static final int COMBINED_STATE_1 = STATE_WRITE_RGB | STATE_WRITE_A | STATE_MSAA;

    static {
        JniLoader.loadBgFxJni();
    }

    /**
     * Init BGFX platform display. Priority to select 2D render type.
     *
     * @param windowHandlerPointer Native window handler pointer.
     * @param canvas               AWT Canvas. Some platform like linux required it to retrieve information.
     * @return True if success
     */
    public static boolean initForAwt(long windowHandlerPointer, Canvas canvas, int gpuIndex) {
        return initForAwt(windowHandlerPointer, canvas, false, gpuIndex);
    }

    /**
     * Init BGFX platform display.
     *
     * @param windowHandlerPointer Native window handler pointer.
     * @param canvas               AWT Canvas. Some platform like linux required it to retrieve information.
     * @param priority3D           Priority to select 3D render type
     * @param gpuIndex             GPU index
     * @return True if success
     */
    public static native boolean initForAwt(long windowHandlerPointer, Canvas canvas, boolean priority3D, int gpuIndex);

    /**
     * Init BGFX platform display.
     *
     * @param windowHandlerPointer Native window handler pointer.
     * @param priority3D           Priority to select 3D render type
     * @param gpuIndex             GPU index
     * @return True if success
     */
    public static native boolean initForGlfw(long windowHandlerPointer, boolean priority3D, int gpuIndex);

    /**
     * Shut down BGFX.
     */
    public static native void shutdown();

    /**
     * Refresh display.
     */
    public static native void frame();

    public static long getNativeHandler(Window frame, Canvas canvas) {
        PlatformInfo platformInfo = PlatformInfo.getInstance();
        if (Objects.requireNonNull(platformInfo.getPlatformType()) == PlatformInfo.PlatformType.MACOS) {
            return getMacOSNativeHandlerFromCanvas(canvas);
        }
        Pointer pointer = Native.getComponentPointer(frame);
        return new MyJFramePointer(pointer).getPeer();
    }

    /**
     * MacOS specific code to get native window handler.
     *
     * @param canvas AWT canvas.
     * @return Native window handler pointer.
     */
    private static native long getMacOSNativeHandlerFromCanvas(Canvas canvas);

    /**
     * This function sets the resolution and display settings for the backbuffer.
     *
     * @param width  Width
     * @param height Height
     */
    public static void reset(int width, int height, BgfxResetFlag resetFlag) {
        reset(width, height, resetFlag.getValue());
    }

    /**
     * This function sets the resolution and display settings for the backbuffer.
     *
     * @param width  Width
     * @param height Height
     */
    public static native void reset(int width, int height, int resetFlag);

    /**
     * Ensures the specified view is cleared even if no draw calls are submitted.
     * Notes:
     * <br/>
     * Prevents leftover frames from being shown if you skip drawing that frame.
     * <br/>
     * You usually call this once per view at the start of render().
     *
     * @param viewId ViewID
     */
    public static native void touch(int viewId);

    /**
     * Defines the rectangle on screen where rendering will occur for a given view. <br />
     * <br />
     * Notes:
     * <br/>
     * Usually called at the start of each frame.
     * <br/>
     * viewId = 0 is typically the default screen pass.
     * <br/>
     *
     * @param viewId View ID
     * @param x      x
     * @param y      y
     * @param width  Width
     * @param height Height
     */
    public static native void setViewRect(int viewId, int x, int y, int width, int height);

    /**
     * Allocates buffers and submits a colored quad.
     *
     * @param x         x
     * @param y         y
     * @param width     Width
     * @param height    Height
     * @param abgrColor Alpha + Red + Green + Blue color.
     */
    public static native void drawQuad(float x, float y, float width, float height, int abgrColor);

    /**
     * In rendering, each view in bgfx is a rendering pass (with its own camera, framebuffer, etc).
     * Before rendering begins for that view, bgfx can automatically clear:
     *
     * @param viewId     View ID to clear
     * @param clearFlags Clear flag
     * @param rgba       the color buffer (what you see on screen)
     * @param depth      the depth buffer (used for depth testing).
     * @param stencil    the stencil buffer (used for masking).
     */
    public static native void setViewClear(int viewId, int clearFlags, int rgba, float depth, byte stencil);

    public static native long loadShader(String path);

    public static long loadShaderFromClasspathMemory(String relativeLocation) throws IOException {
        try (InputStream in = Bgfx.class.getClassLoader().getResourceAsStream(relativeLocation)) {
            if (in == null) {
                throw new IOException("Fail to load shader from classpath path " + relativeLocation);
            }
            byte[] byteArray = new byte[in.available()];
            int read = in.read(byteArray);
            if (read < byteArray.length) {
                throw new IOException("Load shader from classpath path [" + relativeLocation + "] error! Missed some bytes data!");
            }
            return Bgfx.loadShaderFromMemory(ByteBuffer.wrap(byteArray));
        }
    }

    public static native long loadShaderFromMemory(ByteBuffer shaderData);

    public static native long createProgram(long vsHandle, long fsHandle, boolean destroyShaders);

    public static native long createVertexBuffer(ByteBuffer vertexData, long layoutHandle);

    public static native long createIndexBuffer(ByteBuffer indexData);

    /**
     * Setting the camera view and projection for view ID in BGFX, which is a high-performance, cross-platform rendering library.
     *
     * @param viewId View ID
     * @param view   Camera view
     * @param proj   projection
     */
    public static native void setViewTransform(int viewId, float[] view, float[] proj);

    public static native void setTransform(float[] matrix);

    public static native void setVertexBuffer(int stream, long vbh);

    public static native void setIndexBuffer(long ibh);

    public static native void setState(long state);

    public static native void submit(int viewId, long program);

    public static native void destroyVertexBuffer(long vbh);

    public static native void destroyIndexBuffer(long ibh);

    public static native void destroyProgram(long program);

    public static native void destroyTexture(long handle);

    public static native void destroyUniform(short handle);

    public static native RendererType getRendererType();

    public static native short createUniform(String name, UniformType uniformType);

    public static TextureHandle createTexture2D(short width, short height, boolean hasMips, byte numLayers, TextureFormat format, int flags,
            ByteBuffer mem) {
        long textureId = createNativeTexture2D(width, height, hasMips, numLayers, format, flags, mem);
        return new TextureHandle(textureId);
    }

    private native static long createNativeTexture2D(short width, short height, boolean hasMips, byte numLayers, TextureFormat format,
            int flags, ByteBuffer mem);

    public static native void setViewName(int viewId, String name);

    public static native void setViewMode(int viewId, ViewMode mode);

    public static native BgfxCaps getCaps();

    public static native int getAvailTransientVertexBuffer(int num, long vertexLayoutPtr);

    public static native int getAvailTransientIndexBuffer(int num);

    public static native TransientVertexBuffer allocTransientVertexBuffer(int num, long vertexLayoutPtr);

    public static native TransientIndexBuffer allocTransientIndexBuffer(int num, boolean index32);

    public static native BgfxEncoder begin();

    public static native long STATE_BLEND_FUNC(long src, long dst);

    public static native void setUniform(int uniformHandle, float[] lodEnabled);

    public static native void mtxLookAt(float[] outView, Vec3 eye, Vec3 at);

    public static native void mtxProj(float[] outProj, float fovy, float aspect, float near, float far, boolean homogeneousDepth);

    public static native void mtxRotateXY(float[] resultMatrix, float angleX, float angleY);
}
