package tech.gpu.lib.metal.graphics;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import tech.gpu.lib.ex.PixelFormatNotSupportedException;
import tech.gpu.lib.graphics.PixelFormat;
import tech.gpu.lib.graphics.Texture;
import tech.gpu.lib.jni.GpuInfo;
import tech.gpu.lib.jni.GpuManager;
import tech.gpu.lib.metal.MetalApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class TextureTest {

    @BeforeAll
    @EnabledOnOs(OS.MAC)
    public static void startUp() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            List<GpuInfo> gpus = new LinkedList<>(GpuManager.gpuMap.values());
            assertFalse(gpus.isEmpty());
        }
    }

    @AfterAll
    @EnabledOnOs(OS.MAC)
    public static void shutDown() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            GpuManager.releaseAllGpus();
        }
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testCreateEmptyTexture() throws PixelFormatNotSupportedException {
        MetalApplication metalApplication = new MetalApplication();
        int width = 100;
        Texture texture = new Texture(metalApplication, width, 100, PixelFormat.RGBA8888);
        assertNotNull(texture);
        texture.release();
        assertEquals(width, texture.getWidth());
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testLoadTextureFromStream() throws PixelFormatNotSupportedException, IOException {
        MetalApplication metalApplication = new MetalApplication();
        var imageStream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("—Pngtree—correct icon_4602219.png"));
        Texture texture = new Texture(metalApplication, imageStream, PixelFormat.RGBA8888, true);
        assertNotNull(texture);
        texture.release();
        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("—Pngtree—correct icon_4602219.png")));
        assertEquals(image.getWidth(), texture.getWidth());
        // Load again without format
        imageStream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("—Pngtree—correct icon_4602219.png"));
        texture = new Texture(metalApplication, imageStream, true);
        assertEquals(image.getHeight(), texture.getHeight());
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testLoadTextureFromFile() throws PixelFormatNotSupportedException, URISyntaxException, IOException {
        MetalApplication metalApplication = new MetalApplication();
        var imageUri = Objects.requireNonNull(getClass().getClassLoader().getResource("—Pngtree—correct icon_4602219.png"));
        File imageFile = new File(imageUri.toURI());
        System.out.println("Image file path: " + imageFile);
        Texture texture = new Texture(metalApplication, imageFile, PixelFormat.RGBA8888, true);
        assertNotNull(texture);
        texture.release();
        BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("—Pngtree—correct icon_4602219.png")));
        assertEquals(image.getWidth(), texture.getWidth());
        // Load again without format
        texture = new Texture(metalApplication, imageFile, true);
        assertEquals(image.getHeight(), texture.getHeight());
    }
}
