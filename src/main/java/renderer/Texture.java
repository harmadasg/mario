package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private final String filePath;
    private final int textId;
    private final IntBuffer width;
    private final IntBuffer height;
    private final IntBuffer channels;

    public Texture(String filePath) {
        this.filePath = filePath;
        this.textId = glGenTextures();
        width = BufferUtils.createIntBuffer(1);
        height = BufferUtils.createIntBuffer(1);
        channels = BufferUtils.createIntBuffer(1);
        uploadTexture();
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void uploadTexture() {
        stbi_set_flip_vertically_on_load(true);
        glBindTexture(GL_TEXTURE_2D, textId);
        repeatImageInBothDirections();
        pixelateImage();
        var image = loadImage();
        validateImage(image);
        uploadToGPU(image);
        stbi_image_free(image);
    }

    private static void repeatImageInBothDirections() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

    private static void pixelateImage() {
        // When stretching the image pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking the image pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private ByteBuffer loadImage() {
        return stbi_load(filePath, width, height, channels, 0);
    }

    private void validateImage(ByteBuffer image) {
        if (image == null) {
            throw new IllegalStateException("Error: (Texture) Could not load image '" + filePath + "'");
        }
    }

    private void uploadToGPU(ByteBuffer image) {
        var colorScheme = getColorScheme(channels.get(0));
        glTexImage2D(GL_TEXTURE_2D, 0, colorScheme, width.get(0), height.get(0),
                0, colorScheme, GL_UNSIGNED_BYTE, image);
    }

    private int getColorScheme(int channels) {
        switch (channels) {
            case 3:
                return GL_RGB;
            case 4:
                return GL_RGBA;
            default:
                throw new IllegalArgumentException("Channel number not supported");
        }
    }
}
