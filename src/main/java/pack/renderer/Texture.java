package pack.renderer;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

@Getter
@Setter
public class Texture {

    private String filepath;

    private transient int texID;

    private int width, height;

    public void init(String filepath) {
        if (filepath == null || filepath.isEmpty()) {
            throw new IllegalArgumentException("Filepath cannot be null or empty.");
        }

        this.filepath = filepath;
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

            if (image != null) {
                try {
                    this.width = width.get(0);
                    this.height = height.get(0);
                    int format = channels.get(0) == 4 ? GL_RGBA : GL_RGB;

                    glTexImage2D(GL_TEXTURE_2D, 0, format, width.get(0), height.get(0), 0,
                            format, GL_UNSIGNED_BYTE, image);
                } finally {
                    stbi_image_free(image);
                }
            } else {
                throw new RuntimeException("Failed to load texture: " + filepath);
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return texID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Texture)) return false;
        Texture oTex = (Texture)o;
        return oTex.getWidth() == this.width && oTex.getHeight() == this.height &&
                oTex.getId() == this.texID &&
                oTex.getFilepath().equals(this.filepath);
    }

}