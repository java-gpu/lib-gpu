package tech.lib.imgui.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Slf4j
public class FontUtils {
    public static ByteBuffer loadFontFromResource(String resourcePath) {
        try (InputStream is = FontUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException(resourcePath);
            }
            byte[] bytes = is.readAllBytes();
            ByteBuffer fontBuffer = ByteBuffer.wrap(bytes);
            fontBuffer.put(bytes);
            fontBuffer.flip();
            return fontBuffer;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
