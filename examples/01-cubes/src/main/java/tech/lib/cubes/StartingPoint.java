package tech.lib.cubes;

import ch.qos.logback.core.util.StringUtil;
import tech.lib.bgfx.app.AppWindow;
import tech.lib.bgfx.util.PlatformInfo;

public class StartingPoint {
    public static void main(String[] args) {
        String vsShaderPath = System.getProperty("VS_SHADER_PATH");
        String shaderBuildTarget = switch (PlatformInfo.getInstance().getPlatformType()) {
            case MACOS -> "metal";
            case WINDOWS -> "direct3d11";
            case LINUX -> "vulkan";
        };
        if (StringUtil.isNullOrEmpty(vsShaderPath)) {
            vsShaderPath = "external/bgfx/.build/shader/" + shaderBuildTarget + "/vs_cubes.bin";
        }
        String fsShaderPath = System.getProperty("VS_SHADER_PATH");
        if (StringUtil.isNullOrEmpty(fsShaderPath)) {
            fsShaderPath = "external/bgfx/.build/shader/" + shaderBuildTarget + "/fs_cubes.bin";
        }
        AppWindow window = new AppWindow(1280, 720, "Cubes example");
        try {
            window.loadShaderProgram(vsShaderPath, fsShaderPath);
        } finally {
            window.shutdownBgfx();
        }
    }
}
