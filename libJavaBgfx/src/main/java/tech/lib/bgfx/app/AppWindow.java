package tech.lib.bgfx.app;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.jni.Bgfx;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;

@Slf4j
@Getter
public class AppWindow extends JFrame {
    private final Canvas canvas;
    private final long windowPtr;
    private ShaderHandler shaderHandler;
    private boolean priority3D;
    private int gpuIndex;
    @Setter
    private DropTarget dropTarget;

    public AppWindow(int width, int height, String title) {
        this(width, height, title, false, -1);
    }

    public AppWindow(int width, int height, String title, boolean priority3D) {
        this(width, height, title, priority3D, -1);
    }

    public AppWindow(int width, int height, String title, int gpuIndex) {
        this(width, height, title, false, gpuIndex);
    }

    public AppWindow(int width, int height, String title, boolean priority3D, int gpuIndex) {
        super();
        this.priority3D = priority3D;
        this.gpuIndex = gpuIndex;
        setSize(width, height);
        setTitle(title);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas();
        add(canvas);
        windowPtr = Bgfx.getNativeHandler(this, canvas);
        log.debug("Windows pointer [{}]", windowPtr);
        initialBgfx();
    }

    private void initialBgfx() {
        boolean initResult = Bgfx.init(windowPtr, canvas, priority3D, gpuIndex);
        if (!initResult) {
            throw new HeadlessException("Fail to create AppWindow object!");
        }
    }

    public void shutdownBgfx() {
        log.debug("🔴 bgfx shutdown");
        SwingUtilities.invokeLater(Bgfx::shutdown);
    }

    /**
     * Load shader program + set as shaderHandler for this windows + return loaded program.
     *
     * @param vsShaderPath Vertex Shader Handler path
     * @param fsShaderPath Fragment Shader Handler path
     * @return Loaded Shader program
     */
    public ShaderHandler loadShaderProgram(String vsShaderPath, String fsShaderPath) {
        long vs = Bgfx.loadShader(vsShaderPath);
        long fs = Bgfx.loadShader(fsShaderPath);
        long program = Bgfx.createProgram(vs, fs, true);
        this.shaderHandler = new ShaderHandler(vs, fs, program);
        return this.shaderHandler;
    }
}
