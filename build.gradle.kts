import java.util.*

val javaSourceCompatibility: String by project
val applicationVersion: String by project
val applicationGroup: String by project
val lombokVersion: String by project
val slf4jVersion: String by project
val jnaVersion: String by project
val logbackVersion: String by project
val imguiJavaVersion: String by project
val mockitoVersion: String by project
val junitVersion: String by project

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    // dependencies {
    //     classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootPluginVersion")
    //     classpath("io.spring.gradle:dependency-management-plugin:$springDependenciesManagementVersion")
    //     classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:latest.release")
    //     classpath("io.freefair.gradle:lombok-plugin:latest.release")
    // }
}

plugins {
    // id("org.springframework.boot") version springBootPluginVersion apply false
    // id("io.spring.dependency-management") version springDependenciesManagementVersion apply false
    id("io.freefair.lombok") version "8.11" apply false
    // id("org.sonarqube") version sonarqubePluginVersion apply false
    // id("org.hibernate.orm") version hibernatePluginVersion apply false
}

val osName = System.getProperty("os.name").lowercase()
val isMac = osName.contains("mac")
val isWindows = osName.contains("win")
val isLinux = osName.contains("nux") || osName.contains("nix")
val externalBuildScript = layout.projectDirectory.asFile.absolutePath +
        if (isWindows) "/support-scripts/build-bgfx.bat" else "/support-scripts/build-bgfx.sh"
val genieExecPath = layout.projectDirectory.file(
    when {
        isMac -> "external/genie/bin/darwin/genie"
        isLinux -> "external/genie/bin/linux/genie"
        else -> "external/genie/bin/windows/genie"
    }
)

println("Build script: $externalBuildScript")
println("Genie script: $genieExecPath")

val platform = when {
    osName.contains("mac") -> "osx"
    osName.contains("win") -> "windows"
    else -> "linux"
}
val arch = if (System.getProperty("os.arch") == "aarch64") "arm64" else "x86_64"

extra["osName"] = osName
extra["isMac"] = isMac
extra["isLinux"] = isLinux
extra["isWindows"] = isWindows
extra["platform"] = platform
extra["arch"] = arch
extra["externalBuildScript"] = externalBuildScript
extra["jnaVersion"] = jnaVersion
extra["imguiJavaVersion"] = imguiJavaVersion

subprojects {
    buildscript {
        repositories {
            mavenLocal()
            mavenCentral()
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    apply(plugin = "java-library")
    apply(plugin = "jacoco")
    apply(plugin = "io.freefair.lombok")

    group = applicationGroup
    version = applicationVersion

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaSourceCompatibility))
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/groups/public/")
        maven("https://packages.confluent.io/maven/")
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()

        finalizedBy(tasks.named("jacocoTestReport"))

        extensions.configure<JacocoTaskExtension> {
            isEnabled = true
            setDestinationFile(layout.buildDirectory.file("jacoco/${name}.exec").get().asFile)
            sessionId = UUID.randomUUID().toString()
            isDumpOnExit = true
            output = JacocoTaskExtension.Output.FILE
            // address, port, isJmx are NOT valid here, remove them
        }
    }

    tasks.named<JacocoReport>("jacocoTestReport").configure {
        dependsOn(tasks.named<Test>("test"))
        mustRunAfter(tasks.named<Test>("test"))

        reports {
            html.required.set(true)
            xml.required.set(true)
            csv.required.set(true)

            html.outputLocation.set(layout.buildDirectory.dir("jacoco/html"))
            xml.outputLocation.set(layout.buildDirectory.file("jacoco/jacoco-report.xml"))
            csv.outputLocation.set(layout.buildDirectory.file("jacoco/jacoco-report.csv"))
        }

        // Delay setting classDirectories until after project evaluation
//        project.afterEvaluate {
//            val excludedPatterns = listOf("**/ex/**", "**/pojo/**", "**/model/**", "**/dto/**", "**/config/**")
//            classDirectories.setFrom(
//                classDirectories.files.map { dir ->
//                    fileTree(dir) {
//                        exclude(excludedPatterns)
//                    }
//                }
//            )
//        }
    }

    tasks.named<ProcessResources>("processTestResources") {
        from("../share-resources/test-resources") {
            include("**/*")
        }
    }

    tasks.named<Javadoc>("javadoc") {
        exclude("**/InternalServiceConfig.java") // or "**/InternalServiceConfig/**" for package

        doFirst {
            (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
        }
    }

    afterEvaluate {
        dependencies {
            add("compileOnly", "org.projectlombok:lombok:$lombokVersion")
            add("implementation", "org.slf4j:slf4j-api:$slf4jVersion")
            add("implementation", "ch.qos.logback:logback-classic:$logbackVersion")
            add("testImplementation", "org.mockito:mockito-core:$mockitoVersion")
            add("testImplementation", "org.junit.jupiter:junit-jupiter:$junitVersion")
            add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        }
    }
}
