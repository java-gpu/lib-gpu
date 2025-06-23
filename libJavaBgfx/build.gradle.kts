val lombokVersion: String by project
val slf4jVersion: String by project
val lwjglVersion: String by project

val isMac = rootProject.extra["isMac"] as Boolean
val isLinux = rootProject.extra["isLinux"] as Boolean
val isWindows = rootProject.extra["isWindows"] as Boolean
val platform = rootProject.extra["platform"] as String
val arch = rootProject.extra["arch"] as String
val externalBuildScript = rootProject.extra["externalBuildScript"] as String
val jnaVersion = rootProject.extra["jnaVersion"] as String
val imguiJavaVersion = rootProject.extra["imguiJavaVersion"] as String

val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else if (arch.startsWith("ppc"))
                "natives-linux-ppc64le"
            else if (arch.startsWith("riscv"))
                "natives-linux-riscv64"
            else
                "natives-linux"

        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } ->
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"

        arrayOf("Windows").any { name.startsWith(it) } ->
            if (arch.contains("64"))
                "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
            else
                "natives-windows-x86"

        else ->
            throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}


repositories {
    mavenCentral()
}

dependencies {
    api("net.java.dev.jna:jna:$jnaVersion")
    api("net.java.dev.jna:jna-platform:$jnaVersion")
    api("io.github.spair:imgui-java-binding:$imguiJavaVersion")
    // api("io.github.spair:imgui-java-lwjgl3:$imguiJavaVersion")

    val nativeDependency = when {
        isMac -> "io.github.spair:imgui-java-natives-macos:$imguiJavaVersion"
        isLinux -> "io.github.spair:imgui-java-natives-linux:$imguiJavaVersion"
        isWindows -> "io.github.spair:imgui-java-natives-windows:$imguiJavaVersion"
        else -> null
    }
    nativeDependency?.let { api(it) }

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nuklear")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nuklear", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-par", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
}

fun nativeCommand(vararg args: String): List<String> = when {
    isMac -> listOf("/bin/zsh", externalBuildScript) + args
    isLinux -> listOf("/bin/bash", externalBuildScript) + args
    isWindows -> listOf("cmd", "/c", externalBuildScript) + args
    else -> listOf("echo", "Unsupported platform")
}

tasks.register<Exec>("buildBgfx") {
    commandLine(nativeCommand("buildBgfx"))
}

tasks.register<Exec>("cleanBgfx") {
    commandLine(nativeCommand("cleanBgfx"))
}

tasks.register<Exec>("compileBgfxTools") {
    dependsOn("buildBgfx")
    mustRunAfter("buildBgfx")

    commandLine(
        nativeCommand(
            "executeCommandInDir", if (isWindows) "nmake tools" else "make tools",
            "${projectDir}/../../external/bgfx"
        )
    )
}

tasks.register<Exec>("generateJNIHeaders") {
    dependsOn("compileBgfxTools")
    mustRunAfter("compileBgfxTools")

    val script = if (isWindows) "${projectDir}/build.bat" else "${projectDir}/build.sh"
    val command = listOf(
        script,
        layout.buildDirectory.asFile.get().absolutePath,
        "generateJNIHeaders",
        lombokVersion, jnaVersion, slf4jVersion
    )

    commandLine(
        if (isWindows) listOf("cmd", "/c") + command
        else listOf(if (isMac) "/bin/zsh" else "/bin/bash") + command
    )
}

tasks.register<Exec>("generateNativeBuild") {
    dependsOn("generateJNIHeaders")
    mustRunAfter("generateJNIHeaders")

    val script = if (isWindows) "${projectDir}/build.bat" else "${projectDir}/build.sh"
    val command = listOf(
        script,
        layout.buildDirectory.asFile.get().absolutePath,
        "generateNativeBuild",
        lombokVersion, jnaVersion, slf4jVersion
    )

    commandLine(
        if (isWindows) listOf("cmd", "/c") + command
        else listOf(if (isMac) "/bin/zsh" else "/bin/bash") + command
    )
}

tasks.register<Exec>("compileNative") {
    dependsOn("generateNativeBuild")
    mustRunAfter("generateNativeBuild")

    val script = if (isWindows) "${projectDir}/build.bat" else "${projectDir}/build.sh"
    val command = listOf(
        script,
        layout.buildDirectory.asFile.get().absolutePath,
        "compileNative",
        lombokVersion, jnaVersion, slf4jVersion
    )

    commandLine(
        if (isWindows) listOf("cmd", "/c") + command
        else listOf(if (isMac) "/bin/zsh" else "/bin/bash") + command
    )
}

tasks.named("build") {
    dependsOn("compileNative")
    mustRunAfter("compileNative")
}

tasks.register<JavaExec>("runTest") {
    dependsOn("build", "compileNative")
    mustRunAfter("build", "compileNative")

    mainClass.set("tech.lib.bgfx.TestBgfx")
    classpath = sourceSets["main"].runtimeClasspath

    val libPath = when (platform) {
        "windows" -> listOf(
            "${layout.buildDirectory.asFile.get().absolutePath}/libs",
            "${rootProject.projectDir}/external/bgfx/.build/win64_vs2022/bin"
        ).joinToString(";")

        "osx" -> listOf(
            "${layout.buildDirectory.asFile.get().absolutePath}/libs",
            "${rootProject.projectDir}/external/bgfx/.build/$platform-$arch/bin"
        ).joinToString(":")

        "linux" -> listOf(
            "${layout.buildDirectory.asFile.get().absolutePath}/libs",
            "${rootProject.projectDir}/external/bgfx/.build/linux64_gcc/bin"
        ).joinToString(":")

        else -> ""
    }

    jvmArgs = listOf("-Djava.library.path=$libPath", "-XstartOnFirstThread")
}

tasks.named<Test>("test") {
    val nativePaths = when (platform) {
        "windows" -> listOf(
            "${layout.buildDirectory.asFile.get().absolutePath}/libs",
            "${rootProject.projectDir}/external/bgfx/.build/win64_vs2022/bin"
        )

        "osx" -> listOf(
            "${layout.buildDirectory.asFile.get().absolutePath}/libs",
            "${rootProject.projectDir}/external/bgfx/.build/$platform-$arch/bin"
        )

        "linux" -> listOf(
            "${layout.buildDirectory.asFile.get().absolutePath}/libs",
            "${rootProject.projectDir}/external/bgfx/.build/linux64_gcc/bin"
        )

        else -> listOf()
    }

    val separator = if (platform == "windows") ";" else ":"
    systemProperty("java.library.path", nativePaths.joinToString(separator))
}
