package tech.lib.bgfx.util;

import lombok.Data;
import lombok.Getter;

@Data
public class PlatformInfo {
    @Getter
    private static final PlatformInfo instance = new PlatformInfo();

    private PlatformType platformType;
    private PlatformArch platformArch;

    private PlatformInfo() {
        // Singleton
        String osName = System.getProperty("os.name");
        String osNameLowerCase = osName.toLowerCase();
        if (osNameLowerCase.contains("nux")) {
            platformType = PlatformType.LINUX;
        } else if (osNameLowerCase.contains("mac")) {
            platformType = PlatformType.MACOS;
        } else if (osNameLowerCase.contains("win")) {
            platformType = PlatformType.WINDOWS;
        }
        String osArch = System.getProperty("os.arch");
        String osArchLowercase = osArch.toLowerCase();
        if (osArchLowercase.contains("aarch64")) {
            platformArch = PlatformArch.ARM64;
        } else if (osArchLowercase.contains("x64")) {
            platformArch = PlatformArch.x86_64;
        } else if (osArchLowercase.contains("x86")) {
            platformArch = PlatformArch.X86;
        } else if (osArchLowercase.contains("aarch")) {
            platformArch = PlatformArch.ARM86;
        }
    }

    public enum PlatformType {
        MACOS, WINDOWS, LINUX
    }

    public enum PlatformArch {
        X86, x86_64, ARM64, ARM86
    }
}
