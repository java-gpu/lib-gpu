package tech.gpu.lib.directx;

import tech.gpu.lib.Graphics;
import tech.gpu.lib.directx.graphics.DirectxPixelFormatConverter;
import tech.gpu.lib.graphics.PixelFormatConverter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DirectxGraphics implements Graphics {

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getBackBufferWidth() {
        return 0;
    }

    @Override
    public int getBackBufferHeight() {
        return 0;
    }

    @Override
    public float getBackBufferScale() {
        return 0;
    }

    @Override
    public int getSafeInsetLeft() {
        return 0;
    }

    @Override
    public int getSafeInsetTop() {
        return 0;
    }

    @Override
    public int getSafeInsetBottom() {
        return 0;
    }

    @Override
    public int getSafeInsetRight() {
        return 0;
    }

    @Override
    public long getFrameId() {
        return 0;
    }

    @Override
    public float getDeltaTime() {
        return 0;
    }

    @Override
    public int getFramesPerSecond() {
        return 0;
    }

    @Override
    public GraphicsType getType() {
        return null;
    }

    @Override
    public float getPpiX() {
        return 0;
    }

    @Override
    public float getPpiY() {
        return 0;
    }

    @Override
    public float getPpcX() {
        return 0;
    }

    @Override
    public float getPpcY() {
        return 0;
    }

    @Override
    public float getDensity() {
        return 0;
    }

    @Override
    public boolean supportsDisplayModeChange() {
        return false;
    }

    @Override
    public Monitor getPrimaryMonitor() {
        return null;
    }

    @Override
    public Monitor getMonitor() {
        return null;
    }

    @Override
    public Monitor[] getMonitors() {
        return new Monitor[0];
    }

    @Override
    public DisplayMode[] getDisplayModes() {
        return new DisplayMode[0];
    }

    @Override
    public DisplayMode[] getDisplayModes(Monitor monitor) {
        return new DisplayMode[0];
    }

    @Override
    public DisplayMode getDisplayMode() {
        return null;
    }

    @Override
    public DisplayMode getDisplayMode(Monitor monitor) {
        return null;
    }

    @Override
    public boolean setFullscreenMode(DisplayMode displayMode) {
        return false;
    }

    @Override
    public boolean setWindowedMode(int width, int height) {
        return false;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setUndecorated(boolean undecorated) {

    }

    @Override
    public void setResizable(boolean resizable) {

    }

    @Override
    public void setVSync(boolean vsync) {

    }

    @Override
    public void setForegroundFPS(int fps) {

    }

    @Override
    public BufferFormat getBufferFormat() {
        return null;
    }

    @Override
    public boolean supportsExtension(String extension) {
        return false;
    }

    @Override
    public void setContinuousRendering(boolean isContinuous) {

    }

    @Override
    public boolean isContinuousRendering() {
        return false;
    }

    @Override
    public void requestRendering() {

    }

    @Override
    public boolean isFullscreen() {
        return false;
    }

    @Override
    public Cursor newCursor(BufferedImage pixmap, int xHotspot, int yHotspot) {
        return null;
    }

    @Override
    public void setCursor(Cursor cursor) {

    }

    @Override
    public PixelFormatConverter getPixelFormatConverter() {
        return new DirectxPixelFormatConverter();
    }
}
