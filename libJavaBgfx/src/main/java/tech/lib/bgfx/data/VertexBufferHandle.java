package tech.lib.bgfx.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VertexBufferHandle {
    private final long ptr;
}
