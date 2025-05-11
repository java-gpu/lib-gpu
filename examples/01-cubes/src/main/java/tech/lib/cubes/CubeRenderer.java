package tech.lib.cubes;

import imgui.ImGui;
import tech.lib.bgfx.app.AppWindow;
import tech.lib.bgfx.data.Mat4;
import tech.lib.bgfx.data.Vec3;
import tech.lib.bgfx.enu.BgfxStateFlag;
import tech.lib.bgfx.jni.Bgfx;
import tech.lib.bgfx.jni.BxTimer;
import tech.lib.bgfx.jni.VertexLayout;
import tech.lib.bgfx.util.VertexUtil;

public class CubeRenderer {
    private VertexLayout layout;
    private AppWindow window;
    private long vbh;
    private long[] ibhs;
    private long timeOffset;

    public CubeRenderer(AppWindow appWindow) {
        init(appWindow);
    }

    private void init(AppWindow appWindow) {
        this.window = appWindow;
        layout = new VertexLayout();
        var vertexUtil = VertexUtil.getInstance();
        vbh = Bgfx.createVertexBuffer(vertexUtil.convertVertexToByteBuffer(CubeVertices.vertices), layout.getLayoutPtr());
        ibhs = new long[]{
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.triList)),
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.triStrip)),
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.lineList)),
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.lineStrip)),
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.points))
        };
        timeOffset = BxTimer.getHPCounter();
        ImGui.createContext();
    }

    public void renderFrame(float time) {
        Bgfx.setViewClear(0, Bgfx.CLEAR_COLOR | Bgfx.CLEAR_DEPTH, 0x303030ff, 1.0f, (byte) 0);
        Bgfx.setViewRect(0, 0, 0, window.getWidth(), window.getHeight());

        float[] view = Mat4.lookAt(new Vec3(0, 0, -7), new Vec3(0, 0, 0), new Vec3(0, 1, 0));
        float[] proj = Mat4.perspective(60.0f, window.getWidth() / (float) window.getHeight(), 0.1f, 100.0f);
        Bgfx.setViewTransform(0, view, proj);

        float[] model = Mat4.rotateY(time);
        Bgfx.setTransform(model);

        Bgfx.setVertexBuffer(0, vbh);
//        Bgfx.setIndexBuffer(ibh);
        Bgfx.setState(BgfxStateFlag.DEFAULT.getValue());
        Bgfx.submit(0, window.getShaderHandler().getProgram());
        Bgfx.frame();
    }

    public void shutdown() {
        ImGui.destroyContext();
        for (long ibh : ibhs) {
            Bgfx.destroyIndexBuffer(ibh);
        }
        Bgfx.destroyVertexBuffer(vbh);
        Bgfx.destroyProgram(window.getShaderHandler().getProgram());
        // Shutdown bgfx.
        Bgfx.shutdown();
    }
}
