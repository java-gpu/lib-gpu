package tech.lib.imgui.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Slf4j
public class FontUtils {
    public static ByteBuffer loadFontFromClassPath(String resourcePath) {
        return loadFontFromStream(FontUtils.class.getClassLoader().getResourceAsStream(resourcePath));
    }

    public static ByteBuffer loadFontFromStream(InputStream ins) {
        if (ins == null) {
            return null;
        }
        try (ins) {
            byte[] bytes = ins.readAllBytes();
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
