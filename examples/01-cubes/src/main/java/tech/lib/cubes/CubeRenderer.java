package tech.lib.cubes;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.type.ImInt;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.app.AwtAppWindow;
import tech.lib.bgfx.data.IndexBufferHandle;
import tech.lib.bgfx.data.Mat4;
import tech.lib.bgfx.data.Vec3;
import tech.lib.bgfx.data.VertexBufferHandle;
import tech.lib.bgfx.enu.BgfxDebugFlag;
import tech.lib.bgfx.enu.BgfxResetFlag;
import tech.lib.bgfx.enu.BgfxStateFlag;
import tech.lib.bgfx.jni.Bgfx;
import tech.lib.bgfx.jni.BxTimer;
import tech.lib.bgfx.util.VertexUtil;
import tech.lib.examples.Entry;
import tech.lib.examples.ImGuiCommon;
import tech.lib.ui.event.CustomMouseEvent;

import java.awt.event.MouseEvent;

@Slf4j
@Data
public class CubeRenderer {
    public static String s_ptNames[] = {"Triangle List", "Triangle Strip", "Lines", "Line Strip", "Points"};
    public static BgfxStateFlag s_ptState[] =
            {BgfxStateFlag.NONE, BgfxStateFlag.PT_TRISTRIP, BgfxStateFlag.PT_LINES, BgfxStateFlag.PT_LINESTRIP, BgfxStateFlag.PT_POINTS};
    private final ImGuiCommon imGuiCommon;
    private final Entry entry;
    private AwtAppWindow window;
    private long vbh;
    private long[] ibhs;
    private long timeOffset;
    private CustomMouseEvent latestMouseEvent;
    private BgfxResetFlag reset;
    private BgfxDebugFlag debug;
    private boolean m_r;
    private boolean m_g;
    private boolean m_b;
    private boolean m_a;
    private ImInt m_pt;
    private long m_timeOffset;
    private VertexBufferHandle m_vbh;
    private IndexBufferHandle[] m_ibh = new IndexBufferHandle[s_ptState.length];

    public CubeRenderer(AwtAppWindow appWindow, BgfxResetFlag reset, BgfxDebugFlag debug) {
        this.debug = debug;
        this.reset = reset;
        this.entry = new Entry(appWindow.getWindowPointer());
        this.imGuiCommon = ImGuiCommon.getInstance();
        init(appWindow);
    }

