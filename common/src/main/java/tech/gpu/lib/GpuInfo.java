package tech.gpu.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpuInfo {
    private int index;
    private String name;
    private long pointer;
}
