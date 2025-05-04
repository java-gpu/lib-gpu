package tech.game.superjumper.graphics;

import tech.lib.bgfx.jni.Bgfx;

import javax.swing.*;
import java.awt.*;

public class Renderer {
    private final Canvas canvas;
    private final JFrame frame;
    private boolean initialized = false;

    public Renderer(JFrame frame, Canvas canvas) {
        this.frame = frame;
        this.canvas = canvas;
        init();
    }

    private void init() {
        long hwnd = Bgfx.getNativeHandler(frame, canvas);
        Bgfx.init(hwnd);
        Bgfx.reset(canvas.getWidth(), canvas.getHeight());
        initialized = true;
    }

    public void render(float playerX, float playerY) {
        if (!initialized) return;

        Bgfx.touch(0); // Clears the view
        Bgfx.setViewRect(0, 0, 0, canvas.getWidth(), canvas.getHeight());

        // Bgfx.beginFrame();

        // Draw a simple rectangle to represent the player
        float px = playerX;
        float py = canvas.getHeight() - playerY - 50; // Flip Y
        float width = 30;
        float height = 50;

        Bgfx.drawQuad(px, py, width, height, 0xFF00FFFF); // ARGB pink

        // Bgfx.bgfxEndFrame();
        Bgfx.frame();
    }

    public void dispose() {
        Bgfx.shutdown();
    }
}
