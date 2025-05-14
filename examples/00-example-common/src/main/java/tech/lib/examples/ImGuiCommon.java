package tech.lib.examples;

import imgui.*;
import imgui.extension.imguizmo.ImGuizmo;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.internal.ImGuiContext;
import imgui.type.ImInt;
import lombok.Getter;
import tech.lib.bgfx.data.TransientIndexBuffer;
import tech.lib.bgfx.data.TransientVertexBuffer;
import tech.lib.bgfx.enu.RendererType;
import tech.lib.bgfx.enu.TextureFormat;
import tech.lib.bgfx.enu.UniformType;
import tech.lib.bgfx.enu.ViewMode;
import tech.lib.bgfx.graphics.TextureHandle;
import tech.lib.bgfx.jni.*;
import tech.lib.bgfx.util.BgfxEncoder;
import tech.lib.bgfx.util.MatrixUtil;
import tech.lib.imgui.enu.ImGuiConst;
import tech.lib.imgui.enu.ImGuiFont;
import tech.lib.imgui.font.FontConstants;
import tech.lib.imgui.font.FontRangeMerge;
import tech.lib.imgui.util.FontUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class ImGuiCommon {

    public static final int IMGUI_MBUT_LEFT = 1;
    public static final int IMGUI_MBUT_RIGHT = 1 << 1;
    public static final int IMGUI_MBUT_MIDDLE = 1 << 2;
    @Getter
    private static final ImGuiCommon instance = new ImGuiCommon();
    public static ByteBuffer s_iconsKenneyTtf = FontUtils.loadFontFromResource("fonts/kenney-icons.ttf");
    public static ByteBuffer s_iconsFontAwesomeTtf = FontUtils.loadFontFromResource("fonts/fontawesome-webfont.ttf");
    public static ByteBuffer s_robotoRegularTtf = FontUtils.loadFontFromResource("fonts/Roboto-Regular.ttf");
    public static ByteBuffer s_robotoMonoRegularTtf = FontUtils.loadFontFromResource("fonts/roboto-mono/Roboto-Regular.ttf");
    public static FontRangeMerge[] s_fontRangeMerge =
            new FontRangeMerge[]{new FontRangeMerge(s_iconsKenneyTtf, s_iconsKenneyTtf.remaining(), FontConstants.ICON_RANGE_KI),
                    new FontRangeMerge(s_iconsFontAwesomeTtf, s_iconsFontAwesomeTtf.remaining(), FontConstants.ICON_RANGE_FA),
            };
    private ImGuiIO imGuiIO;
    private long m_last;
    private ImGuiContext imGuiContext;
    private boolean appAcceptingEvents;
    private int m_lastScroll;
    private int m_viewId;
    private Map<ImGuiFont, ImFont> mFont;
    private TextureHandle m_texture;
    private short s_tex;
    private short u_imageLodEnabled;
    private long m_imageProgram;
    private long m_program;
    private VertexLayout m_layout;

    private ImGuiCommon() {
        // Singleton
        mFont = new HashMap<>();
        // ImGuiIO
        imGuiIO = ImGui.getIO();
        imGuiIO.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        // Contact
        imGuiContext = ImGui.createContext();
    }

    public void create(float _fontSize) {
        m_viewId = 255;
        m_lastScroll = 0;
        m_last = BxTimer.getHPCounter();

        imGuiContext = ImGui.createContext();

        imGuiIO.setDisplaySize(new ImVec2(1280.0f, 720.0f));
        imGuiIO.setDeltaTime(1.0f / 60.0f);
        imGuiIO.setIniFilename(null);

        setupStyle(true);

        imGuiIO.addBackendFlags(ImGuiBackendFlags.RendererHasVtxOffset);

        // Get the current renderer type
        RendererType type = Bgfx.getRendererType();

        // Load ImGui main shader program
        long vsImgui = createEmbeddedShader(type, "vs_ocornut_imgui");
        long fsImgui = createEmbeddedShader(type, "fs_ocornut_imgui");
        m_program = Bgfx.createProgram(vsImgui, fsImgui, true);

        // Create image LOD uniform
        u_imageLodEnabled = Bgfx.createUniform("u_imageLodEnabled", UniformType.Vec4);

        // Load ImGui image rendering shader
        long vsImage = createEmbeddedShader(type, "vs_imgui_image");
        long fsImage = createEmbeddedShader(type, "fs_imgui_image");
        m_imageProgram = Bgfx.createProgram(vsImage, fsImage, true);

        // Define ImGui vertex layout
        m_layout = new VertexLayout(Bgfx.getRendererType());
        m_layout.add(Attrib.POSITION, 2, AttribType.FLOAT).add(Attrib.TEXCOORD0, 2, AttribType.FLOAT)
                .add(Attrib.COLOR0, 4, AttribType.UINT8, true).end();

        // Create sampler uniform
        s_tex = Bgfx.createUniform("s_tex", UniformType.Sampler);

        ImFontConfig config = new ImFontConfig();
        config.setFontDataOwnedByAtlas(false);
        config.setMergeMode(false);
        // config.setMergeGlyphCenterV(true);

        ImFontAtlas fonts = imGuiIO.getFonts();

        // Get Cyrillic glyph ranges
        short[] ranges = fonts.getGlyphRangesCyrillic();

        // Font config
        config.setGlyphRanges(ranges);

        // Add main fonts
        ImFont regularFont = fonts.addFontFromMemoryTTF(s_robotoRegularTtf.array(), _fontSize, config);
        ImFont monoFont = fonts.addFontFromMemoryTTF(s_robotoMonoRegularTtf.array(), _fontSize - 3.0f, config);

        // Store fonts (assuming mFont is an array or map)
        mFont.put(ImGuiFont.REGULAR, regularFont);
        mFont.put(ImGuiFont.MONO, monoFont);

        // Merge extra ranges into regularFont
        config.setMergeMode(true);
        config.setDstFont(regularFont);

        for (FontRangeMerge frm : s_fontRangeMerge) {
            config.setGlyphRanges(frm.getRanges());
            fonts.addFontFromMemoryTTF(frm.getData().array(), _fontSize - 3.0f, config);
        }

        final ImInt outWidth = new ImInt();
        final ImInt outHeight = new ImInt();
        final ByteBuffer texData = fonts.getTexDataAsRGBA32(outWidth, outHeight);

        int width = outWidth.get();
        int height = outHeight.get();

        m_texture = Bgfx.createTexture2D((short) width, (short) height, false, (byte) 1, TextureFormat.BGRA8, 0, texData);

        ImGui.dockSpaceOverViewport();
        // ImGui::InitDockContext();
    }

    public void setupStyle(boolean _dark) {
        ImGuiStyle style = ImGui.getStyle();
        if (_dark) {
            ImGui.styleColorsDark(style);
        } else {
            ImGui.styleColorsLight(style);
        }

        style.setFrameRounding(4.0f);
        style.setWindowBorderSize(0.0f);
    }

    private long createEmbeddedShader(RendererType type, String name) {
        // TODO
        return 0;
    }

    public void destroy() {
        //        ImGui.ShutdownDockContext();
        ImGui.destroyContext(imGuiContext);

        Bgfx.destroyUniform(s_tex);
        Bgfx.destroyTexture(m_texture.getTextureId());
        //
        Bgfx.destroyProgram(u_imageLodEnabled);
        Bgfx.destroyProgram(m_imageProgram);
        Bgfx.destroyProgram(m_program);
    }

    public void beginFrame(int _mx, int _my, int _button, int _scroll, int _width, int _height, char _inputChar, long _viewId) {
        addInputCharacter(_inputChar);

        imGuiIO.setDisplaySize(new ImVec2((float) _width, (float) _height));

        long now = BxTimer.getHPCounter();
        long frameTime = now - m_last;
        m_last = now;
        double freq = (double) BxTimer.getHPFrequency();
        imGuiIO.setDeltaTime((float) (frameTime / freq));

        // Update mouse position
        imGuiIO.setMousePos((float) _mx, (float) _my);

        // Update mouse buttons
        imGuiIO.setMouseDown(0, (_button & IMGUI_MBUT_LEFT) != 0); // Left
        imGuiIO.setMouseDown(1, (_button & IMGUI_MBUT_RIGHT) != 0); // Right
        imGuiIO.setMouseDown(2, (_button & IMGUI_MBUT_MIDDLE) != 0); // Middle

        // Update mouse wheel
        imGuiIO.setMouseWheel((float) (_scroll - m_lastScroll));

        ImGui.newFrame();
        ImGuizmo.beginFrame();
    }

    public void addInputCharacter(char c) {
        if (c == 0 || !appAcceptingEvents) {
            return;
        }

        imGuiIO.addInputCharacter(c);
    }

    public void render(ImDrawData drawData) {
        int dispWidth = (int) (drawData.getDisplaySize().x * drawData.getFramebufferScale().x);
        int dispHeight = (int) (drawData.getDisplaySize().y * drawData.getFramebufferScale().y);
        if (dispWidth <= 0 || dispHeight <= 0) {
            return;
        }

        Bgfx.setViewName(m_viewId, "ImGui");
        Bgfx.setViewMode(m_viewId, ViewMode.Sequential);

        BgfxCaps caps = Bgfx.getCaps();
        {
            float x = drawData.getDisplayPos().x;
            float y = drawData.getDisplayPos().y;
            float width = drawData.getDisplaySize().x;
            float height = drawData.getDisplaySize().y;
            float[] ortho = MatrixUtil.getInstance().mtxOrtho(x, x + width, y + height, y, 0.0f, 1000.0f, 0.0f, caps.isHomogeneousDepth());

            Bgfx.setViewTransform(m_viewId, null, ortho);
            Bgfx.setViewRect(m_viewId, 0, 0, (short) width, (short) height);
        }

        ImVec2 clipPos = drawData.getDisplayPos();
        ImVec2 clipScale = drawData.getFramebufferScale();

        var cmdListsCount = drawData.getCmdListsCount();
        for (int ii = 0; ii < cmdListsCount; ++ii) {
            int numVertices = drawData.getCmdListVtxBufferSize(ii);
            int numIndices = drawData.getCmdListIdxBufferSize(ii);
            var vtxBuffer = drawData.getCmdListVtxBufferData(ii);
            var idxBuffer = drawData.getCmdListIdxBufferData(ii);
            var indexSize = idxBuffer.array().length / numIndices;

            if (!checkAvailTransientBuffers(numVertices, m_layout.getLayoutPtr(), numIndices)) {
                break;
            }

            TransientVertexBuffer tvb = Bgfx.allocTransientVertexBuffer(numVertices, m_layout.getLayoutPtr());
            TransientIndexBuffer tib = Bgfx.allocTransientIndexBuffer(numIndices, ImDrawData.sizeOfImDrawIdx() >= 4);

            tvb.copyFrom(vtxBuffer);
            tib.copyFrom(idxBuffer);

            BgfxEncoder encoder = Bgfx.begin();

            int numCmds = drawData.getCmdListCmdBufferSize(ii);
            for (var j = 0; j < numCmds; j++) {
                int elemCount = drawData.getCmdListCmdBufferElemCount(ii, j);
                if (elemCount != 0) {
                    int idxOffset = drawData.getCmdListCmdBufferIdxOffset(ii, j);
                    // int indices = idxOffset * ImDrawData.sizeOfImDrawIdx();
                    ImVec4 cmdClipRect = drawData.getCmdListCmdBufferClipRect(ii, j);
                    int vtxOffset = drawData.getCmdListCmdBufferVtxOffset(ii, j);
                    long textureId = drawData.getCmdListCmdBufferTextureId(ii, j);

                    long state = Bgfx.COMBINED_STATE_1;
                    var program = m_program;
                    int handle = (int) (textureId & 0xFFFFFFFFL);

                    if (textureId != 0) {

                        int flags = (int) (textureId >>> 32);
                        int mip = (int) ((textureId >> 24) & 0xFF);

                        if ((flags & ImGuiConst.IMGUI_FLAGS_ALPHA_BLEND) != 0) {
                            state |= Bgfx.STATE_BLEND_FUNC(Bgfx.STATE_BLEND_SRC_ALPHA, Bgfx.STATE_BLEND_INV_SRC_ALPHA);
                        }

                        if (mip != 0) {
                            float[] lodEnabled = new float[]{mip, 1.0f, 0.0f, 0.0f};
                            Bgfx.setUniform(u_imageLodEnabled, lodEnabled);
                            program = m_imageProgram;
                        }
                    } else {
                        state |= Bgfx.STATE_BLEND_FUNC(Bgfx.STATE_BLEND_SRC_ALPHA, Bgfx.STATE_BLEND_INV_SRC_ALPHA);
                    }

                    ImVec4 clipRect = new ImVec4();
                    clipRect.x = (cmdClipRect.x - clipPos.x) * clipScale.x;
                    clipRect.y = (cmdClipRect.y - clipPos.y) * clipScale.y;
                    clipRect.z = (cmdClipRect.z - clipPos.x) * clipScale.x;
                    clipRect.w = (cmdClipRect.w - clipPos.y) * clipScale.y;

                    if (clipRect.x < dispWidth && clipRect.y < dispHeight && clipRect.z >= 0.0f && clipRect.w >= 0.0f) {
                        short xx = (short) Math.max(clipRect.x, 0.0f);
                        short yy = (short) Math.max(clipRect.y, 0.0f);
                        short ww = (short) (Math.min(clipRect.z, 65535.0f) - xx);
                        short hh = (short) (Math.min(clipRect.w, 65535.0f) - yy);

                        encoder.setScissor(xx, yy, ww, hh);
                        encoder.setState(state);
                        encoder.setTexture(0, s_tex, handle);
                        encoder.setVertexBuffer(0, tvb, vtxOffset, numVertices);
                        encoder.setIndexBuffer(tib, idxOffset, elemCount);
                        encoder.submit(m_viewId, program);
                    }
                }
            }

            encoder.end();
        }
    }

    public boolean checkAvailTransientBuffers(int _numVertices, long _layoutPointer, int _numIndices) {
        boolean result = _numVertices == Bgfx.getAvailTransientVertexBuffer(_numVertices, _layoutPointer);
        result = result && (0 == _numIndices || _numIndices == Bgfx.getAvailTransientIndexBuffer(_numIndices));
        return result;
    }
}
