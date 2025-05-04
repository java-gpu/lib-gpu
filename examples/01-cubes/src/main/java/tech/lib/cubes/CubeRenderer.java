package tech.lib.cubes;

import tech.lib.bgfx.app.AppWindow;
import tech.lib.bgfx.jni.Bgfx;
import tech.lib.bgfx.jni.VertexLayout;
import tech.lib.bgfx.util.VertexUtil;

public class CubeRenderer {
    private VertexLayout layout;
    private AppWindow window;
    private long vbh;
    private long ibh;

    public CubeRenderer(AppWindow appWindow) {
        init(appWindow);
    }

    private void init(AppWindow appWindow) {
        this.window = appWindow;
        layout = new VertexLayout();
        var vertexUtil = VertexUtil.getInstance();
        vbh = Bgfx.createVertexBuffer(vertexUtil.convertVertexToByteBuffer(CubeVertices.vertices), layout.getLayoutPtr());
        ibh = Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.indices));
    }

    public void renderFrame(float time) {
        Bgfx.setViewClear(0, Bgfx.CLEAR_COLOR | Bgfx.CLEAR_DEPTH, 0x303030ff, 1.0f, (byte) 0);
        Bgfx.setViewRect(0, 0, 0, window.getWidth(), window.getHeight());
//
//        float[] view = Mat4.lookAt(new Vec3(0, 0, -7), new Vec3(0, 0, 0), new Vec3(0, 1, 0));
//        float[] proj = Mat4.perspective(60.0f, width / (float) height, 0.1f, 100.0f);
//        Bgfx.setViewTransform(0, view, proj);
//
//        float[] model = Mat4.rotateY(time);
//        Bgfx.setTransform(model);
//
//        Bgfx.setVertexBuffer(0, vbh);
//        Bgfx.setIndexBuffer(ibh);
//        Bgfx.setState(Bgfx.STATE_DEFAULT);
//        Bgfx.submit(0, program);
//        Bgfx.frame();
    }

    public void shutdown() {
//        Bgfx.destroyVertexBuffer(vbh);
//        Bgfx.destroyIndexBuffer(ibh);
//        Bgfx.destroyProgram(program);
//        Bgfx.shutdown();
    }
}
