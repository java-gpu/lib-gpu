package tech.lib.bgfx.app;

import org.slf4j.LoggerFactory;
import tech.lib.bgfx.jni.Bgfx;

public interface AppWindow {
    void initialBgfx();

    default void shutdownBgfx() {
        var log = LoggerFactory.getLogger(getClass());
        log.debug("🔴 bgfx shutdown");
        Bgfx.shutdown();
    }

    void shutdownWindow();

    /**
     * Load shader program + set as shaderHandler for this windows + return loaded program.
     *
     * @param vsShaderPath Vertex Shader Handler path
     * @param fsShaderPath Fragment Shader Handler path
     * @return Loaded Shader program
     */
    default ShaderHandler loadShaderProgram(String vsShaderPath, String fsShaderPath) {
        long vs = Bgfx.loadShader(vsShaderPath);
        long fs = Bgfx.loadShader(fsShaderPath);
        long program = Bgfx.createProgram(vs, fs, true);
        var shaderHandler = new ShaderHandler(vs, fs, program);
        setShaderHandler(shaderHandler);
        return shaderHandler;
    }

    void setShaderHandler(ShaderHandler handler);

    long getWindowPointer();

    boolean isPrioritize3DEngine();

    void setPrioritize3DEngine(boolean prioritize3DEngine);

    int getGpuIndex();

    void setGpuIndex(int gpuIndex);
}
