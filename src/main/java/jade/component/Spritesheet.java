package jade.component;

import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {

    private final List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numberOfSprites, int spacing) {
        sprites = new ArrayList<>();
        addSprites(texture, spriteWidth, spriteHeight, numberOfSprites, spacing);
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }

    private void addSprites(Texture texture, int spriteWidth, int spriteHeight, int numberOfSprites, int spacing) {
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight; // start in the top left corner
        for (int i = 0; i < numberOfSprites; i++) {
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] textureCoordinates = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite(texture, textureCoordinates);
            sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }
}
