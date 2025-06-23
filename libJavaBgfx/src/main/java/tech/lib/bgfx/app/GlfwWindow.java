package tech.lib.bgfx.app;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import tech.lib.bgfx.jni.Bgfx;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

@Setter
@Getter
public class GlfwWindow implements AppWindow {

    private long windowPointer;
    private ShaderHandler shaderHandler;
    private boolean prioritize3DEngine;
    private int gpuIndex;

    public GlfwWindow(int width, int height, String title) {
        this(width, height, title, false, -1);
    }

    public GlfwWindow(int width, int height, String title, boolean prioritize3DEngine, int gpuIndex) {
        this.prioritize3DEngine = prioritize3DEngine;
        this.gpuIndex = gpuIndex;
        init(width, height, title);
        initialBgfx();
    }

    private void init(int width, int height, String title) {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        windowPointer = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowPointer == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowPointer, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowPointer, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert vidmode != null;
            glfwSetWindowPos(windowPointer, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make context current
        glfwMakeContextCurrent(windowPointer);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowPointer);
    }

    @Override
    public void initialBgfx() {
        boolean initResult = Bgfx.initForGlfw(windowPointer, prioritize3DEngine, gpuIndex);
        if (!initResult) {
            throw new HeadlessException("Fail to create AppWindow object!");
        }
    }

    @Override
    public void shutdownWindow() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowPointer);
        glfwDestroyWindow(windowPointer);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
