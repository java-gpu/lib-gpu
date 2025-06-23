package tech.lib.bgfx;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tech.lib.bgfx.app.AwtAppWindow;
import tech.lib.bgfx.jni.Bgfx;
import tech.lib.bgfx.util.JniLogger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class BgfxTest {

    @BeforeAll
    public static void setUp() {
        var jniLogger = JniLogger.getInstance();
        jniLogger.setStopThread(false);
        log.debug("🔍 Library path: {}", System.getProperty("java.library.path"));
    }

    @AfterAll
    public static void tearDown() {
        var jniLogger = JniLogger.getInstance();
        jniLogger.setStopThread(true);
    }

    @Test
    @Disabled
    public void testInit() throws Throwable {

        log.debug("Starting BGFX Test....");
        AwtAppWindow window = new AwtAppWindow(1280, 720, "BGFX Test Window");
        assertNotNull(window);

        for (int i = 0; i < 3; i++) {
            Bgfx.frame();
            log.debug("🌀 frame {}", (i + 1));
        }
        Thread.sleep(1000);
        //        SwingUtilities.invokeLater(window::dispose);
        log.debug("✅ bgfx shutdown successfully!");
    }
}
