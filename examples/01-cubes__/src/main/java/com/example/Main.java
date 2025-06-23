package com.example;

import org.lwjgl.bgfx.BGFX;
import org.lwjgl.bgfx.BGFXInit;
import org.lwjgl.bgfx.BGFXPlatformData;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.bgfx.BGFX.BGFX_RENDERER_TYPE_COUNT;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        MemoryStack stack = MemoryStack.stackPush();
        BGFXPlatformData platformData = BGFXPlatformData.calloc(stack);

        BGFXInit init = BGFXInit.calloc(stack);
        init.type(BGFX_RENDERER_TYPE_COUNT); // Auto-select renderer
        init.resolution().width(800).height(600).reset(0);

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        long window = GLFW.glfwCreateWindow(800, 600, "BGFX + LWJGL", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);
        platformData.nwh(window); // native window handle
        init.platformData(platformData);

        //            SwingUtilities.invokeLater(() -> {
        if (!BGFX.bgfx_init(init)) {
            throw new RuntimeException("Failed to initialize BGFX");
        }
        //            });

        //            while (!GLFW.glfwWindowShouldClose(window)) {
        //                BGFX.bgfx_touch(0); // dummy frame
        //                BGFX.bgfx_frame(false);
        //                GLFW.glfwPollEvents();
        //            }
        //
        //            BGFX.bgfx_shutdown();

        Thread.sleep(1000 * 10);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}
