package tech.game.superjumper;

import tech.game.superjumper.graphics.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends Canvas implements Runnable, KeyListener {
    private Thread thread;
    private boolean running = false;
    private final int WIDTH = 480, HEIGHT = 800;

    private Renderer renderer;
    private float playerX = 200, playerY = 100;
    private float velocityY = 0;
    private final float GRAVITY = -9.8f;
    private JFrame frame;

    public Game() {
        this.frame = new JFrame("SuperJumper Demo");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.setVisible(true);

        addKeyListener(this);
        setFocusable(true);

        renderer = new Renderer(frame, this);
        start();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1000000000.0 / 60.0;

        while (running) {
            long now = System.nanoTime();
            double delta = (now - lastTime) / nsPerFrame;
            lastTime = now;

            update((float) (delta / 60.0));
            renderer.render(playerX, playerY);

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        renderer.dispose();
    }

    private void update(float deltaTime) {
        velocityY += GRAVITY * deltaTime;
        playerY += velocityY;
        if (playerY < 0) {
            playerY = 0;
            velocityY = 0;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = 5;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        new Game();
    }
}
