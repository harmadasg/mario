package jade.component;

import org.joml.Vector2f;
import renderer.Texture;

public class Sprite {

    private static final Vector2f[] DEFAULT = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    private final Texture texture;
    private final Vector2f[] textureCoordinates;

    public Sprite(Texture texture) {
        this(texture, DEFAULT);
    }

    public Sprite(Texture texture, Vector2f[] textureCoordinates) {
        this.texture = texture;
        this.textureCoordinates = textureCoordinates;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
