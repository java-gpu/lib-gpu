package tech.lib.bgfx.graphics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextureHandle {
    public static final long INVALID_INDEX = 0xFFFF;
    public static final TextureHandle INVALID_HANDLE = new TextureHandle(INVALID_INDEX);

    private long textureId;

    @Override
    public int hashCode() {
        return Long.hashCode(textureId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TextureHandle)) {
            return false;
        }
        return this.textureId == ((TextureHandle) obj).textureId;
    }

    @Override
    public String toString() {
        return isValid() ? "TextureHandle(" + textureId + ")" : "TextureHandle(INVALID)";
    }

    public boolean isValid() {
        return textureId != INVALID_INDEX;
    }
}
