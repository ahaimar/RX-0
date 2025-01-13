package pack.components;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import pack.renderer.Texture;

public class Sprite {

    @Getter
    @Setter
    private float width, height;

    private Texture texture = null;
    @Setter
    private  Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

    public Sprite() {
        this.width = 0;
        this.height = 0;
    }

    public Sprite(Texture texture, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }



    public Texture getTexture() {

        return this.texture;
    }

    public Vector2f[] getTexCoords() {

        return this.texCoords;
    }

    public void setTexture(Texture texture) {

        if (texture == null) {
            throw new IllegalArgumentException("Texture cannot be null");
        }
        this.texture = texture;
    }

    public int getTexId() {

        return texture == null?  -1 : texture.getId();
    }

}




























