package tech.lib.bgfx.app;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.jni.Bgfx;

import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
@Getter
@Setter
public class AwtAppWindow extends Frame implements AppWindow {
    private final Canvas canvas;
    private final long windowPointer;
    private ShaderHandler shaderHandler;
    private boolean prioritize3DEngine;
    private int gpuIndex;
    @Setter
    private DropTarget dropTarget;

    public AwtAppWindow(int width, int height, String title) {
        this(width, height, title, false, -1);
    }

    public AwtAppWindow(int width, int height, String title, boolean prioritize3DEngine, int gpuIndex) {
        super();
        this.prioritize3DEngine = prioritize3DEngine;
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
        add(canvas, BorderLayout.CENTER);
        windowPointer = Bgfx.getNativeHandler(this, canvas);
        log.debug("Windows pointer [{}]", windowPointer);
        initialBgfx();
    }

    public void initialBgfx() {
        boolean initResult = Bgfx.initForAwt(windowPointer, canvas, prioritize3DEngine, gpuIndex);
        if (!initResult) {
            throw new HeadlessException("Fail to create AppWindow object!");
        }
    }

    @Override
    public void shutdownWindow() {
        this.dispose();
    }

    public ShaderHandler loadShaderProgram(String vsShaderPath, String fsShaderPath) {
        long vs = Bgfx.loadShader(vsShaderPath);
        long fs = Bgfx.loadShader(fsShaderPath);
        long program = Bgfx.createProgram(vs, fs, true);
        this.shaderHandler = new ShaderHandler(vs, fs, program);
        return this.shaderHandler;
    }

    public AwtAppWindow(int width, int height, String title, boolean prioritize3DEngine) {
        this(width, height, title, prioritize3DEngine, -1);
    }

    public AwtAppWindow(int width, int height, String title, int gpuIndex) {
        this(width, height, title, false, gpuIndex);
    }
}
