package tech.lib.bgfx.jni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JniLogData {
    private LogLevel logLevel;
    private String fileName;
    private String message;

    public enum LogLevel {
        DEBUG, INFO, ERROR, TRACE
    }
}
