package tech.lib.bgfx.util;

import lombok.Getter;

import java.nio.charset.StandardCharsets;

public class StringUtils {

    @Getter
    private static final StringUtils instance = new StringUtils();

    public byte[] encodeToFixed4Bytes(char inputChar) {
        byte[] utf8 = (inputChar + "").getBytes(StandardCharsets.UTF_8);
        byte[] fixed = new byte[4];
        System.arraycopy(utf8, 0, fixed, 0, utf8.length);
        return fixed;
    }

    public String decodeFromFixed4Bytes(byte[] fixedBytes) {
        int actualLength = 4;
        for (int i = 0; i < 4; i++) {
            if (fixedBytes[i] == 0) {
                actualLength = i;
                break;
            }
        }
        return new String(fixedBytes, 0, actualLength, StandardCharsets.UTF_8);
    }
}