    private void init(AwtAppWindow appWindow) {
        this.window = appWindow;
        var vertexUtil = VertexUtil.getInstance();
        timeOffset = BxTimer.getHPCounter();
        this.imGuiCommon.create(14);
        m_pt = new ImInt(0);
        log.debug("Creating VertexBuffer....");
        vbh = Bgfx.createVertexBuffer(vertexUtil.convertVertexToByteBuffer(CubeVertices.vertices), imGuiCommon.getLayout().getLayoutPtr());
        log.debug("Creating IndexBuffer....");
        ibhs = new long[]{Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.triList)),
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.triStrip)),
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.lineList)),
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.lineStrip)),
                Bgfx.createIndexBuffer(vertexUtil.convertFloatToByteBuffer(CubeVertices.points))
        };
        Bgfx.frame();
        log.debug("Initialize finished!");
    }

    public boolean update() {
        if (!entry.processEvents(window.getWidth(), window.getHeight(), BgfxDebugFlag.STATS, BgfxResetFlag.VSYNC, latestMouseEvent)) {
            int button;
            switch (latestMouseEvent.getButton()) {
                case MouseEvent.BUTTON1 -> button = ImGuiCommon.IMGUI_MBUT_LEFT;
                case MouseEvent.BUTTON2 -> button = ImGuiCommon.IMGUI_MBUT_MIDDLE;
                case MouseEvent.BUTTON3 -> button = ImGuiCommon.IMGUI_MBUT_RIGHT;
                default -> button = 0;
            }
            Bgfx.frame();
            imGuiCommon.beginFrame(latestMouseEvent.getX(), latestMouseEvent.getY(), button, latestMouseEvent.getZ(), window.getWidth(),
                                   window.getHeight(), null);

            //            showExampleDialog(this);
            //

            ImGui.setNextWindowPos(new ImVec2(window.getWidth() - window.getWidth() / 5.0f - 10.0f, 10.0f), ImGuiCond.FirstUseEver);
            ImGui.setWindowSize(new ImVec2(window.getWidth() / 5.0f, window.getHeight() / 3.5f), ImGuiCond.FirstUseEver);
            ImGui.begin("Settings", null, 0);

            ImGui.checkbox("Write R", m_r);
            ImGui.checkbox("Write G", m_g);
            ImGui.checkbox("Write B", m_b);
            ImGui.checkbox("Write A", m_a);

            ImGui.text("Primitive topology:");
            ImGui.combo("##topology", m_pt, s_ptNames, s_ptNames.length);

            ImGui.end();

            imGuiCommon.endFrame();

            float time = (float) ((BxTimer.getHPCounter() - m_timeOffset) / (double) BxTimer.getHPFrequency());

            Vec3 at = new Vec3(0.0f, 0.0f, 0.0f);
            Vec3 eye = new Vec3(0.0f, 0.0f, -35.0f);

            // Set view and projection matrix for view 0.
            {
                float[] view = new float[16];
                Bgfx.mtxLookAt(view, eye, at);

                float[] proj = new float[16];
                Bgfx.mtxProj(proj, 60.0f, (float) window.getWidth() / window.getHeight(), 0.1f, 100.0f,
                             Bgfx.getCaps().isHomogeneousDepth());
                Bgfx.setViewTransform(0, view, proj);

                // Set view 0 default viewport.
                Bgfx.setViewRect(0, 0, 0, window.getWidth(), window.getHeight());
            }

            // This dummy draw call is here to make sure that view 0 is cleared
            // if no other draw calls are submitted to view 0.
            Bgfx.touch(0);

            IndexBufferHandle ibh = m_ibh[m_pt.get()];
            long state =
                    (m_r ? BgfxStateFlag.WRITE_R.getValue() : 0L) | (m_g ? BgfxStateFlag.WRITE_G.getValue() : 0L) | (m_b ? BgfxStateFlag.WRITE_B.getValue() : 0L) | (m_a ? BgfxStateFlag.WRITE_A.getValue() : 0L) | BgfxStateFlag.WRITE_Z.getValue() | BgfxStateFlag.DEPTH_TEST_LESS.getValue() | BgfxStateFlag.CULL_CW.getValue() | BgfxStateFlag.MSAA.getValue() | s_ptState[m_pt.get()].getValue();

            // Submit 11x11 cubes.
            for (int yy = 0; yy < 11; ++yy) {
                for (int xx = 0; xx < 11; ++xx) {
                    float[] mtx = new float[16];
                    Bgfx.mtxRotateXY(mtx, time + xx * 0.21f, time + yy * 0.37f);
                    mtx[12] = -15.0f + (float) xx * 3.0f;
                    mtx[13] = -15.0f + (float) yy * 3.0f;
                    mtx[14] = 0.0f;

                    // Set model matrix for rendering.
                    Bgfx.setTransform(mtx);

                    // Set vertex and index buffer.
                    Bgfx.setVertexBuffer(0, m_vbh.getPtr());
                    Bgfx.setIndexBuffer(ibh.getPtr());

                    // Set render states.
                    Bgfx.setState(state);

                    // Submit primitive for rendering to view 0.
                    Bgfx.submit(0, window.getShaderHandler().getProgram());
                }
            }

            // Advance to next frame. Rendering thread will be kicked to process submitted rendering primitives.
            Bgfx.frame();

            return true;
        }

        return false;
    }

    public void renderFrame(float time) {
        log.debug("Starting renderFrame....");
        log.debug("Bgfx.setViewClear");
        Bgfx.setViewClear(0, Bgfx.CLEAR_COLOR | Bgfx.CLEAR_DEPTH, 0x303030ff, 1.0f, (byte) 0);
        log.debug("Bgfx.setViewRect");
        Bgfx.setViewRect(0, 0, 0, window.getWidth(), window.getHeight());

        log.debug("Mat4.lookAt");
        float[] view = Mat4.lookAt(new Vec3(0, 0, -7), new Vec3(0, 0, 0), new Vec3(0, 1, 0));
        log.debug("Mat4.perspective");
        float[] proj = Mat4.perspective(60.0f, window.getWidth() / (float) window.getHeight(), 0.1f, 100.0f);
        log.debug("Bgfx.setViewTransform");
        Bgfx.setViewTransform(0, view, proj);

        log.debug("Mat4.rotateY");
        float[] model = Mat4.rotateY(time);
        log.debug("Bgfx.setTransform");
        Bgfx.setTransform(model);
        //        log.debug("Bgfx.setVertexBuffer");
        //        Bgfx.setVertexBuffer(0, vbh);
        //        IndexBufferHandle ibh = m_ibh[m_pt.get()];
        //        Bgfx.setIndexBuffer(ibh.getPtr());
        //        log.debug("Bgfx.setState");
        //        Bgfx.setState(BgfxStateFlag.DEFAULT.getValue());
        //        log.debug("Bgfx.submit");
        //        Bgfx.submit(0, window.getShaderHandler().getProgram());
        //        log.debug("Bgfx.frame");
        //        Bgfx.frame();
    }

    public void shutdown() {
        this.imGuiCommon.destroy();
        for (long ibh : ibhs) {
            Bgfx.destroyIndexBuffer(ibh);
        }
        Bgfx.destroyVertexBuffer(vbh);
        Bgfx.destroyProgram(window.getShaderHandler().getProgram());
        // Shutdown bgfx.
        Bgfx.shutdown();
    }
}
