package tech.lib.bgfx.app;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShaderHandler {
    private long vertexShaderHandler;
    private long fragmentShaderHandler;
    private long program;
}
