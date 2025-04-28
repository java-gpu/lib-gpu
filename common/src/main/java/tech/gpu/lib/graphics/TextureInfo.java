package tech.gpu.lib.graphics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class TextureInfo {
    private long texturePointer;

    private int width;
    private int height;
    private int depth;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TextureInfo that = (TextureInfo) o;
        return texturePointer == that.texturePointer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(texturePointer);
    }
}
