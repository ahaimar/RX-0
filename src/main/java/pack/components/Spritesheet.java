package pack.components;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import pack.renderer.Texture;

import java.util.ArrayList;
import java.util.List;


public class Spritesheet {

    private Texture texture;
    @Getter
    private List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i=0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    public int size() {
        return sprites.size();
    }
}
//private Texture texture;
//private List<Sprite> sprites;
//
//public Spritesheet(Texture texture, List<Sprite> sprites){
//
//    this.texture = texture;
//    this.sprites = sprites;
//}
//
//public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing){
//
//    this.sprites = new ArrayList<>();
//
//    this.texture = texture;
//    int currentX = 0;
//    int currentY = texture.getHeight() - spriteHeight;
//    for (int i = 0; i < numSprites; i++){
//
//        float topY = (currentY + spriteHeight) / (float)(texture.getHeight());
//        float rightX = (currentX + spriteWidth) / (float)(texture.getWidth());
//        float leftX = currentX / (float)texture.getWidth();
//        float bottomY = currentY / (float)texture.getHeight();
//
//        if ((texture.getWidth() % (spriteWidth + spacing) != 0) ||
//                (texture.getHeight() % (spriteHeight + spacing) != 0)) {
//            throw new RuntimeException("Spritesheet dimensions do not align with sprite size and spacing!");
//        }
//
//        Vector2f[] texCoords = {
//                new Vector2f(rightX, topY),
//                new Vector2f(rightX, bottomY),
//                new Vector2f(leftX, bottomY),
//                new Vector2f(leftX, topY)
//        };
//        Sprite sprite = new Sprite();
//        sprite.setTexture(this.texture);
//        sprite.setTexCoords(texCoords);
//        sprite.setWidth(spriteWidth);
//        sprite.setHeight(spriteHeight);
//        this.sprites.add(sprite);
//
//        currentX += spriteWidth + spacing;
//        if (currentX + spriteWidth > texture.getWidth()){
//            currentX = 0;
//            currentY -= spriteHeight + spacing;
//        }
//    }
//}
//
//public Sprite getSprite(int index) {
//
//    return sprites.get(index);
//}
//
//public int size() {
//
//    return sprites.size();
//}