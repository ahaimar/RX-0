package pack.matriale;

import org.joml.Vector2f;
import pack.components.Sprite;
import pack.components.SpriteRenderer;
import pack.objectes.GameObject;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        if (sprite == null) {
            throw new IllegalArgumentException("Sprite cannot be null");
        }

        // Create the game object with default position and specified size
        GameObject block = new GameObject(
                "Sprite_Object_" + System.currentTimeMillis(),
                new TransForm(new Vector2f(), new Vector2f(sizeX, sizeY)),
                0
        );

        // Add the sprite renderer component
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, Vector2f position, int zIndex) {
        if (sprite == null) {
            throw new IllegalArgumentException("Sprite cannot be null");
        }

        // Create the game object with specified position and size
        GameObject block = new GameObject(
                "Sprite_Object_" + System.currentTimeMillis(),
                new TransForm(position, new Vector2f(sizeX, sizeY)),
                zIndex
        );

        // Add the sprite renderer component
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}

