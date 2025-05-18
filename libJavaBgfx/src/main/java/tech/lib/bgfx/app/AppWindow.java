package tech.lib.bgfx.app;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.jni.Bgfx;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
@Getter
public class AppWindow extends Frame {
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

    public AppWindow(int width, int height, String title, boolean priority3D, int gpuIndex) {
        super();
        this.priority3D = priority3D;
        this.gpuIndex = gpuIndex;
        setSize(width, height);
        setTitle(title);
        setVisible(true);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Release resources
                dispose();
                // Terminate the JVM
                System.exit(0);
            }
        });
        canvas = new Canvas();
//        add(canvas, BorderLayout.CENTER);
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

    public AppWindow(int width, int height, String title, boolean priority3D) {
        this(width, height, title, priority3D, -1);
    }

    public AppWindow(int width, int height, String title, int gpuIndex) {
        this(width, height, title, false, gpuIndex);
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
