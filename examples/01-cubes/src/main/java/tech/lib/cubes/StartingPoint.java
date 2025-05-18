package tech.lib.cubes;

import ch.qos.logback.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.app.AppWindow;
import tech.lib.bgfx.app.ShaderHandler;
import tech.lib.bgfx.enu.AppConst;
import tech.lib.bgfx.enu.BgfxDebugFlag;
import tech.lib.bgfx.enu.BgfxResetFlag;
import tech.lib.bgfx.util.PlatformInfo;
import tech.lib.ui.event.CustomMouseEvent;
import tech.lib.ui.input.queue.AppWindowEventQueueInitializer;
import tech.lib.ui.jni.EventManager;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class StartingPoint {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        // Please do not run this main class in IDE because you need to link with generated libraries
        // Let move to project root folder and run the following commands:
        // gradle buildBgfx compileBgfxTools compileNative
        // gradle :01-cubes:runExample
        String vsShaderPath = System.getProperty("VS_SHADER_PATH");
        String shaderBuildTarget = switch (PlatformInfo.getInstance().getPlatformType()) {
            case MACOS -> "metal";
            case WINDOWS -> "direct3d11";
            case LINUX -> "vulkan";
        };
        if (StringUtil.isNullOrEmpty(vsShaderPath)) {
            vsShaderPath = "../../external/bgfx/.build/shaders/" + shaderBuildTarget + "/vs_cubes.bin";
        }
        String fsShaderPath = System.getProperty("VS_SHADER_PATH");
        if (StringUtil.isNullOrEmpty(fsShaderPath)) {
            fsShaderPath = "../../external/bgfx/.build/shaders/" + shaderBuildTarget + "/fs_cubes.bin";
        }
        final String vsShaderPathFinal = vsShaderPath;
        final String fsShaderPathFinal = fsShaderPath;
        AppWindow appWindow = new AppWindow(AppConst.ENTRY_DEFAULT_WIDTH, AppConst.ENTRY_DEFAULT_HEIGHT, "01-Cube Example");
        ShaderHandler shaderHandler = appWindow.loadShaderProgram(vsShaderPathFinal, fsShaderPathFinal);
        if (shaderHandler == null) {
            log.error("Load shader fail!!");
        }
        //        SwingUtilities.invokeAndWait();
        AppWindowEventQueueInitializer.monitorWindowEventQueue(appWindow);

        CubeRenderer cubeRenderer = new CubeRenderer(appWindow, BgfxResetFlag.NONE, BgfxDebugFlag.TEXT);
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cubeRenderer.renderFrame(4);
        });
        Thread thread = new Thread(() -> {
            while (true) {
                var appEvent = EventManager.pollUiEvent(appWindow.getWindowPtr());
                if (appEvent != null) {
                    if (appEvent instanceof CustomMouseEvent) {
                        cubeRenderer.setLatestMouseEvent((CustomMouseEvent) appEvent);
                    }
                    SwingUtilities.invokeLater(cubeRenderer::update);
                }
            }
        });
        thread.start();
        //        log.debug("Bgfx.submit");
        //        SwingUtilities.invokeLater(() -> Bgfx.submit(0, appWindow.getShaderHandler().getProgram()));
        //        log.debug("Bgfx.frame");
        //        SwingUtilities.invokeLater(Bgfx::frame);
    }
}
