package tech.lib.bgfx;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.lib.bgfx.jni.Bgfx;
import tech.lib.bgfx.util.JniLogger;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class TestBgfx {
    public static void main(String[] args) throws InterruptedException {
        Logger logger = LoggerFactory.getLogger(TestBgfx.class);
        logger.debug("🔍 Library path: {}", System.getProperty("java.library.path"));

        logger.debug("✅ Starting BGFX Test");

        var jniLogger = JniLogger.getInstance();
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        Canvas canvas = new Canvas();
        frame.add(canvas);

        try {
            logger.debug("\uD83D\uDD25 Is JNI logger stopped [{}]", jniLogger.isStopThread());
            long windowPointer = Bgfx.getNativeHandler(frame, canvas);
            logger.debug("Windows pointer [{}]", windowPointer);

            boolean initSuccess = Bgfx.init(windowPointer);
            if (initSuccess) {
                logger.debug("🟢 bgfx initialized success!");

                for (int i = 0; i < 3; i++) {
                    Bgfx.frame();
                    logger.debug("🌀 frame {}", (i + 1));
                }
                Bgfx.shutdown();
                logger.debug("🔴 bgfx shutdown");
            } else {
                logger.debug("🔴 bgfx initialized fail!");
            }
        } finally {
            Thread.sleep(2000);
            jniLogger.setStopThread(true);
        }
    }
}
