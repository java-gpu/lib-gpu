package tech.lib.bgfx;

import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.app.AppWindow;
import tech.lib.bgfx.app.AwtAppWindow;
import tech.lib.bgfx.jni.Bgfx;
import tech.lib.bgfx.util.JniLogger;

import java.awt.*;

@Slf4j
public class TestBgfx {
    public static void main(String[] args) throws InterruptedException {
        log.debug("🔍 Library path: {}", System.getProperty("java.library.path"));

        log.debug("✅ Starting BGFX Test");

        var jniLogger = JniLogger.getInstance();
        AppWindow window;
        try {
            log.debug("\uD83D\uDD25 Is JNI logger stopped [{}]", jniLogger.isStopThread());
            window = new AwtAppWindow(1280, 720, "BGFX Window");
            Thread.sleep(1000);
            //            SwingUtilities.invokeLater(window::initialBgfx);
            log.debug("🟢 bgfx initialized success!");
            for (int i = 0; i < 3; i++) {
                Bgfx.frame();
                log.debug("🌀 frame {}", (i + 1));
            }
            //            window.addWindowListener(new WindowAdapter() {
            //                @Override
            //                public void windowClosing(WindowEvent e) {
            //                    window.dispose();
            //                }
            //            });
            log.debug("Window pointer: {}", window.getWindowPointer());
        } catch (HeadlessException e) {
            log.debug("🔴 bgfx initialized fail!");
        } finally {
            Thread.sleep(40 * 1000);
            jniLogger.setStopThread(true);
        }
    }
}
