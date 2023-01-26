package jade.component;

import lombok.Getter;
import org.joml.Vector2f;
import renderer.Texture;

@Getter
public class Sprite {

    private static final Vector2f[] DEFAULT = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    Texture texture;
    Vector2f[] textureCoordinates;
    public Sprite(Texture texture) {
        this(texture, DEFAULT);
    }

    public Sprite(Texture texture, Vector2f[] textureCoordinates) {
        this.texture = texture;
        this.textureCoordinates = textureCoordinates;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
