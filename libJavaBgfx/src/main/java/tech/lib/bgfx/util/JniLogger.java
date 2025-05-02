package tech.lib.bgfx.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.lib.bgfx.jni.JniLogData;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

@Getter
@Slf4j
public final class JniLogger {
    @Getter
    public static final JniLogger instance = new JniLogger();

    private final Deque<JniLogData> logDataQueue = new LinkedList<>();
    private final Thread loggingQueueHandler;
    @Setter
    private boolean stopThread;

    private JniLogger() {
        // Singleton
        loggingQueueHandler = new Thread(() -> {
            while (!stopThread) {
                try {
                    var logData = logDataQueue.removeFirst();
                    Logger logger = LoggerFactory.getLogger("tech.lib.jni." + logData.getFileName());
                    switch (logData.getLogLevel()) {
                        case DEBUG:
                            logger.debug(logData.getMessage());
                            break;
                        case INFO:
                            logger.info(logData.getMessage());
                            break;
                        case ERROR:
                            logger.error(logData.getMessage());
                            break;
                        case TRACE:
                            logger.trace(logData.getMessage());
                            break;
                        default:
                            log.debug("No log writing because no level defined! {}", logData.getMessage());
                            break;
                    }
                } catch (NoSuchElementException e) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        log.debug("Starting the JNI logger...");
        loggingQueueHandler.start();
    }

    public void log(JniLogData.LogLevel level, String fileName, String message) {
        var logData = new JniLogData(level, fileName, message);
        logDataQueue.addLast(logData);
    }

}
